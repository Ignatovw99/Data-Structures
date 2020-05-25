import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PersonCollectionSlowImpl implements PersonCollection {

    private List<Person> people;

    public PersonCollectionSlowImpl() {
        people = new ArrayList<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (people.stream().anyMatch(person -> person.getEmail().equals(email))) {
            return false;
        }
        Person person = new Person(email, name, age, town);
        return people.add(person);
    }

    @Override
    public int getCount() {
        return people.size();
    }

    @Override
    public boolean delete(String email) {
        return people.removeIf(person -> person.getEmail().equals(email));
    }

    @Override
    public Person find(String email) {
        return people.stream()
                .filter(person -> person.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    private List<Person> getPeopleByPredicateSortedByComparator(Predicate<Person> predicate, Comparator<Person> comparator) {
        return people.stream()
                .filter(predicate)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        return getPeopleByPredicateSortedByComparator(person -> person.getEmail().endsWith("@" + emailDomain), Comparator.comparing(Person::getEmail));
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        return getPeopleByPredicateSortedByComparator(person -> person.getName().equals(name) && person.getTown().equals(town), Comparator.comparing(Person::getEmail));
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        return getPeopleByPredicateSortedByComparator(person -> person.getAge() >= startAge && person.getAge() <= endAge, Comparator.comparing(Person::getEmail));
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        return getPeopleByPredicateSortedByComparator(person -> person.getAge() >= startAge && person.getAge() <= endAge && person.getTown().equals(town), Comparator.comparing(Person::getEmail));
    }
}
