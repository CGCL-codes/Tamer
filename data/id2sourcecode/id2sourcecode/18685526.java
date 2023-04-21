    public void removeTimepoint(int which) {
        Keyframe newv[] = new Keyframe[value.length - 1];
        double newt[] = new double[time.length - 1];
        Smoothness news[] = new Smoothness[smoothness.length - 1];
        for (int j = 0; j < newv.length; j++) {
            if (j < which) {
                newv[j] = value[j];
                newt[j] = time[j];
                news[j] = smoothness[j];
            } else {
                newv[j] = value[j + 1];
                newt[j] = time[j + 1];
                news[j] = smoothness[j + 1];
            }
        }
        value = newv;
        time = newt;
        smoothness = news;
    }
