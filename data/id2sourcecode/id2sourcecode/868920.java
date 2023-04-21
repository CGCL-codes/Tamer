    public TestThread(int minRunTime, int maxRunTime) {
        Random r = new Random();
        int min = minRunTime;
        int max = maxRunTime;
        actualRunTime = min + r.nextInt(max - min);
    }
