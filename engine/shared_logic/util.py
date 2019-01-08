from functools import reduce
from typing import Dict, Any

from shared_logic.Point import Point


def merge_failed():
    return {"merge_failed": "fd8s9ah0qfixnb5809bfisj021z0oql012irxi02ihq"}


merge_cache = dict()


# prioritizes bound points, if both are unbound prioritizes the second, if both are bound throws exception
def merge(first: Dict[Point, Point], second: Dict[Point, Point]) -> Dict[Any, Any]:
    # cur_hash = (((k, v) for k, v in sorted(first.items(), key=str)), ((k, v) for k, v in sorted(second.items(), key=str)))
    # if cur_hash in merge_cache:
    #     return merge_cache[cur_hash]
    ret = {}
    for k, v in first.items():
        ret[k] = v
    for k, v in second.items():
        if k not in ret or ret[k].unbound():
            ret[k] = v
        if k in ret and ret[k].bound() and v.bound() and ret[k] != v:
            # return merge_failed()
            raise ValueError("Merge failed")
    # print("merge", first, second, ret)
    # merge_cache[cur_hash] = ret
    return ret


def merge_many(*args) -> Dict[Point, Point]:
    return reduce(merge, args, {})


_debug = 2

def get_debug() -> int:
    """
    -1 = dead silence
    0 = no debug
    1 = short debug
    2 = long debug
    :return:
    """
    return _debug
