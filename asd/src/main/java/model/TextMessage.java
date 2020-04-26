package model;

import java.util.Objects;

public class TextMessage implements Message, Comparable<TextMessage> {
    private int weight;
    private String text;

    public TextMessage(int weight, String text) {
        this.weight = weight;
        this.text = text;
    }

    @Override
    public int getWeight() {
        return this.weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
       this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextMessage that = (TextMessage) o;
        return weight == that.weight &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, text);
    }

    @Override
    public int compareTo(TextMessage o) {
        return Integer.compare(this.weight, o.weight);
    }
}
