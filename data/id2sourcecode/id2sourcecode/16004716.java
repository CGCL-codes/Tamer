    private void readLanguageList() {
        Properties languageList = new Properties();
        try {
            languageList.load(url.openStream());
            int numLangs = languageList.keySet().size();
            langList.ensureCapacity(numLangs + 3);
            EdLangName commentsLang = readLang("1000", "cm", "Comments");
            boolean readRef = false;
            if (commentsLang == null) {
                commentsLang = readLang("1000", "reference", "Reference");
                readRef = true;
            }
            if (refLang == null) {
                addLang("reference", "Reference", null, false);
                refLang = ((EdLangName) langList.elementAt(readRef ? 0 : 1)).edLang;
            }
            EdLangName hintsLang = readLang("1001", "HT", "Hints");
            int id = 0;
            Enumeration languageKeys = languageList.keys();
            while (languageKeys.hasMoreElements()) {
                String languageName = (String) languageKeys.nextElement();
                String languageCode = (String) languageList.get(languageName);
                String languageNameLwr = languageName.toLowerCase();
                if (languageNameLwr.equals("comments") || languageNameLwr.equals("hints") || languageNameLwr.equals("reference")) continue;
                readLang(Integer.toString(id++), languageCode, languageName);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (langList.size() == 0 || !languageList.containsKey("Reference")) {
                addLang("reference", "Reference", null, false);
                refLang = ((EdLangName) langList.elementAt(0)).edLang;
            }
        }
    }
