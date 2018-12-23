package re.liujeff.web;

import org.junit.Test;
import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Theorem;
import re.liujeff.web.services.ProblemSolvingService;
import re.liujeff.web.services.PythonProblemSolvingService;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

}
