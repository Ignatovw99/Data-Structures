import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class RoyaleArena implements IArena {

    private Map<Integer, Battlecard> cardsById;

    private Map<CardType, Set<Battlecard>> cardsByTypes;


    public RoyaleArena() {
        cardsById = new LinkedHashMap<>();
        cardsByTypes = new LinkedHashMap<>();
    }

    private void addToCardsByType(Battlecard card) {
        cardsByTypes.putIfAbsent(card.getType(), new TreeSet<>(Battlecard::compareTo));
        cardsByTypes.get(card.getType()).add(card);
    }

    private Set<Battlecard> throwOrReturnCollection(Set<Battlecard> battlecardsCollection) {
        if (battlecardsCollection == null || battlecardsCollection.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        return battlecardsCollection;
    }

    private Set<Battlecard> getAllCardsByType(CardType type) {
        Set<Battlecard> battlecards = cardsByTypes.get(type);
        return throwOrReturnCollection(battlecards);
    }

    private boolean isInDamageRange(Battlecard battlecard, int lo, int hi) {
        return battlecard.getDamage() > lo && battlecard.getDamage() < hi;
    }

    @Override
    public void add(Battlecard card) {
        if (contains(card)) {
            return;
        }
        cardsById.put(card.getId(), card);
        addToCardsByType(card);
    }

    @Override
    public boolean contains(Battlecard card) {
        return cardsById.containsKey(card.getId());
    }

    @Override
    public int count() {
        return cardsById.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        Battlecard battlecard = cardsById.get(id);
        if (battlecard == null) {
            throw new IllegalArgumentException();
        }
        CardType oldType = battlecard.getType();
        battlecard.setType(type);
        cardsByTypes.get(oldType).remove(battlecard);
        addToCardsByType(battlecard);
    }

    @Override
    public Battlecard getById(int id) {
        Battlecard battlecard = cardsById.get(id);
        if (battlecard == null) {
            throw new UnsupportedOperationException();
        }
        return battlecard;
    }

    @Override
    public void removeById(int id) {
        Battlecard battlecard = getById(id);
        cardsById.remove(id, battlecard);
        cardsByTypes.get(battlecard.getType()).remove(battlecard);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        return getAllCardsByType(type);
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        Set<Battlecard> cardsByTypeInRange = getAllCardsByType(type);
        cardsByTypeInRange = cardsByTypeInRange.stream()
                .filter(battlecard -> isInDamageRange(battlecard, lo, hi))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return throwOrReturnCollection(cardsByTypeInRange);
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        Set<Battlecard> cardsByGivenTypeWithLessThanGivenDamage = getAllCardsByType(type);
        cardsByGivenTypeWithLessThanGivenDamage = cardsByGivenTypeWithLessThanGivenDamage.stream()
                .filter(battlecard -> battlecard.getDamage() <= damage)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return throwOrReturnCollection(cardsByGivenTypeWithLessThanGivenDamage);
    }

    private Set<Battlecard> getByPredicateOrderedBySwag(Predicate<Battlecard> predicate) {
        Set<Battlecard> cardsWithGivenName = new TreeSet<>(new Battlecard.SwagComparator());

        for (Battlecard battlecard : cardsById.values()) {
            if (predicate.test(battlecard)) {
                cardsWithGivenName.add(battlecard);
            }
        }

        return throwOrReturnCollection(cardsWithGivenName);
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        return getByPredicateOrderedBySwag(battlecard -> battlecard.getName().equals(name));
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        return getByPredicateOrderedBySwag(battlecard -> battlecard.getName().equals(name) && battlecard.getSwag() >= lo && battlecard.getSwag() < hi);
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {
        Map<String, Battlecard> battlecardsByName = new LinkedHashMap<>();
        for (Battlecard battlecard : cardsById.values()) {
            if (!battlecardsByName.containsKey(battlecard.getName())) {
                battlecardsByName.put(battlecard.getName(), battlecard);
            } else {
                double oldSlag = battlecardsByName.get(battlecard.getName()).getSwag();
                double newSlag = battlecard.getSwag();
                if (newSlag > oldSlag) {
                    battlecardsByName.put(battlecard.getName(), battlecard);
                }
            }
        }

        return battlecardsByName.values();
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n <0 || n > count()) {
            throw new UnsupportedOperationException();
        }
        return cardsById.values()
                .stream()
                .sorted(Comparator.comparingDouble(Battlecard::getSwag).thenComparing(Battlecard::getId))
                .limit(n)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {
        Set<Battlecard> battlecards = new TreeSet<>(Comparator.comparingDouble(Battlecard::getSwag));
        for (Battlecard battlecard : cardsById.values()) {
            if (battlecard.getSwag() >= lo && battlecard.getSwag() <= hi) {
                battlecards.add(battlecard);
            }
        }
        return battlecards;
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return cardsById.values().iterator();
    }
}
