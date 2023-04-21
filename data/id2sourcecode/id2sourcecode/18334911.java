    private void extendRuns(EnumStructure[] tags) {
        for (int i = 1; i < monomerCount - 4; ++i) if (tags[i] == EnumStructure.NONE && tags[i + 1] != EnumStructure.NONE) tags[i] = tags[i + 1];
        tags[0] = tags[1];
        tags[monomerCount - 1] = tags[monomerCount - 2];
    }
