package re.liujeff.web.services;

import org.springframework.stereotype.Service;
import re.liujeff.web.model.Hypothesis;
import re.liujeff.web.model.Proof;
import re.liujeff.web.model.Theorem;

import java.util.List;

@Service
public class JavaProblemSolvingService implements ProblemSolvingService {
    @Override
    public void addTheorem(Theorem theorem) {

    }

    @Override
    public Proof getSolution(List<Hypothesis> goals, List<Hypothesis> constraints) {
        return null;
    }
}
