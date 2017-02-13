package CustPack.entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The customer entity, including constrains on the class' memebers
 */

public class Customer {
    @Id
    private String id;


    @NotNull
    @Pattern(regexp = "[A-Za-z]+[\\sA-Za-z]*", message = "name requires only letters and spaces")
    private String name;

    @Pattern(regexp = "[A-Za-z1-9]+@[A-Za-z1-9]+(\\.[A-Za-z1-9]+)+", message = "email pattern is X@(with any domain)")
    @NotNull
    private String email;

    @Pattern(regexp = "[A-Za-z]+[\\sA-Za-z]*", message = "city requires only letters and spaces")
    @NotNull
    private String city;

    @Pattern(regexp = "[A-Za-z]+[\\sA-Za-z]*", message = "street requires only letters and spaces")
    @NotNull
    private String street;

    @NotNull
    @Min(1)
    private Integer houseNum;

    @NotNull
    @NotEmpty
    private Set<String> tokens;


    public Customer(final String name, final String email, final String city, final String street, final Integer houseNum, final String[] tokens)
    {
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.houseNum = houseNum;
        this.tokens = Arrays.stream(tokens).collect(Collectors.toSet());
    }

    public Customer()
    {

    }

    public Set<String> getTokens() {
        return tokens;
    }

    public void setTokens(Set<String> tokens) {
        this.tokens = tokens;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id =  id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHouseNum() {
        return houseNum;
    }

    public void setHouseNum(Integer houseNum) {
        this.houseNum = houseNum;
    }

}
