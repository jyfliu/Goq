from functools import reduce
from typing import Dict

from Point import Point


# prioritizes bound points, if both are unbound prioritizes the second, if both are bound throws exception
def merge(first: Dict[Point, Point], second: Dict[Point, Point]) -> Dict[Point, Point]:
    ret = {}
    for k, v in first.items():
        ret[k] = v
    for k, v in second.items():
        if k not in ret or ret[k].unbound():
            ret[k] = v
        if k in ret and ret[k].bound() and v.bound() and ret[k] != v:
            raise ValueError("Merge failed")
    return ret


def merge_many(*args) -> Dict[Point, Point]:
    return reduce(merge, args, {})
