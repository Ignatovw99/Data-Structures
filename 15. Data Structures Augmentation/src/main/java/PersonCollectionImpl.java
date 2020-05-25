import java.util.*;

public class PersonCollectionImpl implements PersonCollection {

    private PersonCollection delegate;

    private Map<String, Person> peopleByEmails;

    private Map<String, TreeSet<Person>> peopleByDomains;

    private Map<String, TreeSet<Person>> peopleByNamesAndTowns;

    private NavigableMap<Integer, TreeSet<Person>> peopleByAge;

    public PersonCollectionImpl() {
        delegate = new PersonCollectionSlowImpl();
        peopleByEmails = new HashMap<>();
        peopleByDomains = new HashMap<>();
        peopleByNamesAndTowns = new HashMap<>();
        peopleByAge = new TreeMap<>();
    }

    @Override
    public boolean add(String email, String name, int age, String town) {
        if (peopleByEmails.containsKey(email)) {
            return false;
        }
        Person person = new Person(email, name, age, town);
        peopleByEmails.put(email, person);

        String domain = getDomain(email);
        peopleByDomains.putIfAbsent(domain, new TreeSet<>(Person::compareTo));
        peopleByDomains.get(domain).add(person);

        String nameAndTown = name + town;
        peopleByNamesAndTowns.putIfAbsent(nameAndTown, new TreeSet<>(Person::compareTo));
        peopleByNamesAndTowns.get(nameAndTown).add(person);

        peopleByAge.putIfAbsent(age, new TreeSet<>());
        peopleByAge.get(age).add(person);

        return true;
    }

    private String getDomain(String email) {
        return email.substring(email.indexOf("@") + 1);
    }

    @Override
    public int getCount() {
        return peopleByEmails.size();
    }

    @Override
    public boolean delete(String email) {
        Person personToDelete = peopleByEmails.remove(email);
        if (personToDelete == null) {
            return false;
        }
        peopleByDomains.get(getDomain(email)).remove(personToDelete);

        String personNameAndTown = personToDelete.getName() + personToDelete.getTown();
        peopleByNamesAndTowns.get(personNameAndTown).remove(personToDelete);

        peopleByAge.get(personToDelete.getAge()).remove(personToDelete);

        return true;
    }

    @Override
    public Person find(String email) {
        return peopleByEmails.get(email);
    }

    @Override
    public Iterable<Person> findAll(String emailDomain) {
        TreeSet<Person> peopleByDomain = peopleByDomains.get(emailDomain);
        return peopleByDomain != null ? peopleByDomain : new TreeSet<>();
    }

    @Override
    public Iterable<Person> findAll(String name, String town) {
        TreeSet<Person> peopleByNameAndTown = peopleByNamesAndTowns.get(name + town);
        return peopleByNameAndTown != null ? peopleByNameAndTown : new TreeSet<>();
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge) {
        NavigableMap<Integer, TreeSet<Person>> peopleInGivenAgeRangeMap = peopleByAge.subMap(startAge, true, endAge, true);
        List<Person> peopleInGivenAgeRange = new ArrayList<>();
        if (peopleInGivenAgeRangeMap == null) {
            return peopleInGivenAgeRange;
        }
        peopleInGivenAgeRangeMap.values()
                .forEach(peopleInGivenAgeRange::addAll);
        return peopleInGivenAgeRange;
    }

    @Override
    public Iterable<Person> findAll(int startAge, int endAge, String town) {
        NavigableMap<Integer, TreeSet<Person>> peopleInGivenAgeRangeMap = peopleByAge.subMap(startAge, true, endAge, true);
        List<Person> peopleInGivenAgeRange = new ArrayList<>();
        if (peopleInGivenAgeRangeMap == null) {
            return peopleInGivenAgeRange;
        }
        peopleInGivenAgeRangeMap.values()
                .forEach(peopleInCurrentAge -> {
                    for (Person person : peopleInCurrentAge) {
                        if (person.getTown().equals(town)) {
                            peopleInGivenAgeRange.add(person);
                        }
                    }
                });
        return peopleInGivenAgeRange;
    }
}
