import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @Column(name = "ssn")
    private String ssn;

    @Column(name = "fullname")
    private String fullname;

    @Column(name = "phonenumber")
    private String phonenumber;

    @Column(name = "businessregistrationnumber")
    private String businessregistrationnumber;

    // Getters and setters
}
