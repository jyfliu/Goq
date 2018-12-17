from typing import Set, Any, List, Dict, TYPE_CHECKING

import Hypothesis

class Knowledge(object):

    hypotheses: List[Any]
    hash: Set[Any]
    type: Dict[Any, Any]

    def __init__(self):
        self.hypotheses = [] # list of all hypotheses
        self.hash = set([]) # hash of all hypotheses
        self.type = {}

    def get_all_of(self, hypothesis: Hypothesis) -> List:
        """

        :param hypothesis:
        :return: all hypotheses with the same prefix
        """
        return self.type[hypothesis.prefix]

    def insert(self, hypothesis) -> None:
        if hypothesis in self.hash:
            return
        self.hypotheses.append(hypothesis)
        self.hash.add(hypothesis)
        if hypothesis.prefix in self.type:
            self.type[hypothesis.prefix].append(hypothesis)
        else:
            self.type[hypothesis.prefix] = [hypothesis]

    def contains(self, hypothesis: "Hypothesis") -> bool:
        return hypothesis in self.hash

    def print_hypotheses(self):
        for v in self.type.values():
            for h in v:
                print(">> ", h, sep="")
