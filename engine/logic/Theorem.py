from functools import reduce
from typing import List, Dict, Tuple, Any, Set

from itertools import product

from shared_logic import Hypothesis, Point
from shared_logic.Knowledge import left_implies
from shared_logic.util import merge


class Theorem(object):

    def __init__(self, points: int, results: List, hypotheses: List, *,
                 name: str="<?T?>", source: str="God"):
        self.points = points
        self.results = results
        self.hypotheses = hypotheses
        self.name = name
        self.source = source

    def try_apply(self, points: Dict, results: List,
                  hypotheses: List, sources: List,
                  universe) -> List[Tuple["Hypothesis", Tuple["Hypothesis"]]]:
       # results = [x for x in bind(points, results) if x.valid(x)]
        for x in bind(points, results):
            if not x.valid(x):
                # print(bind(points, results))
                return []
        if not hypotheses:
            if len([v for k, v in points.items() if v.unbound()]):
                all_points = all_maps(points, universe.points)
                ans = []
                for cur_points in all_points:
                    ans += list({(x, (" ".join((self.name, left_implies(), self.source)),)
                                  + tuple(bind(cur_points, sources))) for x in bind(cur_points, results) if x.valid(x)})
                return ans
            return list({(x, (" ".join((self.name, left_implies(),self.source)),)
                          + tuple(bind(points, sources))) for x in bind(points, results)})
        hypotheses = bind(points, hypotheses)
        hypo = hypotheses[0]
        if not hypo.valid(hypo):
            return []
        ret = []
       # print("\n".join(str(x) for x in universe.knowledge.get_all_of(hypo)))
        #print(len(universe.knowledge.get_all_of(hypo)), len(hypotheses), datetime.datetime.now()) # TODO FOR SOME REASON ITS TOO SLOW
        for u in universe.knowledge.get_all_of(hypo):
            if match(hypo, u):
                for cur_map in Hypothesis.update_map(points, hypo, u):
                    ret += self.try_apply(cur_map, results, hypotheses[1:], sources+[hypo], universe)
        return list(set(ret))

    def apply(self, universe) -> List[Tuple["Hypothesis", Tuple["Hypothesis"]]]:
        return self.try_apply(identity_point_map(self.points), self.results, self.hypotheses, [], universe)

    def __str__(self) -> str:
        return "".join((self.name, ": ", str(self.results), " <-| ", str(self.hypotheses)))

    def __repr__(self) -> str:
        return self.__str__()

    def __eq__(self, other):
        return str(self) == str(other)

    def __hash__(self):
        return hash(self.__str__())


def parse_from_string(theorem: List[str]) -> Theorem:
    name = theorem[0]
    num_results = int(theorem[1])
    results = theorem[2:2+num_results]
    num_hypotheses = int(theorem[2+num_results])
    hypotheses = theorem[3+num_results:3+num_results+num_hypotheses]
    source = theorem[3+num_hypotheses+num_results]

    parsed_results = [Hypothesis.parse_from_string(result.split("+")) for result in results]
    parsed_hypotheses = [Hypothesis.parse_from_string(hypothesis.split("+")) for hypothesis in hypotheses]

    points = reduce(lambda x, y: x|y, [set(r.all_entities()) for r in parsed_results + parsed_hypotheses], set())

    return Theorem(len(points), parsed_results, parsed_hypotheses, name=name, source=source)


def bind(points: Dict, results: List["Hypothesis"]) -> List["Hypothesis"]:
    return [res.bind(points) for res in results]


def match(first, second) -> bool:
    return first == second


def identity_point_map(size: int) -> Dict:
    points = {} # initially points is the identity map
    for i in range(size):
        points[Point.unbound_point(i)] = Point.unbound_point(i)
    return points


def all_maps(base_map: Dict, points: Set[Any]):
    maps = []
    all_unbound = [v for k, v in base_map.items() if v.unbound()]
    for prod in product(list(points), repeat=len(all_unbound)):
        new_map = dict(zip(all_unbound, prod))
        maps.append(merge(base_map, new_map))
    return maps


#
# example: Thale's Theorem
#
# suppose we have the following theorem (Thale's)
#
# P = points: [_A, _B, _C, _D, _E]
# R = results: [equal_ratios(_E, _A, _A, _C, _E, _B, _B, _D)],
# H = hypotheses: [parallel(_A, _B, _C, _D), collinear(_E, _A, _C), collinear(_E, _B, _D)]
#
# which we wish to apply to the following universe
#
# U = universe
#
# we loop through the hypotheses, beginning in this case with para(_A, _B, _C, _D), and
# search for matches in U
#
# as points become determined, the points array changes
# say we find the hypothesis para(A, B, X, Y) in our knowledge base
#
# P: [_A, _B, _C, _D, _E]
#  -> P: [A, B, X, Y, _E]
#
# _A, _B, _C, _D get bound, _E remains free. We also substitute the other occurrences
# H: coll(_E, _A, _C) -> H: coll(_E, A, X)
# H: coll(_E, _B, _D) -> H: coll(_E, B, Y)
# R: eqratio(_E, _A, _A, _C, _E, _B, _B, _D) -> R: eqratio(_E, A, A, X, _E, B, B, Y)
#
# we now search for all hypotheses in our knowledge base matching our second required, in this
# case coll(_E, A, X)
#
# let's say we find coll(P, A, X). so once again we bind _E -> P
#
# finally we search for our final required, which is coll(P, A, Y). suppose we find it
# then we may return our results, which is eqratio(P, A, A, X, P, B, B, Y)
#
# we add these results to the knowledge base
#
# time complexity: O(N^K)
#     where:  N is the number of hypotheses in our knowledge base (~100ish?? <1000 at most)
#             K is the number of hypotheses required by our theorem (3-4 at most?)
#
# K is rather small, I think K<=4 is almost always satisfied by the theorems I chose
# N is at worst 1000ish, in real use cases this number can be reduced significantly, for example
# we can sort the knowledge base or implement a bbst for faster querying
#
# this time complexity can be dramatically improved to O((P^T*log(N))^K) if we can implement logarithmic
# searching in knowledge base, ie a function which satisfies the following constraints
#
#     where:  P is the number of points (4-8ish?)
#             T is the number of free points in our current hypotheses (4-8?)
#
# we are also further assured P^T<N
#
# def search(hypothesis, knowledgebase)
#     # returns a list of all theorems which match the bound variables of hypothesis
#     # O(M*log(N))
#     # (M is size of list we are returning)
#     # (M < P^T)
#     # (naive implementation is O(N))
#
# # pseudo code ish
# def bind(points_map, hypo):
#     returns a copy of hypo with all instances of points replaced
#     points_map is a map like   [_A: A,
#                                 _B: B,
#                                 _C: X,
#                                 _D: Y,
#                                 _E: P]
#
# def match(h, h1):
#     return h==h1 # override __eq__ in hypothesis
#
# def get_map(h, h1):
#     D = {}
#     for idx, p in enumerate(h.entities.entities):
#         if p.unbound()
#             D[p]=h1.entities.entities[idx]
#     return D
#
# def merge(m1, m2):
#     return m1 and m2 merged dictionaries
#
# def try_apply(P, R, H, U):
#     if H is empty:
#         return bind(P, R)
#     h = H[0]
#     results = []
#     for u in U.knowledge:
#         if match(h, u):
#             results += [try_apply(merge(P, get_map(h, u)), R, H[1:], U)]
#     return results
#
# def apply(self, universe):
#     return try_apply({}, self.results, self.hypotheses, universe)
