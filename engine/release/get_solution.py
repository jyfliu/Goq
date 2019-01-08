import os
import sys

abs_path = os.path.abspath(os.path.dirname(__file__))
sys.path.append(os.path.abspath(os.path.join(abs_path, '..')))
sys.path.append(os.path.abspath(os.path.join(abs_path, '..', 'logic')))

_DEBUG = -1
_FAST = False


from logic import Theorem, Universe
from shared_logic import Hypothesis, util

# parse input
# this can definitely be done a lot cleaner lol
# call this script like so
# python get_solution.py
#
# sys.argv[1]:
# <number of theorems>
# [theorems]
# <number of goals>
# [goals]
# <number of constraints>
# [constraints]
#
# theorems are formatted as follows (hypotheses are separated by '+' here)
# name#num_results#result1#...#result n#num_hypotheses#hypo1#...#hypo n#source
#
# hypotheses are formatted as follows
# prefix#point1#...#point_n#value (leave value string blank if no value exists, but do not forget the final #)

if len(sys.argv) > 1:
    with open(os.path.join(abs_path, sys.argv[1]), 'r') as f:
        data = f.readlines()
else:
    with open(os.path.join(abs_path, "temp.txt"), 'r') as f:
        data = f.readlines()

num_theorems = int(data[0])
theorems = [x.split("#") for x in data[1:1+num_theorems]]
num_goals = int(data[1+num_theorems])
goals = [x.split("#") for x in data[2+num_theorems:2+num_theorems+num_goals]]
num_constraints = int(data[2+num_theorems+num_goals])
constraints = [x.split("#") for x in data[3+num_theorems+num_goals:3+num_theorems+num_constraints+num_goals]]

parsed_theorems = [Theorem.parse_from_string(theorem) for theorem in theorems]
parsed_goals = [Hypothesis.parse_from_string(goal) for goal in goals]
parsed_constraints = [Hypothesis.parse_from_string(constraint) for constraint in constraints]


universe = Universe.Universe()
for theorem in parsed_theorems:
    universe.admit(theorem)
for goal in parsed_goals:
    universe.claim(goal)
for constraint in parsed_constraints:
    universe.pose(constraint)

universe.assert_points_unique()

old_debug = util._debug
util._debug = _DEBUG
universe.run(fast=_FAST, print_time=True)

with open(os.path.join(abs_path, "proof.txt"), "w") as sys.stdout:
    for goal in parsed_goals:
        universe.knowledge.print_stack_trace(goal)

util._debug = old_debug # not really necessary oh well

# without get_map cache: 157.38 s
# with get_map cache: 114.20 s

# below numbers are rough and not in proper testing configs (ie. on battery saver power mode
# data based 1st draft: 308s with debug = 1
# same thing as above but with fast = True: 34.15s

# Proper test benches: plugged in, max performance, data based: 291.41s
# Same test conditions, preposition based: 114.32 (so its very damn close to before)
# both found the same proof