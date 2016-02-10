package birthreminder.repository;

import birthreminder.domain.Person;

import birthreminder.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the Person entity.
 */
public interface PersonRepository extends MongoRepository<Person,String> {

    List<Person> findAllByOwner(User owner);

    Page<Person> findAllByOwner(User owner, Pageable pageable);

}
