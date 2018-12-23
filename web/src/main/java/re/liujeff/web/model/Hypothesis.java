package re.liujeff.web.model;

import lombok.Value;

import java.util.List;

@Value
public class Hypothesis implements Pythonable {

    private final Long id;

    private final String prefix;
    private final List<String> entities;
    private final String value;

    @Override
    public String toPython(String delim) {
        return prefix+delim+String.join(delim, entities)+delim+value;
    }

}
