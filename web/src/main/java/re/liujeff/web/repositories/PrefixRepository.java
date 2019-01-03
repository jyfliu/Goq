package re.liujeff.web.repositories;

import org.springframework.data.repository.CrudRepository;
import re.liujeff.web.model.Prefix;

import java.util.Optional;

public interface PrefixRepository extends CrudRepository<Prefix, Long> {

    Optional<Prefix> findByDisplay(String display);
    Optional<Prefix> findByPrefix(String prefix);

}
