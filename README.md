# Goq

DEMO COMING SOON (sorry!)

Goq is an automatic theorem prover capable of solving complex Olympiad style geometry problems. The solver itself was written in Python, while the interface/website was written in Java with the help of Spring Boot, Thymeleaf, HTML, etc. The theorem database was written in MySQL.

## Inspiration

Olympiad geometry is usually the most hated discipline by high school students. And for good reason, too. Synthetic geometry requires an exceptional amount of preparation work, and more analytical "bash" techniques are frowned upon due to their ugly and time consuming nature.

Enter the computer: an emotionless machine who exceps at ugly and time consuming bashes, and who's databases can store (for our purposes) near limitless theorems and their applications.

It may seem difficult formalizing geometry problems into a computer-legible interface, but after this hurdle it is actually quite simple for a computer to actually solve the problem. This is because geometry proofs are very mechanical in nature -- they don't require much creativity. The vast majority of geometry problems, even at the Olympiad level, can be solved in the following manner: Construct these templates, apply these 7 theorems in this order, these points lie in the following configuration of which we know this property about, etc.

TODO: IMO 2018 P6 + solution + show how computer could solve it

In comparison, the other disciplines are much more ad hoc in nature, which makes their problems much harder to solve with computerized technology. Let's look at problem 3 (combinatorics) of the same year.

This problem requires several observations that are difficult to formalize and make with machine-assisted technology...

## How it works.

To be written. Something about bidirectional dfs over a very large decision graph (slowly working towards a much more intelligent algorithm. Expect a paper to be written about this (?) !)

## How good is it?

To be written. It is very good (better than me for sure).

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
