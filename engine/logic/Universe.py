import time

from logic import Theorem
from shared_logic import Knowledge, Hypothesis
from shared_logic.util import get_debug


# I'd rather do something and regret it than regret doing nothing at all.
class Universe(object):

    def __init__(self) -> None:
        self.points = set()
        self.knowledge = Knowledge.Knowledge()
        self.goals = []
        self.theorems = []
        self.last_theorem_application = dict()
        self.heat_death = 20

    # insert a bunch of axioms
    def generate_axioms(self):
        pass

    # add theorem to universe
    def admit(self, theorem: Theorem) -> Theorem:
        self.theorems += [theorem]
        self.last_theorem_application[theorem] = []
        return theorem

    # add hypothesis to goals
    def claim(self, hypothesis: Hypothesis) -> Hypothesis:
        for p in hypothesis.all_entities():
            assert p.bound(), "Goal must always contain bound entities"
            if p not in self.points:
                self.points.add(p)
            # (subject to change?) maybe like prove existence type goals
        self.goals += [hypothesis]
        if self.knowledge.contains(hypothesis):
            self.print_solved_goals(hypothesis)
        return hypothesis

    # add hypothesis to universe
    def pose(self, hypothesis: Hypothesis) -> Hypothesis:
        self.derive(hypothesis, [])
        return hypothesis

    def pose_many(self, *args) -> None:
        for hypo in args:
            self.pose(hypo)

    def derive(self, hypothesis: Hypothesis, sources):
        for p in hypothesis.all_entities():
            assert p.bound(), "Knowledge must only contain bound entities"
        # print("DERIVING", hypothesis)
        if not hypothesis.valid(hypothesis):
            if get_debug() >= 1:
                print("critical error, attempted to derive", hypothesis)
            return
        self.knowledge.insert(hypothesis, sources)
        if hypothesis in self.goals:
            self.print_solved_goals(hypothesis)
            self.goals = [h for h in self.goals if h != hypothesis]
        return hypothesis

    def derive_many(self, list_of_hypos):
        for hypo, sources in list_of_hypos:
            self.derive(hypo, sources)

    def assert_points_unique(self):
        for p in self.points:
            for q in self.points:
                if p != q:
                    self.pose(Hypothesis.unequal(p, q))


    def print_solved_goals(self, hypothesis: Hypothesis) -> None:
        for goal in self.goals:
            if goal == hypothesis and get_debug() >= 0:
                print("Solved ", goal)
                self.knowledge.print_stack_trace(hypothesis)

    def print_knowledge(self) -> None:
        if get_debug() >= 0:
            self.knowledge.print_hypotheses()

    def step(self) -> None:
        # apply theorems
        for theorem in self.theorems:
            if get_debug() >= 2:
                print(theorem)
            # get the hypotheses signature of the theorem
            signature = [len(self.knowledge.get_all_of(x)) for x in theorem.hypotheses]
            if self.last_theorem_application[theorem] == signature:
                # if our universe hasn't changed significantly, don't repeat theorem call
                if get_debug() >= 2:
                    print("SKIPPED")
                continue
            returned = theorem.apply(self)
            self.derive_many(returned)
            # update the previous call signature
            self.last_theorem_application[theorem] = [len(self.knowledge.get_all_of(x)) for x in theorem.hypotheses]
            if get_debug() >= 2:
                print("returned: ",*[x[0] for x in returned])
            if get_debug() >= 2:
                print("DONE")
        # add a construction database (somehow ??? )
        # If I'm not going to catch a fish, I might as well not catch a big fish.
        # ?????????????

    # run until it no longer runs
    def run_til_heat_death(self, print_time=False) -> None:
        if get_debug() >= 1:
            print("BEGINNING RUN")
        start_time = time.time()
        for _ in range(self.heat_death):
            if get_debug() >= 2:
                self.print_knowledge()
            if get_debug() >= 1:
                print("Step %d" % _)
            before = len(self.knowledge.hypotheses)
            self.step()
            after = len(self.knowledge.hypotheses)
            if before == after:
                break
        if get_debug() >= 2:
            self.print_knowledge()
        if get_debug() >= 1 or print_time:
            print("TIME ELAPSED:", time.time()-start_time)

    def run_til_no_more_goals(self, print_time=False):
        if get_debug() >= 1:
            print("BEGINNING RUN")
        start_time = time.time()
        for _ in range(self.heat_death):
            if get_debug() >= 2:
                self.print_knowledge()
            if get_debug() >= 1:
                print("Step %d" % _)
            self.step()
            if not self.goals:
                break
        if get_debug() >= 2:
            self.print_knowledge()
        if get_debug() >= 1 or print_time:
            print("TIME ELAPSED:", time.time()-start_time)

    def run(self, fast=False, print_time=False):
        if fast:
            self.run_til_no_more_goals(print_time=print_time)
        else:
            self.run_til_heat_death(print_time=print_time)