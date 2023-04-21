    private HashMap getPhrasesForDictionary(String str) {
        String[] buffer = new String[m_MaxPhraseLength];
        HashMap hash = new HashMap();
        StringTokenizer tok = new StringTokenizer(str, "\n");
        while (tok.hasMoreTokens()) {
            String phrase = tok.nextToken();
            int numSeen = 0;
            StringTokenizer wordTok = new StringTokenizer(phrase, " ");
            while (wordTok.hasMoreTokens()) {
                String word = wordTok.nextToken();
                for (int i = 0; i < m_MaxPhraseLength - 1; i++) {
                    buffer[i] = buffer[i + 1];
                }
                buffer[m_MaxPhraseLength - 1] = word;
                numSeen++;
                if (numSeen > m_MaxPhraseLength) {
                    numSeen = m_MaxPhraseLength;
                }
                if (m_Stopwords.isStopword(buffer[m_MaxPhraseLength - 1])) {
                    continue;
                }
                StringBuffer phraseBuffer = new StringBuffer();
                for (int i = 1; i <= numSeen; i++) {
                    if (i > 1) {
                        phraseBuffer.insert(0, ' ');
                    }
                    phraseBuffer.insert(0, buffer[m_MaxPhraseLength - i]);
                    if ((i > 1) && (m_Stopwords.isStopword(buffer[m_MaxPhraseLength - i]))) {
                        continue;
                    }
                    if (i >= m_MinPhraseLength) {
                        String orig = phraseBuffer.toString();
                        String internal = internalFormat(orig);
                        Counter count = (Counter) hash.get(internal);
                        if (count == null) {
                            hash.put(internal, new Counter());
                        } else {
                            count.increment();
                        }
                    }
                }
            }
        }
        return hash;
    }
