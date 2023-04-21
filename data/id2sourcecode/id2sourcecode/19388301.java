    public void createWaveform(int[] audioData, int accuracy) {
        this.accuracy = accuracy;
        int w = audioData.length / accuracy;
        int h = getSize().y;
        int frames_per_pixel = audioData.length / af.getFrameSize() / w;
        this.waveLength = (audioData.length) / accuracy;
        System.out.println(w + " e anche dim " + this.waveLength);
        byte my_byte = 0;
        lines = new Polyline();
        lines.addPoint(new Point(0, 0));
        int numChannels = af.getChannels();
        for (double x = 0; x < w && audioData != null; x++) {
            int idx = (int) (frames_per_pixel * numChannels * x);
            if (af.getSampleSizeInBits() == 8) {
                my_byte = (byte) audioData[idx];
            } else {
                my_byte = (byte) (128 * audioData[idx] / 32768);
            }
            double y_new = (double) (h * (128 - my_byte) / 256);
            lines.addPoint(new Point(x, y_new));
        }
        setContents(lines);
    }
