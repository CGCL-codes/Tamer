    public String getAdjustedString(String text) {
        String[] tokens = text.toLowerCase().split("\\s+");
        for (int i = 0; i < tokens.length - 1; i++) {
            if (tokens[i + 1].equals("$")) {
                try {
                    Double.parseDouble(tokens[i]);
                    String num = tokens[i];
                    tokens[i] = tokens[i + 1];
                    tokens[i + 1] = num;
                    i++;
                } catch (NumberFormatException e) {
                }
            }
        }
        List<String> splitTokens = new ArrayList<String>(tokens.length * 2);
        for (String token : tokens) {
            String[] tokenSplits = token.replace("-", " - ").split("[_ ]");
            for (String s : tokenSplits) splitTokens.add(s);
        }
        StringBuffer retval = new StringBuffer();
        for (int i = 0; i < splitTokens.size() - 1; i++) {
            retval.append(splitTokens.get(i));
            retval.append(' ');
        }
        retval.append(splitTokens.get(splitTokens.size() - 1));
        return retval.toString();
    }
