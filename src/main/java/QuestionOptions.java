import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class QuestionOptions {
    public class Option implements Comparable<Option> {
        private Integer id;
        private Character selector;
        private String text;

        public Option(Integer i, Character s, String t) {
            this.id = i;
            this.selector = s;
            this.text = t;
        }

        public Integer getId() {
            return this.id;
        }

        public Character getSelector() {
            return this.selector;
        }

        public String getText() {
            return this.text;
        }

        @Override
        public int compareTo(Option o) {
            if (this.selector == null || o.selector == null) {
                return 0;
            } else {
                return this.selector.compareTo(o.selector);
            }
        }
    }

    private ArrayList<Character> selectors;
    private ArrayList<Option> options;

    public QuestionOptions() {
        this.options = new ArrayList<Option>();
        this.selectors = new ArrayList<Character>();
        this.selectors.add('A');
        this.selectors.add('B');
        this.selectors.add('C');
        this.selectors.add('D');
    }

    public void addOption(String text) {
        int idx = (int)(Math.random() * this.selectors.size());
        Option o = new Option(options.size(), this.selectors.get(idx), text);
        this.options.add(o);
        this.selectors.remove(idx);
    }

    public String getPrintableOptions() {
        StringBuilder result = new StringBuilder();
        Collections.sort(options);

        for (Option o : options) {
            result.append(o.selector);
            result.append(". ");
            result.append(o.text);
            result.append("\n");
        }
        return result.toString();
    }

    public Integer getResponseIndex(Character response) {
        for (Integer i = 0; i < this.options.size(); i++) {
            Option o = this.options.get(i);
            if (o.selector.equals(Character.toUpperCase(response))) {
                return o.id;
            }
        }
        throw new IllegalArgumentException(response + " is not a valid response.");
    }
}
