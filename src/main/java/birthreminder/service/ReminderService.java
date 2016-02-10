package birthreminder.service;

import birthreminder.domain.Person;
import birthreminder.domain.User;
import birthreminder.repository.PersonRepository;
import birthreminder.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    private final Logger log = LoggerFactory.getLogger(ReminderService.class);

    @Inject
    private PersonRepository personRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MailService mailService;

    @Scheduled(cron = "0 0 5 * * ?")
    //@Scheduled(cron = "1 * * * * ?")
    public void notifyUsers() {
        LocalDate now = LocalDate.now();
        List<User> users = userRepository.findAll();
        for (User user : users) {
            List<Person> persons = personRepository.findAllByOwner(user);
            List<Person> personsForRemind = new ArrayList<>();
            for (Person person : persons) {
                LocalDate birthDay = person.getBirthDay();
                birthDay.withYear(now.getYear());
                int period = now.getDayOfYear() - birthDay.getDayOfYear();
                if (-person.getRemindBefore() < period && period < person.getRemindAfter()) {
                    personsForRemind.add(person);
                }
            }
            if (personsForRemind.size() > 0) {
                mailService.sendBirthdayReminder(user, personsForRemind);
                log.debug("Send notification for {}", user.getLogin());
            }
        }
    }
}
