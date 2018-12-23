package re.liujeff.web.services;

import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Proof;
import re.liujeff.web.model.Theorem;

import java.util.List;

public class PythonProblemSolvingService implements ProblemSolvingService {

    private List<Theorem> theorems;

    public PythonProblemSolvingService() {

    }

    @Override
    public void addTheorem(Theorem theorem) {
        theorems.add(theorem);
    }

    @Override
    public Proof getSolution(List<Hypothesis> constraints, List<Hypothesis> goals) {
        StringBuilder sb = new StringBuilder();
        String fs = System.getProperty("file.separator");
        String path = "engine"+fs+"";
        sb.append("python ");
        return null;
    }

}
