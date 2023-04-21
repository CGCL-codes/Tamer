    private Rule getExternalGrammar(String grammarReference, String ruleName) {
        SrgsRuleGrammarParser srgsRuleGrammarParser = new SrgsRuleGrammarParser();
        URL url = null;
        InputStream grammarStream = null;
        try {
            if (grammarReference.contains(":")) {
                url = new URL(grammarReference);
            } else {
                url = new URL(baseURL + grammarReference);
            }
            grammarStream = url.openStream();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Rule rules[] = srgsRuleGrammarParser.load(grammarStream);
        for (Rule r : rules) {
            if (r.getRuleName().compareTo(ruleName) == 0) {
                return r;
            }
        }
        return null;
    }
