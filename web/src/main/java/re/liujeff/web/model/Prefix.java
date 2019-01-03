package re.liujeff.web.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class Prefix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String prefix;
    private String display;

    public Prefix(String prefix) {
        this.prefix = prefix;
        this.display = prefix;
    }

    public Prefix(String prefix, String display) {
        this.prefix = prefix;
        this.display = display;
    }

}
