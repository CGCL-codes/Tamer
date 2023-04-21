    public boolean loadVocabulary(URL url) {
        clear();
        int ID = 0;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                String[] field = line.split("\t");
                if (field.length == 2) {
                    Word word = new Word();
                    word.label = field[0];
                    word.ID = ID++;
                    word.phonemes = parsePhonemes(field[1]);
                    word.inputSequence = phonemeSet.getWordInputCode(word.phonemes);
                    add(word);
                }
            }
            in.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
