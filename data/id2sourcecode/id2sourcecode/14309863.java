    public void setDensity(int[] results) {
        totalAlarms = results[0];
        densityArray = new int[results.length - 1];
        for (int i = 0; i < densityArray.length; i++) {
            densityArray[i] = results[i + 1];
        }
    }
