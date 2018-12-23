import sys

import Hypothesis
import Theorem
import Universe

# parse input
# this can definitely be done a lot cleaner lol
# call this script like so
# python get_solution.py <number of theorems> [theorems]
#                        <number of goals> [goals]
#                        <number of constraints> [constraints]
#
# theorems are formatted as follows (hypotheses are separated by '+' here)
# name#num_results#result1#...#result n#num_hypotheses#hypo1#...#hypo n#source
#
# hypotheses are formatted as follows
# prefix#point1#...#point_n#value (leave value string blank if no value exists, but do not forget the final #)
num_theorems = int(sys.argv[1])
theorems = [x.split("#") for x in sys.argv[2:2+num_theorems]]
num_goals = int(sys.argv[2+num_theorems])
goals = [x.split("#") for x in sys.argv[3+num_theorems:3+num_theorems+num_goals]]
num_constraints = int(sys.argv[3+num_theorems+num_goals])
constraints = [x.split("#") for x in sys.argv[4+num_theorems+num_goals:4+num_theorems+num_constraints+num_goals]]

parsed_theorems = [Theorem.parse_from_string(theorem) for theorem in theorems]
parsed_goals = [Hypothesis.parse_from_string(goal) for goal in goals]
parsed_constraints = [Hypothesis.parse_from_string(constraint) for constraint in constraints]


universe = Universe.Universe()
for theorem in theorems:
    universe.admit(theorem)
for goal in goals:
    universe.claim(goal)
for constraint in constraints:
    universe.pose(constraint)


old_debug = Universe._debug
Universe._debug = -1
universe.run_til_heat_death()

for goal in parsed_goals:
    universe.knowledge.print_stack_trace(goal)

Universe._debug = old_debug # not really necessary oh well