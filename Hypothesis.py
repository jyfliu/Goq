from functools import reduce
from itertools import permutations, product
from typing import List, Callable, Dict, Set

from Entities import Entities, match_sets, create_entities
from Point import Point
from Universe import get_debug
import copy

from util import merge


class Hypothesis(object):

    entities: Entities
    prefix: str
    value: str

    def __init__(self, entities: Entities, *, prefix="<?H?>", value="<?V?>",
                 valid=lambda x: False, equal=None, update_map=None) -> None:
        '''

        :param entities: something made by Entities(), a tuple of frozensets with comparison
        :param prefix: a prefix, eg simtri, contri, etc
        :param value: an optional value, eg length, area, etc
        '''
        self.entities = entities
        self.prefix = prefix
        self.value = value
        self.valid = valid
        if equal is None:
            self.equal = lambda x, y: x.entities.match(y.entities) and x.value == y.value and x.prefix == y.prefix
        else:
            self.equal = equal
        if update_map is None and equal is None:
            self.update_map = update_map_bijection
        elif update_map is None:
            self.update_map = update_map_sep
        else:
            self.update_map = update_map

    def bind(self, points: Dict[Point, Point]):
        ret = Hypothesis(self.entities.bind(points),
                         prefix=self.prefix,
                         value=self.value,
                         valid=self.valid,
                         equal=self.equal)
        return ret

    def ent_list(self):
        return self.entities.entities

    def all_entities(self):
        return [p for s in self.ent_list() for p in s]

    def __eq__(self, other) -> bool:
        """
        by default returns if both hypotheses are identical up to permutation of individual entities
        can be overwritten
        :param other:
        :return:
        """
        if not self.valid(self) or not other.valid(other): # they must consider themselves valid
            return False
        return self.equal(self, other)

    def __hash__(self) -> int:
        return hash(self.__str__())

    def __repr__(self):
        return self.__str__()

    def __str__(self) -> str:
        if self.value != '<?V?>':
            return ''.join((self.prefix, '(', str(self.entities), ')=', self.value))
        else:
            return ''.join((self.prefix, '(', str(self.entities), ')'))


def valid(hypo: Hypothesis) -> bool:
    return hypo.valid(hypo)


def check(lengths: List[int], unique=None) -> Callable[[Hypothesis], bool]:
    if unique is None:
        unique = []

    def ret(hypo: Hypothesis) -> bool:
        if len(hypo.entities.entities) != len(lengths):
            return False
        for i, l in enumerate(lengths):
            if len(hypo.entities.entities[i]) != l:
                return False
        for tup in unique:
            tmp = set()
            sum = 0
            for id in tup:
                tmp = tmp | hypo.entities.entities[id]
                sum += lengths[id]
            if len(tmp) != sum:
                return False
        return True
    return ret


# Given a map M and two hypotheses, source and destination (s and d for short) satisfying
# d has only bound variables
# s has a combination of bound and unbound variables
# d and s have the same hypothesis name
# M[x] = x for all unbound variables x in s
# return a map N satisfying
# - if M[x] != x for some x then N[x] = M[x] (ie N is a superset of M)
# - bind(M, s) has only bound variables
# - bind(M, s) == d returns true
#
# for example, given id[x], eqangle(_A, _B, C, D, _E, F, G, _H), eqangle(H, G, C, D, F, A, B, E)
# then a possible map is
# {_A: B, _B: E, _E: D, _H: H}
#
# contri(_A, _B, C, _D, _E, C) -> contri(A, B, C, A, D, C)
# {_A, _B, C}, {B, _E, C}, {B, _G, C} -> {A, B, C, A, D, C, B, D, C}
# maybe O(m!) where m is the number of different set, m is smallish so this should work
#
# ideas: O(n!) worst case solution - try every possible map
# wait that may not even work
# problem: map is not injective.. trying every possible map is then O(n^n)...
#
#
# given id[x], perp(_A, _B, Y, _C), perp(X, Y, X, Z)
# {_A: X, _B: Z, _C: X} is a possible solution
def update_map(cur_map: Dict[Point, Point], source: Hypothesis, destination: Hypothesis) -> List[Dict[Point, Point]]:
    assert source == destination, "Cannot map different hypotheses to each other"
    a = source.ent_list()
    b = destination.ent_list()
    ca = 0
    for x in a:
        for y in x:
            if y.unbound():
                ca += 1
    if not ca:
        return [cur_map]
    for x in b:
        for y in x:
            assert y.bound(), "Destination may only contain bound points"
    return source.update_map(cur_map, source, destination)


# O((n permute k)^m) where k is size of each bucket, m is how many buckets there are, n is number of points in dest
# = O(n^{km})
# default, and slowest option, but guaranteed to work no matter what source.equal is
def update_map_sep(cur_map: Dict[Point, Point], source: Hypothesis, destination: Hypothesis) -> List[Dict[Point, Point]]:
    # ok = False
    # for key, val in cur_map.items(): # check if there is still anything left to change
    #     if key.name == val.name:
    #         ok = True
    # if not ok:
    #     return [cur_map]
    potential_mappings = set()
    for points_bin in destination.ent_list():
        potential_mappings |= points_bin
    required_mappings = []
    choices = []
    maps = []
    for points_bin in source.ent_list():
        unbound_points = {x for x in points_bin if x.unbound()}
        if not unbound_points:
            continue
        required_mappings += [unbound_points]
        choices += [permutations(potential_mappings, len(unbound_points)),]
    for choice in product(*choices):
        temp_map = dict()
        for r, c in zip(required_mappings, choice):
            for x, y in zip(r, c):
                temp_map[x] = y
        merge_map = merge(temp_map, cur_map)
        # print(" ".join(": ".join((m.name,n.name)) for m, n in merge_map.items()))
        temp_source = source.bind(merge_map)
        if temp_source != destination:
            continue
        maps += [merge_map]
    if not maps and get_debug() == 1:
        print("Failed to create mapping", cur_map, source, destination, sep="\n")
    return [dict(t) for t in {tuple(d.items()) for d in maps}]


# only works if source.equal is default
def update_map_bijection(cur_map: Dict[Point, Point], source: Hypothesis,
                         destination: Hypothesis) -> List[Dict[Point, Point]]:
    a = source.ent_list()
    b = destination.ent_list()
    maps = []
    for choice in product(*[get_map_sets(x, y) for x, y in zip(a, b)]):
        try:
            m = reduce(merge, [cur_map]+list(choice), {})
            maps += [m]
        except ValueError:
            pass
    if not maps:
        print("Failed to create mapping ", cur_map, source, destination, sep="\n")
    return maps


def equal_pair(first, second) -> bool:
    """
    returns true iff the entities in first and second are same up to permutation
    :param first:
    :param second:
    :return:
    """
    if first.prefix != second.prefix or first.value != second.value:
        return False
    e1 = first.entities.entities
    e2 = second.entities.entities
    if match_sets(e1[0], e2[0]) and match_sets(e1[1], e2[1]):
        return True
    if match_sets(e1[0], e2[1]) and match_sets(e1[1], e2[0]):
        return True
    return False


def collinear(A: Point, B: Point, C: Point) -> Hypothesis:
    return Hypothesis(create_entities(({A, B, C},)), prefix="coll", valid=check([3]))


def parallel(A: Point, B: Point, C: Point, D: Point) -> Hypothesis:
    return Hypothesis(create_entities(({A, B}, {C, D})), prefix="para", equal=equal_pair, valid=check([2, 2]))


def perpendicular(A: Point, B: Point, C: Point, D: Point) -> Hypothesis:
    return Hypothesis(create_entities(({A, B}, {C, D})), prefix="perp", equal=equal_pair, valid=check([2, 2]))


def midpoint(M: Point, A: Point, B: Point):
    return Hypothesis(create_entities(({M}, {A, B})), prefix="midp", valid=check([1, 2], [(0, 1)]))


def circle(O: Point, A: Point, B: Point, C: Point):
    return Hypothesis(create_entities(({O}, {A, B, C})), prefix="circle", valid=check([1, 3], [(0, 1)]))


def congruent(A: Point, B: Point, C: Point, D: Point): # AB cong CD
    return Hypothesis(create_entities(({A, B}, {C, D})), prefix="cong", equal=equal_pair, valid=check([2, 2]))


def concyclic(A: Point, B: Point, C: Point, D: Point):
    return Hypothesis(create_entities(({A, B, C, D},)), prefix="cyclic", valid=check([4]))


    # 0/1 = 2/3
    # 0/2 = 1/3
    # 1/0 = 3/2
    # 1/3 = 0/2
    # 2/3 = 0/1
    # 2/0 = 3/1
    # 3/2 = 1/0
    # 3/1 = 2/0
    #
    # AB/CD = EF/GH
    # AB/EF = CD/GH
    # CD/AB = GH/EF
    # CD/GH = AB/EF
    # EF/GH = AB/CD
    # EF/AB = GH/CD
    # GH/EF = CD/AB
    # GH/CD = EF/AB
eight_idx_array = [(0, 1, 2, 3),
                   (0, 2, 1, 3),
                   (1, 0, 3, 2),
                   (1, 3, 0, 2),
                   (2, 3, 0, 1),
                   (2, 0, 3, 1),
                   (3, 2, 1, 0),
                   (3, 1, 2, 0)]


def equal_eight(first, second) -> bool:
    if first.prefix != second.prefix or first.value != second.value:
        return False
    a = first.ent_list()
    b = second.ent_list()
    for idx in eight_idx_array:
        if (match_sets(a[0], b[idx[0]]) and match_sets(a[1], b[idx[1]]) and
                match_sets(a[2], b[idx[2]]) and match_sets(a[3], b[idx[3]])):
            return True
    return False


def update_map_eight(cur_map: Dict[Point, Point], source: Hypothesis,
                     destination: Hypothesis) -> List[Dict[Point, Point]]:
    a = source.ent_list()
    b = destination.ent_list()
    maps = []

    def update_perm_eight(i0, i1, i2, i3):
        tmp = []
        if match_sets(a[0], b[i0]) and match_sets(a[1], b[i1]) and match_sets(a[2], b[i2]) and match_sets(a[3], b[i3]):
            m0s = get_map_sets(a[0], b[i0])
            m1s = get_map_sets(a[1], b[i1])
            m2s = get_map_sets(a[2], b[i2])
            m3s = get_map_sets(a[3], b[i3])
            for m0, m1, m2, m3 in product(m0s, m1s, m2s, m3s):
                try:
                    m = reduce(merge, [cur_map, m0, m1, m2, m3], {})
                    tmp += [m]
                except ValueError:
                    pass
        return tmp

    for idx in eight_idx_array:
        maps += update_perm_eight(idx[0], idx[1], idx[2], idx[3])

    return maps


def get_map_sets(set1: Set[Point], set2: Set[Point]) -> List[Dict[Point, Point]]:
    required_mappings = [p for p in set1 if p.unbound()]
    potential_mappings = [p for p in set2 if p not in set1]
    maps = []
    assert len(required_mappings) <= len(potential_mappings)
    for choice in permutations(potential_mappings, len(required_mappings)):
        cur_map = {k: v for k, v in zip(required_mappings, choice)}
        post_map = [p.bind(cur_map) for p in set1]
        if match_sets(post_map, set2):
            maps += [cur_map]
    return maps


def equal_angles(A: Point, B: Point, C: Point, D: Point, E: Point, F: Point, G: Point, H: Point):
    return Hypothesis(create_entities(({A, B}, {C, D}, {E, F}, {G, H})),
                      prefix="eqangle",
                      equal=equal_eight,
                      valid=check([2, 2, 2, 2]),
                      update_map=update_map_eight)


def equal_ratios(A: Point, B: Point, C: Point, D: Point, E: Point, F: Point, G: Point, H: Point):
    return Hypothesis(create_entities(({A, B}, {C, D}, {E, F}, {G, H})),
                      prefix="eqratio",
                      equal=equal_eight,
                      valid=check([2, 2, 2, 2]),
                      update_map=update_map_eight)


def similar_triangles(A: Point, B: Point, C: Point, D: Point, E: Point, F: Point):
    return Hypothesis(create_entities(({A, B, C}, {D, E, F})), prefix="simtri", equal=equal_pair, valid=check([3, 3]))


def congruent_triangles(A: Point, B: Point, C: Point, D: Point, E: Point, F: Point):
    return Hypothesis(create_entities(({A, B, C}, {D, E, F})), prefix="contri", equal=equal_pair, valid=check([3, 3]))
