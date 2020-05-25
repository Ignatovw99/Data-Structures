import java.util.*;

public class ShoppingCentre {

    private Map<String, Set<Product>> productsByProducer;

    private Map<String, Set<Product>> productsByName;

    private NavigableMap<Double, Set<Product>> productsByPriceRange;

    public ShoppingCentre() {
        productsByProducer = new HashMap<>();
        productsByName = new HashMap<>();
        productsByPriceRange = new TreeMap<>();
    }

    public String addProduct(String name, double price, String producer) {
        Product product = new Product(name, price, producer);
        productsByName.putIfAbsent(name, new TreeSet<>());
        if (productsByName.get(name).contains(product)) {
            productsByName.get(name);
        } else {

        }
        return null;
    }

    public String delete(String name, String producer) {
        return "";
    }

    public String delete(String producer) {
        return "";
    }

    public String findProductsByName(String name) {
        return "";
    }

    public String findProductsByProducer(String producer) {
        return "";
    }

    public String findProductsByPriceRange(double priceFrom, double priceTo) {
        return "";
    }
}
