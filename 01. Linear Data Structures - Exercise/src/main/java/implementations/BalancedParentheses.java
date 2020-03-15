package implementations;

import interfaces.Solvable;

import java.util.HashMap;
import java.util.Map;

public class BalancedParentheses implements Solvable {

    private static final Map<Character, Character> OPEN_CLOSED_PARENTHESES = new HashMap<>() {{
        put('{', '}');
        put('(', ')');
        put('[', ']');
        put('}', '{');
        put(')', '(');
        put(']', '[');
    }};

    private String parentheses;

    public BalancedParentheses(String parentheses) {
        this.parentheses = parentheses;
    }

    @Override
    public Boolean solve() {
        ArrayDeque<Character> stack = new ArrayDeque<>();
        for (int i = 0; i < parentheses.length(); i++) {
            char current = parentheses.charAt(i);
            if (OPEN_CLOSED_PARENTHESES.get(stack.peek()) != null && OPEN_CLOSED_PARENTHESES.get(stack.peek()).equals(current)) {
                stack.pop();
            } else {
                stack.push(current);
            }
        }

        return stack.isEmpty();
    }
}
