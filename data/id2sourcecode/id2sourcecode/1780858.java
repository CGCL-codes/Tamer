    public void swap(int a, int b) {
        int ns = getSampleCount();
        float[] asamples = getChannel(a);
        float[] bsamples = getChannel(b);
        float tmp;
        for (int s = 0; s < ns; s++) {
            tmp = asamples[s];
            asamples[s] = bsamples[s];
            bsamples[s] = tmp;
        }
    }
