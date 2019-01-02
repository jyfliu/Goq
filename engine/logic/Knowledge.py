from typing import Any, List, Dict

from logic import Hypothesis
from logic.util import get_debug


class Knowledge(object):

    hypotheses: List[Any]
    hash: Dict[Any, Any]
    type: Dict[Any, Any]

    def __init__(self):
        self.hypotheses = [] # list of all hypotheses
        self.hash = {} # hash of all hypotheses, value is a list of sources of each hypothesis
        self.type = {}

    def get_all_of(self, hypothesis: Hypothesis) -> List:
        """

        :param hypothesis:
        :return: all hypotheses with the same prefix
        """
        if hypothesis.prefix in self.type:
            return self.type[hypothesis.prefix]
        return []

    def insert(self, hypothesis: Hypothesis, sources: List[Any]=None) -> None:
        if hypothesis in self.hash:
            return
        self.hypotheses.append(hypothesis)
        if sources is None or not sources:
            self.hash[hypothesis] = ["God"]
        else:
            self.hash[hypothesis] = sources
        if hypothesis.prefix in self.type:
            self.type[hypothesis.prefix].append(hypothesis)
        else:
            self.type[hypothesis.prefix] = [hypothesis]

    def contains(self, hypothesis: "Hypothesis") -> bool:
        return hypothesis in self.hash

    def print_stack_trace(self, hypothesis: Hypothesis, depth: int=0, first: bool=True):
        if isinstance(hypothesis, str):
            print(hypothesis)
            return
        print("" if first else " "*(depth-2), "" if first else "| ", hypothesis, " ", left_implies(), " ", sep="", end="")
        if self.hash[hypothesis][0] == "God":
            print("God")
            return
        for s in self.hash[hypothesis]:
            self.print_stack_trace(s, depth+5+len(hypothesis.__str__()), first)
            first = False

    def print_hypotheses(self):
        for v in self.type.values():
            for h in v:
                print(">> ", h, sep="")
                if get_debug() >= 3:
                    self.print_stack_trace(h)

def left_implies() -> str:
    return "<-|"
