package re.liujeff.web.repositories;

import org.springframework.data.repository.CrudRepository;
import re.liujeff.web.model.Hypothesis;

public interface HypothesisRepository extends CrudRepository<Hypothesis, Long> {
}
