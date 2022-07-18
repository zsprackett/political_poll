import java.util.Arrays;

public enum Party {
    DEMOCRAT,
    REPUBLICAN,
    LIBERTARIAN,
    SOCIALIST;

    /**
     * Retrieve a Party by ordinal id
     * @param   id the ordinal id of the party
     * @return  Returns the Party specified by id or null if no party corresponds to the supplied id
     */
    public static Party getById(Integer id) {
        for (Party e : Party.values()) {
            if (e.ordinal() == id) return e;
        }
        return null;
    }

    /**
     * Generates a string containing all Parties, separated by commas
     * @return String   comma separate list of parties
     */
    public static String toCommaSeparatedString() {
        StringBuilder result = new StringBuilder();
        for (Party p : Party.values()) {
            result.append(p.toString());
            result.append(",");
        }

        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    /**
     * Returns a party name with the first letter capitalized and the rest lower case
     * @param p         the party to format
     * @return String   the result
     */
    public static String capitalizeFirst(Party p) {
        String temp = p.toString();
        return temp.substring(0, 1).toUpperCase() + temp.substring(1).toLowerCase();
    }

    /**
     * Creates a string array that contains the names of each party
    * @return String[]      the result
     */
    public static String[] getNames() {
        return Arrays.stream(Party.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
    }
}