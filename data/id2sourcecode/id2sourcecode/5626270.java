    private int getPhrases(HashMap hash, String str) {
        String[] buffer = new String[m_MaxPhraseLength];
        StringTokenizer tok = new StringTokenizer(str, "\n");
        int pos = 1;
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
                    pos++;
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
                        String phrStr = phraseBuffer.toString();
                        String internal = internalFormat(phrStr);
                        FastVector vec = (FastVector) hash.get(internal);
                        if (vec == null) {
                            vec = new FastVector(3);
                            HashMap secHash = new HashMap();
                            secHash.put(phrStr, new Counter());
                            vec.addElement(new Counter(pos + 1 - i));
                            vec.addElement(new Counter());
                            vec.addElement(secHash);
                            hash.put(internal, vec);
                        } else {
                            ((Counter) ((FastVector) vec).elementAt(1)).increment();
                            HashMap secHash = (HashMap) vec.elementAt(2);
                            Counter count = (Counter) secHash.get(phrStr);
                            if (count == null) {
                                secHash.put(phrStr, new Counter());
                            } else {
                                count.increment();
                            }
                        }
                    }
                }
                pos++;
            }
        }
        Iterator phrases = hash.keySet().iterator();
        while (phrases.hasNext()) {
            String phrase = (String) phrases.next();
            FastVector info = (FastVector) hash.get(phrase);
            if (((Counter) ((FastVector) info).elementAt(1)).value() < m_MinNumOccur) {
                phrases.remove();
                continue;
            }
            String canForm = canonicalForm((HashMap) info.elementAt(2));
            if (canForm == null) {
                phrases.remove();
            } else {
                info.setElementAt(canForm, 2);
            }
        }
        return pos;
    }
