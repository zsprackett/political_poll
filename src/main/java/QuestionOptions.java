import java.util.ArrayList;
import java.util.Collections;

public class QuestionOptions {
    public static class Option implements Comparable<Option> {
        private final Integer id;
        private final Character selector;
        private final String text;

        /**
         * Default constructor
         * @param i the ID associated with this option
         * @param s the character selector that a user might type
         * @param t the text that describes this option
         */
        public Option(Integer i, Character s, String t) {
            this.id = i;
            this.selector = s;
            this.text = t;
        }

        /**
         * Allow comparison between options
         * @param o the option to compare against
         * @return an integer describing the results of the comparison
         */
        @Override
        public int compareTo(Option o) {
            if (this.selector == null || o.selector == null) {
                return 0;
            } else {
                return this.selector.compareTo(o.selector);
            }
        }
    }

    private final ArrayList<Character> selectors;
    private final ArrayList<Option> options;

    public QuestionOptions() {
        this.options = new ArrayList<>();
        this.selectors = new ArrayList<>();
        this.selectors.add('A');
        this.selectors.add('B');
        this.selectors.add('C');
        this.selectors.add('D');
    }

    public void addOption(String text, boolean randomize) {
        int idx = 0;
        if (randomize) {
            idx = (int) (Math.random() * this.selectors.size());
        }
        Option o = new Option(options.size(), this.selectors.get(idx), text);
        this.options.add(o);
        this.selectors.remove(idx);
    }

    public String getPrintableOptions(boolean randomize) {
        StringBuilder result = new StringBuilder();
        if (randomize) {
                Collections.sort(options);
        }

        for (Option o : options) {
            result.append(o.selector);
            result.append(". ");
            result.append(o.text);
            result.append("\n");
        }
        return result.toString();
    }

    public Integer getResponseIndex(Character response) throws IllegalArgumentException {
        for (Option o : this.options) {
            if (o.selector.equals(Character.toUpperCase(response))) {
                return o.id;
            }
        }
        throw new IllegalArgumentException(response + " is not a valid response.");
    }
}
