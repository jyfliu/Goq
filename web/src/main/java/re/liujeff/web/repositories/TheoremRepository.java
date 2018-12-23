package re.liujeff.web.repositories;

import org.springframework.data.repository.CrudRepository;
import re.liujeff.web.model.Theorem;

public interface TheoremRepository extends CrudRepository<Theorem, Long> {
}
