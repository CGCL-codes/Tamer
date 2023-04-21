    private ArrayList<String> getNearWords(String word) {
        ArrayList<String> near_words = new ArrayList<String>();
        char[] letters = word.toCharArray();
        char[] copy;
        copy = new char[letters.length - 1];
        for (int i = 1; i < letters.length; i++) {
            copy[i - 1] = letters[i];
        }
        for (int i = 0; i < letters.length - 1; i++) {
            near_words.add(new String(copy));
            copy[i] = letters[i];
        }
        copy = new char[letters.length];
        for (int i = 0; i < letters.length; i++) {
            copy[i] = letters[i];
        }
        for (int i = 0; i < letters.length - 1; i++) {
            char tmp = copy[i + 1];
            copy[i + 1] = copy[i];
            copy[i] = tmp;
            near_words.add(new String(copy));
            copy[i] = copy[i + 1];
            copy[i + 1] = tmp;
        }
        return near_words;
    }
