from shared_logic.Hypothesis import cong


def is_not_empty(knowledge, hypothesis) -> bool:
    a = hypothesis.ent_list()
    if hypothesis.prefix == "contri":
        # contri(O, A, B, O, B, A) if cong(O, A, O, B)
        for i in range(3):
            fi = i
            se = (1+i)%3
            th = (2+i)%3
            if (a[fi] == a[3 + fi] and a[se] == a[3 + th] and
                    a[th] == a[3 + se] and knowledge.contains(cong(a[fi], a[se], a[fi], a[th]))):
                return False
    # if hypothesis.prefix == "eqangle":
    #     eqangle(B, A, A, C, D, E, F, G) if coll(A, B, C)
        # first_pair = a[0] | a[1]
        # second_pair = a[2] | a[3]
    return True