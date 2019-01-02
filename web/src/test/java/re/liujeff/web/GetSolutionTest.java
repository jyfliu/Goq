package re.liujeff.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Prefix;
import re.liujeff.web.model.Theorem;
import re.liujeff.web.repositories.PrefixRepository;
import re.liujeff.web.repositories.TheoremRepository;
import re.liujeff.web.services.ProblemSolvingService;
import re.liujeff.web.services.PythonProblemSolvingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith( SpringJUnit4ClassRunner.class )
@ContextConfiguration
@SpringBootTest
public class GetSolutionTest {

    @Test
    public void runTest0() {
        // got to do this the dumb way since the smarter methods have not been tested yet
        Theorem theorem0 = new Theorem(0L,
                Collections.singletonList(
                        new Hypothesis(100L, "midp", Arrays.asList("_E", "_A", "_C"), "")),
                Arrays.asList(
                        new Hypothesis(101L,"midp", Arrays.asList("_D", "_A", "_B"), ""),
                        new Hypothesis(102L, "para", Arrays.asList("_D", "_E", "_B", "_C"), ""),
                        new Hypothesis(103L, "coll", Arrays.asList("_E", "_A", "_C"), "")),
                "Theorem Tim", "Superman");
        Theorem theorem1 = new Theorem(1L,
                Collections.singletonList(new Hypothesis(104L,"para",
                        Arrays.asList("_A", "_B", "_C", "_D"), "")),
                Collections.singletonList(new Hypothesis(105L,"eqangle",
                        Arrays.asList("_A", "_B", "_E", "_F", "_C", "_D", "_E", "_F"), "")),
                "Theorem Tom", "Batman");

        List<Theorem> theorems = Arrays.asList(theorem0, theorem1);

        Hypothesis hypo0 = new Hypothesis(106L, "midp", Arrays.asList("A", "B", "C"), "");
        Hypothesis hypo1 = new Hypothesis(107L, "eqangle",
                Arrays.asList("B", "H", "G", "A", "H", "B", "C", "F"), "");
        Hypothesis hypo2 = new Hypothesis(108L, "coll", Arrays.asList("G", "F", "B"), "");
        Hypothesis hypo3 = new Hypothesis(109L, "midp", Arrays.asList("G", "F", "B"), "");

        ProblemSolvingService solver = new PythonProblemSolvingService();
        solver.initialize(theorems);
        System.out.println(solver.getSolution(Collections.singletonList(hypo3), Arrays.asList(hypo0, hypo1, hypo2)).getProof());
    }

    @Autowired
    private TheoremRepository tr;

    @Autowired
    private PrefixRepository pr;

    @Autowired
    private ProblemSolvingService solver;

    @Test
    public void addTheoremsTest() {
        Iterable<Theorem> it = tr.findAll();
        List<Theorem> list = new ArrayList<>();
        it.forEach(list::add);

        String A = "A";
        String B = "B";
        String C = "C";
        String D = "D";
        String E = "E";

        Prefix eqangle = pr.findByPrefix("eqangle").orElse(null);
        Prefix coll = pr.findByPrefix("coll").orElse(null);
        Prefix cong = pr.findByPrefix("cong").orElse(null);
        Prefix circle = pr.findByPrefix("circle").orElse(null);
        Prefix midp = pr.findByPrefix("midp").orElse(null);
        Prefix tri = pr.findByPrefix("tri").orElse(null);
        Hypothesis goal0 = new Hypothesis(eqangle, Arrays.asList(C, A, A, B, B, E, E, C));
        Hypothesis con0 = new Hypothesis(circle, Arrays.asList(D, A, B, C));
        Hypothesis con1 = new Hypothesis(midp, Arrays.asList(D, A, C));
        Hypothesis con2 = new Hypothesis(midp, Arrays.asList(B, A, E));
        Hypothesis ndg0 = new Hypothesis(tri, Arrays.asList(A, B, C));

        List<Hypothesis> goals = new ArrayList<>(Collections.singletonList(goal0));
        List<Hypothesis> constraints = new ArrayList<>(Arrays.asList(con0, con1, con2, ndg0));

        solver.initialize(list);
        System.out.println("*****HI*****");
        System.out.println(solver.getSolution(goals, constraints).getProof());
        System.out.println("*****BYE*****");
    }

}
