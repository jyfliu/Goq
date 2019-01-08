
class Point(object):

    def __init__(self, name="<?P?>"):
        if name.startswith("_"):
            name = "<"+name+"_>"
        self.name = name

    def bind(self, points):
        if self.unbound():
            return points[self]
        else:
            return self

    def match(self, other) -> bool:
        if self.unbound():
            return True
        if other.unbound():
            return True
        return self.name == other.name

    def __str__(self) -> str:
        return self.name

    def __repr__(self) -> str:
        return self.name

    def __eq__(self, other):
        #    if self.unbound():
        #        return True
        #    if other.unbound():
        #        return True
        return self.name == other.name

    def __hash__(self):
        return hash(self.name)

    def unbound(self) -> bool:
        if self.name.startswith("<_") and self.name.endswith("_>"):
            return True
        return False

    def bound(self) -> bool:
        return not self.unbound()


def default_point(idx=None) -> Point:
    if idx is None:
        return Point("<DP>")
    else:
        return Point("<DP%d>" % idx)


def unbound_point(idx) -> Point:
    return Point(''.join(["<_", chr(ord('A')+idx), "_>"]))