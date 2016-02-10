package birthreminder.domain;

import java.time.LocalDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Person.
 */

@Document(collection = "person")
public class Person implements Serializable {

    @Id
    private String id;

    @NotNull
    @Field("name")
    private String name;

    @NotNull
    @Field("last_name")
    private String lastName;

    @Field("birth_day")
    private LocalDate birthDay;

    @Field("info")
    private String info;

    @Field("remind")
    private Boolean remind;

    @Field("remind_before")
    private Integer remindBefore;

    @Field("remind_after")
    private Integer remindAfter;

    @DBRef
    @Field("owner")
    private User owner;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(LocalDate birthDay) {
        this.birthDay = birthDay;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Boolean getRemind() {
        return remind;
    }

    public void setRemind(Boolean remind) {
        this.remind = remind;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Person person = (Person) o;
        if (!person.getOwner().equals(owner)) {
            return false;
        }
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", lastName='" + lastName + "'" +
            ", birthDay='" + birthDay + "'" +
            ", info='" + info + "'" +
            ", remind='" + remind + "'" +
            '}';
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Integer getRemindBefore() {
        return remindBefore;
    }

    public void setRemindBefore(Integer remindBefore) {
        this.remindBefore = remindBefore;
    }

    public Integer getRemindAfter() {
        return remindAfter;
    }

    public void setRemindAfter(Integer remindAfter) {
        this.remindAfter = remindAfter;
    }
}
