public enum Party {
    DEMOCRAT,
    REPUBLICAN,
    LIBERTARIAN,
    SOCIALIST;

    public static String toCommaSeparatedString() {
        StringBuilder result = new StringBuilder();
        for (Party p : Party.values()) {
            result.append(p.toString());
            result.append(",");
        }

        result.deleteCharAt(result.length()-1);
        return result.toString();
    }
}