# Goq

Goq is an automatic theorem prover capable of solving complex Olympiad style geometry problems. The solver itself was written in Python, while the interface/website was written in Java with the help of Spring Boot, Thymeleaf, HTML, etc. The theorem database uses Hibernate, MySQL, JPA.

## See it in action

A pretty front-end UI has not been implemented yet. In the meantime, if you load Goq as a Spring project (I'm using IntelliJ, I'm not sure how this would work with other IDEs), then I have a few demos loaded up in web/src/test/java/re/liujeff/web/GetSolutionTest.java. You can easily add your own in the same format.

To completely avoid the front end and view the logic, the solver begins at engine/release/get_solution.py. You can edit the \_DEBUG flag to control how much output is printed (-1 = silence, 0 = quiet updates, 1 = debug mode, 2 = verbose debug). You can also change the \_FAST flag to enable some experimental speed increases. Fair warning: The current implementation of \_FAST makes the program nondeterministic, however, I've found that it still is able to reach the correct fixpoint most (nearly always) of the time. The performance increase is in the 10x order of magnitude. 

Once I finish an acceptable front-end I'll host the engine on my website.

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

To be written.

Currently the high level algorithm looks something like this:

Step 1: Pose an initial list of hypotheses into the hypothesis database.

Step 2: Apply theorems until we reach a fixpoint (where no matter which theorem we attempt to apply, no more progress may be made).

Step 3: Construct a template ("auxilary point") from the template database, eg. connect two lines, etc.

Step 4: Repeat steps 2, 3 until the conclusion is reached.

(In the future, use Wu's method and computational methods when this fails)

(The current bottleneck is a poor choice of auxilary point in step 3)

Let's run through some example problems to see how this works in practice.

The Orthocenter Theorem: The three altitudes of a triangle are concurrent.

Well, of course, the computer won't be able to understand this. We can rephrase it in the following manner.

```
Hypothesis:
triangle(A, B, C)
collinear(E, A, C)
perpendicular(B, E, A, C)
collinear(F, B, C)
perpendicular(A, F, B, C)
collinear(H, A, F)
collinear(H, B, E)
collinear(G, A, B)
collinear(G, C, H)

Goals:
perp(C, G, A, B)
```
![](https://github.com/jyfliu/Goq/blob/master/resources/img1.PNG)

The machine generated the following proof (the transcription of the proof is still a heavy work in progress):

```
Since FH is perpendicular to FC and EH is perpendicular to EC, CFEH forms a cyclic quadrilateral.
Since FB is perpendicular to FA and EB is perpendicular to EA, AFBE forms a cyclic quadrilateral.
Since CFEH is cyclic and HF-HC and EF-EC intercept the same arc, angle HF-HC is equal to angle EF-EC.
Since AFBE is cyclic and BF-BA and EF-EA intercept the same arc, angle BF-BA is equal to angle EF-EA.
Since AHF is collinear and AEC is collinear and angle HF-HC is equal to angle EF-EC, angle AF-CH is equal to angle FE-AC.
Since CBF is collinear and CEA is collinear and angle BF-BA is equal to angle EF-EA, angle BC-AB is equal to angle FE-AC.
Since angle AF-CH is equal to angle FE-AC angle and angle BC-AB is equal to angle FE-AC and equality of angles is transitive, angle AF-CH is equal to angle BC-AB.
Since BC is perpendicular to AF and GCH is collinear and angle BC-AF is equal to angle AB-CH, CG is perpendicular to AB.
This completes the proof.
```

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

## Sources

Chou, S., & Gao, X. (2001). Automated Reasoning in Geometry. _Handbook of Automated Reasoning_, 707-749. doi:10.1016/b978-044450813-3/50013-8
