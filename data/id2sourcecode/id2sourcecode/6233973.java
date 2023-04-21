    public WordsEnumerator(IRebus rebus, RebusEnumerator enumerator) {
        AbstractWord[] words = new AbstractWord[rebus.getVisibleWords().size()];
        int k = 0;
        for (VisibleWord word : rebus.getVisibleWords()) {
            words[k++] = word.getElement();
        }
        for (int j = 0; j < words.length; j++) {
            for (int i = 0; i < words.length - 1; i++) {
                if (words[i].compareTo(words[i + 1]) == -1) {
                    AbstractWord w = words[i];
                    words[i] = words[i + 1];
                    words[i + 1] = w;
                }
            }
        }
        myEnumerator = enumerator;
        myRebus = rebus;
        myWords = words;
        myHelpers = new EnumHelper[words.length];
    }
