package re.liujeff.web.services;

import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Proof;
import re.liujeff.web.model.Theorem;

import java.util.List;

public interface ProblemSolvingService {

    void addTheorem(Theorem theorem);
    Proof getSolution(List<Hypothesis> constraints, List<Hypothesis> goals);

    default void initialize(List<Theorem> theorems) {
        theorems.forEach(this::addTheorem);
    }

}
