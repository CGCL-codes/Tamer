    public HashMap<String, Candidate> getCandidates(String text) {
        if (debugMode) {
            System.err.println("---- Extracting candidates...");
        }
        HashMap<String, Candidate> candidatesTable = new HashMap<String, Candidate>();
        int countCandidates = 0;
        String[] buffer = new String[maxPhraseLength];
        StringTokenizer tok = new StringTokenizer(text, "\n");
        int pos = 0;
        int totalFrequency = 0;
        int firstWord = 0;
        while (tok.hasMoreTokens()) {
            String token = tok.nextToken();
            int numSeen = 0;
            StringTokenizer wordTok = new StringTokenizer(token, " ");
            while (wordTok.hasMoreTokens()) {
                pos++;
                String word = wordTok.nextToken();
                for (int i = 0; i < maxPhraseLength - 1; i++) {
                    buffer[i] = buffer[i + 1];
                }
                buffer[maxPhraseLength - 1] = word;
                numSeen++;
                if (numSeen > maxPhraseLength) {
                    numSeen = maxPhraseLength;
                }
                if (vocabularyName.equals("none")) {
                    if (stopwords.isStopword(buffer[maxPhraseLength - 1])) {
                        continue;
                    }
                }
                StringBuffer phraseBuffer = new StringBuffer();
                for (int i = 1; i <= numSeen; i++) {
                    if (i > 1) {
                        phraseBuffer.insert(0, ' ');
                    }
                    phraseBuffer.insert(0, buffer[maxPhraseLength - i]);
                    if (vocabularyName.equals("none")) {
                        if ((i > 1) && (stopwords.isStopword(buffer[maxPhraseLength - i]))) {
                            continue;
                        }
                    }
                    if (i >= minPhraseLength) {
                        String form = phraseBuffer.toString();
                        Vector<String> candidateNames = new Vector<String>();
                        if (vocabularyName.equals("none")) {
                            String phrase = pseudoPhrase(form);
                            if (phrase != null) candidateNames.add(phrase);
                            totalFrequency++;
                        } else if (vocabularyName.equals("wikipedia")) {
                            String patternStr = "[0-9\\s]+";
                            Pattern pattern = Pattern.compile(patternStr);
                            Matcher matcher = pattern.matcher(form);
                            boolean matchFound = matcher.matches();
                            if (matchFound == false) {
                                candidateNames.add(form);
                            }
                        } else {
                            for (String sense : vocabulary.getSenses(form)) {
                                candidateNames.add(sense);
                            }
                        }
                        if (!candidateNames.isEmpty()) {
                            for (String name : candidateNames) {
                                Candidate candidate = candidatesTable.get(name);
                                if (candidate == null) {
                                    if (vocabularyName.equals("wikipedia")) {
                                        Anchor anchor;
                                        try {
                                            anchor = new Anchor(form, textProcessor, wikipedia.getDatabase());
                                            double probability = anchor.getLinkProbability();
                                            if (probability >= minKeyphraseness) {
                                                countCandidates++;
                                                totalFrequency++;
                                                firstWord = pos - i;
                                                candidate = new Candidate(name, form, firstWord, anchor, probability);
                                            }
                                        } catch (SQLException e) {
                                            System.err.println("Error adding ngram " + form);
                                            e.printStackTrace();
                                        }
                                    } else {
                                        firstWord = pos - i;
                                        candidate = new Candidate(name, form, firstWord);
                                        totalFrequency++;
                                        if (!vocabularyName.equals("none")) {
                                            candidate.setTitle(vocabulary.getTerm(name));
                                        }
                                    }
                                } else {
                                    firstWord = pos - i;
                                    candidate.recordOccurrence(form, firstWord);
                                    countCandidates++;
                                    totalFrequency++;
                                }
                                if (candidate != null) {
                                    candidatesTable.put(name, candidate);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Candidate candidate : candidatesTable.values()) {
            candidate.normalize(totalFrequency, pos);
        }
        if (vocabularyName.equals("wikipedia")) {
            candidatesTable = disambiguateCandidates(candidatesTable.values());
        }
        return candidatesTable;
    }
