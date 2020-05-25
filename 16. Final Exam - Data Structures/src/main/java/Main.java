import models.Hero;
import models.HeroType;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class Main {
    public static void main(String[] args) {
        Set<Hero> heroSet = new TreeSet<>(new Comparator<Hero>() {
            @Override
            public int compare(Hero o1, Hero o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        Hero sda = new Hero(HeroType.DRUID, 2, "sda", 34);
        Hero sne = new Hero(HeroType.DRUID, 2, "qwqd", 34);
        heroSet.add(sda);
        heroSet.add(sne);

        sne.setName("as");

        System.out.println();

    }
}
