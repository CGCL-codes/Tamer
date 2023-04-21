    void deleteDiscrimination(RenumMap map) {
        int did = map.did;
        LearnerBlock b[] = new LearnerBlock[blocks.length - 1];
        for (int i = 0; i < did; i++) b[i] = blocks[i];
        for (int i = did; i < b.length; i++) b[i] = blocks[i + 1];
        blocks = b;
    }
