package core;

import models.Planet;
import models.Star;

import java.util.*;

public class MilkyWay implements Galaxy {

    private Map<Integer, Star> starsByIds;

    private Set<Star> stars;

    private Map<Integer, Planet> planetsByIds;

    private Set<Planet> planets;

    private Set<Star> startsInOrderOfDiscovery;

    private Map<Star, Set<Planet>> starsByPlanets;

    private Map<Planet, Star> planetsByStars;

    public MilkyWay() {
        starsByIds = new HashMap<>();
        stars = new TreeSet<>(Comparator.comparingInt(Star::getLuminosity).reversed());
        planetsByIds = new HashMap<>();
        planets = new TreeSet<>(Comparator.comparing(Planet::getMass).reversed());
        startsInOrderOfDiscovery = new LinkedHashSet<>();
        starsByPlanets = new LinkedHashMap<>();
        planetsByStars = new HashMap<>();
    }

    @Override
    public void add(Star star) {
        if (contains(star)) {
            throw new IllegalArgumentException();
        }
        starsByIds.put(star.getId(), star);
        stars.add(star);
        startsInOrderOfDiscovery.add(star);
        starsByPlanets.put(star, new TreeSet<>(Comparator.comparingInt(Planet::getDistanceFromStar).thenComparingInt(Planet::getMass)));
    }

    @Override
    public void add(Planet planet, Star star) {
        if (contains(planet)) {
            throw new IllegalArgumentException();
        }
        if (!contains(star)) {
            throw new IllegalArgumentException();
        }
        planetsByIds.put(planet.getId(), planet);
        planets.add(planet);
        starsByPlanets.get(star).add(planet);
        planetsByStars.put(planet, star);
    }

    @Override
    public boolean contains(Planet planet) {
        return planets.contains(planet);
    }

    @Override
    public boolean contains(Star star) {
        return stars.contains(star);
    }

    @Override
    public Star getStar(int id) {
        Star star = starsByIds.get(id);
        if (star == null) {
            throw new IllegalArgumentException();
        }
        return star;
    }

    @Override
    public Planet getPlanet(int id) {
        Planet planet = planetsByIds.get(id);
        if (planet == null) {
            throw new IllegalArgumentException();
        }
        return planet;
    }

    @Override
    public Star removeStar(int id) {
        Star remove = getStar(id);

        Set<Planet> planetsToRemove = starsByPlanets.get(remove);

        for (Planet planet : planetsToRemove) {
            planetsByIds.remove(planet.getId());
            planets.remove(planet);
            planetsByStars.remove(planet);
        }

        starsByIds.remove(remove.getId());
        starsByPlanets.remove(remove);
        stars.remove(remove);
        startsInOrderOfDiscovery.remove(remove);

        return remove;
    }

    @Override
    public Planet removePlanet(int id) {
        Planet planet = getPlanet(id);
        
        planetsByIds.remove(id);
        planets.remove(planet);

        Star assignedStar = planetsByStars.remove(planet);
        starsByPlanets.get(assignedStar).remove(planet);
        return planet;
    }

    @Override
    public int countObjects() {
        return stars.size() + planets.size();
    }

    @Override
    public Iterable<Planet> getPlanetsByStar(Star star) {
        Set<Planet> planets = starsByPlanets.get(star);
        return planets == null ? new ArrayList<>() : planets;
    }

    @Override
    public Iterable<Star> getStars() {
        return stars;
    }

    @Override
    public Iterable<Star> getStarsInOrderOfDiscovery() {
        return startsInOrderOfDiscovery;
    }

    @Override
    public Iterable<Planet> getAllPlanetsByMass() {
        return planets;
    }

    @Override
    public Iterable<Planet> getAllPlanetsByDistanceFromStar(Star star) {
        Set<Planet> planets = starsByPlanets.get(star);
        return planets == null ? new ArrayList<>() : planets;
    }

    @Override
    public Map<Star, Set<Planet>> getStarsAndPlanetsByOrderOfStarDiscoveryAndPlanetDistanceFromStarThenByPlanetMass() {
        return starsByPlanets;
    }
}
