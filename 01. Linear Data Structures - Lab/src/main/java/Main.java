import implementations.ArrayList;
import implementations.Stack;
import interfaces.AbstractStack;
import interfaces.List;

import java.util.Iterator;

public class Main {
    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("1");
        arrayList.add("2");
        arrayList.add("3");
        arrayList.add("4");
        arrayList.add("5");
        arrayList.add(2, "rwe");

        AbstractStack<Integer> stack = new Stack<>();
        stack.push(3);
        Iterator<Integer> iterator = stack.iterator();
        System.out.println(iterator.hasNext());
        System.out.println(iterator.next());
        System.out.println(iterator.hasNext());
    }
}
