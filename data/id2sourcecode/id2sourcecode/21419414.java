    private Map loadMappedBinaryLexicon(FileInputStream is, int estimatedSize) throws IOException {
        FileChannel fc = is.getChannel();
        MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, (int) fc.size());
        bb.load();
        int size = 0;
        int numEntries = 0;
        List phonemeList = new ArrayList();
        Map lexicon = new LinkedHashMap(estimatedSize * 4 / 3);
        if (bb.getInt() != MAGIC) {
            throw new Error("bad magic number in lexicon");
        }
        if (bb.getInt() != VERSION) {
            throw new Error("bad version number in lexicon");
        }
        size = bb.getInt();
        for (int i = 0; i < size; i++) {
            String phoneme = getString(bb);
            phonemeList.add(phoneme);
        }
        numEntries = bb.getInt();
        for (int i = 0; i < numEntries; i++) {
            String wordAndPos = getString(bb);
            String pos = Character.toString(wordAndPos.charAt(wordAndPos.length() - 1));
            if (!partsOfSpeech.contains(pos)) {
                partsOfSpeech.add(pos);
            }
            int numPhonemes = bb.get();
            String[] phonemes = new String[numPhonemes];
            for (int j = 0; j < numPhonemes; j++) {
                phonemes[j] = (String) phonemeList.get(bb.get());
            }
            lexicon.put(wordAndPos, phonemes);
        }
        fc.close();
        return lexicon;
    }
