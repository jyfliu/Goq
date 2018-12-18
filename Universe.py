from typing import TYPE_CHECKING, List


import Hypothesis
import Knowledge
import Point
import Theorem


class Universe(object):

    def __init__(self) -> None:
        self.points = []
        self.knowledge = Knowledge.Knowledge()
        self.goals = []
        self.theorems = []
        self.generate_axioms()
        self.heat_death = 20

    # insert a bunch of axioms
    def generate_axioms(self):
        pass

    # add theorem to universe
    def admit(self, theorem: Theorem) -> Theorem:
        self.theorems += [theorem]
        return theorem

    # add hypothesis to goals
    def claim(self, hypothesis: Hypothesis) -> Hypothesis:
        for p in hypothesis.all_entities():
            assert p.bound(), "Goal must always contain bound entities"
            # (subject to change?) maybe like prove existence type goals
        self.goals += [hypothesis]
        if self.knowledge.contains(hypothesis):
            self.print_solved_goals(hypothesis)
        return hypothesis

    # add hypothesis to universe
    def pose(self, hypothesis: Hypothesis) -> Hypothesis:
        for p in hypothesis.all_entities():
            assert p.bound(), "Knowledge must only contain bound entities"
        if hypothesis in self.goals:
            self.print_solved_goals(hypothesis)
            self.goals = [h for h in self.goals if h != hypothesis]
        self.knowledge.insert(hypothesis)
        return hypothesis

    def pose_many(self, *args):
        for hypo in args:
            self.pose(hypo)

    def print_solved_goals(self, hypothesis: Hypothesis):
        for goal in self.goals:
            if goal == hypothesis:
                print("Solved ", goal)
                print("TODO: Print hypo stack trace")

    def print_knowledge(self):
        self.knowledge.print_hypotheses()

    def step(self) -> None:
        # apply theorems
        for theorem in self.theorems:
            returned = theorem.apply(self)
            self.pose_many(*returned)
        # add a construction database (somehow ??? )
        # If I'm not going to catch a fish, I might as well not catch a big fish.
        # ?????????????

    def run_til_heat_death(self):
        for _ in range(self.heat_death):
            if get_debug() == 2:
                self.print_knowledge()
            if get_debug() >= 1:
                print("Step %d"%_)
            self.step()
        if get_debug() == 2:
            self.print_knowledge()

    def why(self) -> None:
        print("I'd rather do something and regret it than regret doing nothing at all.")


def get_debug() -> int:
    """
    0 - no debug
    1 - short debug
    2 - long debug
    :return:
    """
    return 2
