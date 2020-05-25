package core;

import models.Hero;
import models.HeroType;

import java.util.*;

public class HeroDatabaseImpl implements HeroDatabase {

    private Set<Hero> heroes;

    private Map<String, Hero> heroesByNames;

    private Map<HeroType, Set<Hero>> heroesByTypes;

    private Map<Integer, Set<Hero>> heroesByLevels;

    private NavigableMap<Integer, Set<Hero>> heroesByPoints;

    public HeroDatabaseImpl() {
        heroes = new TreeSet<>(Comparator.comparingInt(Hero::getLevel).reversed().thenComparing(Hero::getName));
        heroesByNames = new HashMap<>();
        heroesByTypes = new HashMap<>();
        heroesByLevels = new HashMap<>();
        heroesByPoints = new TreeMap<>();
    }

    @Override
    public void addHero(Hero hero) {
        if (contains(hero)) {
            throw new IllegalArgumentException();
        }
        heroes.add(hero);
        heroesByNames.put(hero.getName(), hero);
        heroesByTypes.putIfAbsent(hero.getHeroType(), new TreeSet<>(Comparator.comparing(Hero::getName)));
        heroesByTypes.get(hero.getHeroType()).add(hero);

        heroesByLevels.putIfAbsent(hero.getLevel(), new TreeSet<>(Comparator.comparing(Hero::getName)));
        heroesByLevels.get(hero.getLevel()).add(hero);

        heroesByPoints.putIfAbsent(hero.getPoints(), new TreeSet<>(Comparator.comparingInt(Hero::getPoints).reversed().thenComparing(Hero::getLevel)));
        heroesByPoints.get(hero.getPoints()).add(hero);
    }

    @Override
    public boolean contains(Hero hero) {
        return heroesByNames.containsKey(hero.getName());
    }

    @Override
    public int size() {
        return heroesByNames.size();
    }

    @Override
    public Hero getHero(String name) {
        Hero hero = heroesByNames.get(name);
        if (hero == null) {
            throw new IllegalArgumentException();
        }
        return hero;
    }

    @Override
    public Hero remove(String name) {
        Hero hero = heroesByNames.remove(name);

        if (hero == null) {
            throw new IllegalArgumentException();
        }

        heroes.remove(hero);
        //Remove if is also possible
        heroesByTypes.get(hero.getHeroType()).remove(hero);
        heroesByLevels.get(hero.getLevel()).remove(hero);
        heroesByPoints.get(hero.getPoints()).remove(hero);

        return hero;
    }

    @Override
    public Iterable<Hero> removeAllByType(HeroType type) {
        Set<Hero> heroesToRemove = heroesByTypes.remove(type);

        if (heroesToRemove == null) {
            return new ArrayList<>();
        }

        for (Hero hero : heroesToRemove) {
            heroes.remove(hero);
            heroesByNames.remove(hero.getName());
            heroesByLevels.get(hero.getLevel()).remove(hero);
            heroesByPoints.get(hero.getPoints()).remove(hero);
        }

        return heroesToRemove;
    }

    @Override
    public void levelUp(String name) {
        Hero hero = getHero(name);

        remove(name);
        hero.setLevel(hero.getLevel() + 1);
        addHero(hero);
    }

    @Override
    public void rename(String oldName, String newName) {
        Hero hero = heroesByNames.get(oldName);
        if (hero == null) {
            throw new IllegalArgumentException();
        }
        remove(oldName);
        hero.setName(newName);
        addHero(hero);
    }

    @Override
    public Iterable<Hero> getAllByType(HeroType type) {
        Set<Hero> heroes = heroesByTypes.get(type);
        return heroes == null ? new ArrayList<>() : heroes;
    }

    @Override
    public Iterable<Hero> getAllByLevel(int level) {
        Set<Hero> heroes = heroesByLevels.get(level);
        return heroes == null ? new ArrayList<>() : heroes;
    }

    @Override
    public Iterable<Hero> getInPointsRange(int lowerBound, int upperBound) {
        NavigableMap<Integer, Set<Hero>> rangeMap = heroesByPoints.subMap(lowerBound, true, upperBound, false);
        Set<Hero> heroes = new TreeSet<>(Comparator.comparingInt(Hero::getPoints).reversed().thenComparing(Hero::getLevel));
        if (rangeMap == null) {
            return heroes;
        }
        rangeMap.values()
                .forEach(heroes::addAll);
        return heroes;
    }

    @Override
    public Iterable<Hero> getAllOrderedByLevelDescendingThenByName() {
        return heroes;
    }
}
