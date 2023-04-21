    static synchronized List<BKWord> getWords() {
        List<BKWord> listOfWords = new ArrayList<BKWord>();
        InputStreamReader reader = null;
        BufferedReader buffReader = null;
        try {
            try {
                URL url = KSpellCheckEngine.class.getResource("../../dict/en_US.dic");
                logger.debug("... Loading: " + "../../dict/en_US.dic = {" + url + "}");
                reader = new InputStreamReader(url.openStream());
                buffReader = new BufferedReader(reader);
                String line = null;
                do {
                    line = buffReader.readLine();
                    if (line != null) {
                        String[] parts = line.split("/");
                        if (parts.length == 1) {
                            BKWord newWord = new BKWord(parts[0], true);
                            listOfWords.add(newWord);
                            logger.debug("Added: " + newWord.toString());
                        } else {
                            boolean suggest = parts[1].indexOf("!") > 0;
                            BKWord newWord = new BKWord(parts[0], suggest);
                            for (int i = 0; i < parts[1].length(); i++) {
                                newWord.addClass(parts[1].charAt(i));
                            }
                            listOfWords.add(newWord);
                            logger.debug("Added: " + newWord.toString());
                        }
                    }
                } while (line != null);
            } catch (Exception ex) {
                logger.error("Error loading dictionary", ex);
            } finally {
                if (reader != null) {
                    reader.close();
                }
                if (buffReader != null) {
                    buffReader.close();
                }
            }
        } catch (Exception ex) {
            logger.error("Error loading dictionary", ex);
        }
        return listOfWords;
    }
