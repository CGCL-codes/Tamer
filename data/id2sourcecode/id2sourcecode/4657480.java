    public int next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        int value = from + rand.nextInt(to - from);
        while (memory.containsKey(value)) {
            ++value;
            if (value == to) {
                value = from;
            }
        }
        memory.put(value, value);
        return value;
    }
