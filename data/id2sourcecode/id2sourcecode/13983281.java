    public Composition nextUniform(int size) {
        double vector[] = new double[size + 1];
        double result[] = new double[size];
        vector[0] = 0;
        for (int i = 1; i < size; i++) vector[i] = generator.nextDouble();
        vector[size] = 1;
        Arrays.sort(vector);
        for (int i = 0; i < size; i++) result[i] = vector[i + 1] - vector[i];
        vector = null;
        return new Composition(result);
    }
