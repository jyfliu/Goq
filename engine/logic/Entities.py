from functools import reduce
from itertools import permutations
from typing import Dict, Tuple, Set

from logic.Point import Point


class Entities(object):

    entities: Tuple[Set[Point], ...]

    def __init__(self, entities) -> None:
        self.entities = entities

    def bind(self, points: Dict[Point, Point]):
        return create_entities(tuple({p.bind(points) for p in col} for col in self.entities))

    def match(self, other) -> bool:
        if len(self.entities) != len(other.entities):
            return False
        # print([" ".join((x.name for x in z)) for z in self.entities],
        #       [" ".join((y.name for y in z)) for z in other.entities])
        return reduce(lambda a, b: a and b, [match_sets(x, y) for x, y in zip(self.entities, other.entities)])

    def __str__(self) -> str:
        return ", ".join(", ".join(map(str, x)) for x in self.entities)

    def __hash__(self) -> int:
        return hash(self.entities)

    def __eq__(self, other) -> bool:
        return self.entities == other.entities


def match_sets(set1, set2):
    if len(set1) != len(set2):
        return False
    l1 = list(set1)
    l2 = list(set2)
    for l in permutations(l2):
        if reduce(lambda a, b: a and b, [x.match(y) for x, y in zip(l1, l)]):
            return True
    return False


def create_entities(entities: Tuple[Set[Point],...]) -> Entities:
    tmp = Entities(entities)
    tmp.entities = entities
    return tmp

