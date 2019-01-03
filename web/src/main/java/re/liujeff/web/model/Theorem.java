package re.liujeff.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Theorem implements Pythonable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Hypothesis> results;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Hypothesis> hypotheses;
    private String name;
    private String source;

    public Theorem(List<Hypothesis> results, List<Hypothesis> hypotheses, String name, String source) {
        this.results = results;
        this.hypotheses = hypotheses;
        this.name = name;
        this.source = source;
    }

    @Override
    public String toPython(String delim) {
        List<String> results_strings = results.stream()
                .map(x -> x.toPython("+"))
                .collect(Collectors.toList());
        List<String> hypotheses_strings = hypotheses.stream()
                .map(x -> x.toPython("+"))
                .collect(Collectors.toList());
        return name+delim
                +results.size()+delim
                +String.join(delim, results_strings)+delim
                +hypotheses.size()+delim
                +String.join(delim, hypotheses_strings)+delim
                +source+delim;
    }

}
