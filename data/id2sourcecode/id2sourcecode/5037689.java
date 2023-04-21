    private void workForRandomPeriod(int minDuration, int maxDuration) {
        Random random = new Random();
        int period = minDuration + random.nextInt(maxDuration - minDuration);
        long start = Process.getElapsedCpuTime();
        while (Process.getElapsedCpuTime() - start < period) {
            for (int i = 0, temp = 0; i < 50; i++) {
                temp += i;
            }
        }
    }
