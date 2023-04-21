    void extendRuns(byte[] tags) {
        for (int i = 1; i < monomerCount - 4; ++i) if (tags[i] == TAG_NADA && tags[i + 1] != TAG_NADA) tags[i] = tags[i + 1];
        tags[0] = tags[1];
        tags[monomerCount - 1] = tags[monomerCount - 2];
    }
