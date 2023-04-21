    private static BreakIterator createBreakInstance(Locale where, int type, String rulesName, String dictionaryName) {
        ResourceBundle bundle = getBundle("sun.text.resources.BreakIteratorRules", where);
        String[] classNames = bundle.getStringArray("BreakIteratorClasses");
        String rules = bundle.getString(rulesName);
        if (classNames[type].equals("RuleBasedBreakIterator")) {
            return new RuleBasedBreakIterator(rules);
        } else if (classNames[type].equals("DictionaryBasedBreakIterator")) {
            try {
                URL url = (URL) bundle.getObject(dictionaryName);
                InputStream dictionary = url.openStream();
                return new DictionaryBasedBreakIterator(rules, dictionary);
            } catch (IOException e) {
            } catch (MissingResourceException e) {
            }
            return new RuleBasedBreakIterator(rules);
        } else throw new IllegalArgumentException("Invalid break iterator class \"" + classNames[type] + "\"");
    }
