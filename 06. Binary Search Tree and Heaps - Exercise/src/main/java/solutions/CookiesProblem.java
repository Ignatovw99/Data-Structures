package solutions;

import java.util.PriorityQueue;
import java.util.Queue;

public class CookiesProblem {

    public Integer solve(int requiredSweetness, int[] cookiesSweetness) {
        Queue<Integer> prioritySweetness = new PriorityQueue<>();

        if (cookiesSweetness.length == 0) {
            return -1;
        }

        for (int sweetness : cookiesSweetness) {
            prioritySweetness.offer(sweetness);
        }

        int currentCookie = prioritySweetness.peek();

        int operations = 0;
        while (currentCookie < requiredSweetness && prioritySweetness.size() > 1) {
            int firstCookie = prioritySweetness.poll();
            int secondCookie = prioritySweetness.poll();

            int combinedCookie = firstCookie + 2 * secondCookie;

            prioritySweetness.offer(combinedCookie);
            operations++;
            currentCookie = prioritySweetness.peek();
        }

        return currentCookie >= requiredSweetness ? operations : -1;
    }
}
