    public BinaryClassificationPerformance(int type, double[][] counter) {
        this.type = type;
        this.counter[N][N] = counter[N][N];
        this.counter[N][P] = counter[N][P];
        this.counter[P][N] = counter[P][N];
        this.counter[P][P] = counter[P][P];
    }
