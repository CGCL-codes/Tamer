    public Attribute(String name, int min, int max) {
        this.name = name;
        this.min = min;
        this.max = max;
        this.value = (min + max) / 2;
    }
