    public static Integer generateRandomInt(int upperLimit, boolean unique) {
        Integer randomInteger = null;
        if (unique) {
            do {
                randomInteger = generator.nextInt(upperLimit + 1);
            } while (set.contains(randomInteger));
            set.add(randomInteger);
        } else {
            randomInteger = generator.nextInt(upperLimit + 1);
        }
        return randomInteger;
    }
