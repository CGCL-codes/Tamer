    public byte[] randomOverwriteFuzz(int size, String stuff) {
        byte randomOverwriteFuzz[] = readedByte.clone();
        byte s = getByte(stuff);
        int[] randomIndex = getRandoms(size, randomOverwriteFuzz.length);
        for (int i = 0; i < randomIndex.length; i++) {
            randomOverwriteFuzz[randomIndex[i]] = s;
        }
        return randomOverwriteFuzz;
    }
