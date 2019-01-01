package re.liujeff.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Hypothesis implements Pythonable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Prefix prefix;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "ENTITIES",
            joinColumns = @JoinColumn(name="HYPOTHESIS_ID")
    )
    @Fetch(value = FetchMode.SUBSELECT)
    @Column(name = "ENTITY_NAME")
    private List<String> entities;
    private String value;

    @Deprecated
    public Hypothesis(Long id, String prefix, List<String> entities, String value) {
        this(new Prefix(prefix), entities, value);
        this.id = id;
    }

    public Hypothesis(Prefix prefix, List<String> entities, String value) {
        if (prefix == null) {
            throw new NullPointerException();
        }
        this.prefix = prefix;
        this.entities = entities;
        this.value = value;
    }

    public Hypothesis(Prefix prefix, List<String> entities) {
        this(prefix, entities, "");
    }

    @Override
    public String toPython(String delim) {
        return prefix+delim+String.join(delim, entities)+delim+value;
    }

}
