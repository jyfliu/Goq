import Theorem
import Hypothesis
import Point
from Hypothesis import update_map

_A = Point.unbound_point(0)
_B = Point.unbound_point(1)
_C = Point.unbound_point(2)
_D = Point.unbound_point(3)
_E = Point.unbound_point(4)
_F = Point.unbound_point(5)
_G = Point.unbound_point(6)
_H = Point.unbound_point(7)
A = Point.Point("A")
B = Point.Point("B")
C = Point.Point("C")
D = Point.Point("D")
E = Point.Point("E")
F = Point.Point("F")
G = Point.Point("G")
H = Point.Point("H")


def test_update_map(test_id: int, verbose: bool=False):
    if verbose:
        print("TESTING update_map %d"%(test_id,))
    sam = Theorem.Theorem(8, [], [])
    identity = None
    source = None
    destination = None
    num_sols = -1
    if test_id == 0:
        identity = Theorem.identity_point_map(3)
        source = Hypothesis.midpoint(_A, _B, _C)
        destination = Hypothesis.midpoint(D, E, F)
        num_sols = 2
    elif test_id == 1:
        identity = Theorem.identity_point_map(8)
        source = Hypothesis.equal_angles(_A, _B, _C, _D, _E, _F, _G, _H)
        destination = Hypothesis.equal_angles(A, B, C, D, E, F, G, H)
        num_sols = 8*(2**4)
    elif test_id == 2:
        identity = Theorem.identity_point_map(6)
        source = Hypothesis.congruent_triangles(_A, B, C, _A, _E, _F)
        destination = Hypothesis.congruent_triangles(A, B, C, A, E, F)
        num_sols = 2
    else:
        return True
        # raise Exception("TEST %d NOT FOUND"%(test_id))
    maps = update_map(identity, source, destination)
    assert num_sols == len(maps) or num_sols < 0
    if verbose:
        print("Number of solutions passed: %d" % len(maps))
    for cur_map in maps:
        assert source.bind(cur_map) == destination
        if verbose:
            print(' '.join((':'.join((a.name, b.name)) for a, b in cur_map.items())), "passed", sep=" ")
    print("Test %d passed" % test_id)


def test_update_map_all(verbose=False):
    idx = 0
    while not test_update_map(idx, verbose):
        idx += 1
    print("All tests passed")

if __name__ == '__main__':
    test_update_map_all(False)