# Goq

DEMO COMING SOON (sorry!)

Goq is an automatic theorem prover capable of solving complex Olympiad style geometry problems. The solver itself was written in Python, while the interface/website was written in Java with the help of Spring Boot, Thymeleaf, HTML, etc. The theorem database uses Hibernate, MySQL, JPA.

## Inspiration

Olympiad geometry is a very polarizing math discipline -- high school students either love it or hate it. For some, the diagrams lead to easier visualization and thus facilitate abstract thinking right away. For others, myself included, geometry problems either require an exceptional amount of preparation work when done with a synthetic approach, and more analytical "bash" techniques are time consuming, low level, and frankly quite ugly.

Enter the computer: an emotionless machine who excels at ugly and time consuming bashes, and who's databases can store (for our purposes) near limitless theorems and their applications.

Geometry proofs are very mechanical in nature -- they don't require much creativity. The vast majority of geometry problems, even at the Olympiad level, can be solved in the following manner: Construct these templates, apply these 7 theorems in this order, these points lie in the following configuration of which we know this property about, etc. Both humans and computers are not restricted in the techniques they know, but rather by the number of templates and theorems they have memorized. And computers are a lot better at memorization than humans.

TODO: IMO 2018 P6 + solution + show how computer could solve it

In comparison, the other disciplines are much more ad hoc in nature, which makes their problems much harder to solve with computerized technology. Let's look at problem 3 (combinatorics) of the same year.

TODO: IMO 2018 P3

The solutions to problems such as this one require several observations that are difficult to formalize machine-assisted technology, and even harder to generate automatically.

There are some hurdles, however. Rigourously formalizing a geometry problem into a computer-legible form is a difficult challenge, and is made more difficult with the introduction of symbolic math. Template matching and other problems requiring modern solutions (AI, etc) are also abundant. These challenges are what keeps a project such as this one fun.

## How it works.

To be written. Something about bidirectional dfs over a very large decision graph (slowly working towards a much more intelligent algorithm. Expect a paper to be written about this (?) !)

## How good is it?

Well, it's definitely better than me.

In theory, any problem solvable with synthetic techniques is solvable by Goq. However, we are limited by our knowledge base of theorems and constructions. Unlike humans, Goq cannot intuitively guess which construction should be optimal, and instead needs to try them all.

Yet.

There are optimization techniques available for us. Chess engines have been using many of these techniques for decades (alpha-beta pruning, razoring), with newer and fancier techniques (deep learning) continuously being developed.

I'd only ever consider this project done if Goq consistently outperforms the top humans. And we are far from that.

## Future

To be written. However, in my opinion, of the four big topics in Olympiad math, geometry is by far the easiest to solve automatically. The difficulty order probably looks something like: Geometry < Number Theory =? Algebra < Combinatorics.

Symbolic math is also getting to the point where I'd consider it good enough to use in a proof software such as this one. This means that analytic techniques such as barycentric, complex, or (god forbid) coordinate geometry are directions for further work.

## FAQ:
#### Why is it called Goq?
Goq is a portmanteau between geometry and coq. Coq is a computerized proof assistant with a functional programming language and formal logic. Now Coq, unlike Goq, is not an automatic theorem prover. Coq is a proof *assistant*. Still, I chose the name because it kind of sounds funny.

#### Why did you use Java/Spring instead of Django/some thing Python?
I wanted to learn Spring.

#### Why did you use Python instead of pure Java?
I wanted to practice Python. Also eventually I'd like to integrate more modern AI techniques, which is most readily done in Python.
