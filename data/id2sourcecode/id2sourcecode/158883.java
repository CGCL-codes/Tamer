    public void playBack() {
        File soundFile = new File("trumpet-c4.wav");
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        AudioFormat audioFormat = audioInputStream.getFormat();
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        float sample_rate = audioFormat.getSampleRate();
        System.out.println("sample rate = " + sample_rate);
        float T = audioInputStream.getFrameLength() / audioFormat.getFrameRate();
        System.out.println("T = " + T + " (length of sampled sound in seconds)");
        int n = (int) (T * sample_rate) / 2;
        System.out.println("n = " + n + " (number of equidistant points)");
        float h = (T / n);
        System.out.println("h = " + h + " (length of each time interval in seconds)");
        line.start();
        int nBytesRead = 0;
        byte[] audioBytes = new byte[EXTERNAL_BUFFER_SIZE];
        while (nBytesRead != -1) {
            try {
                nBytesRead = audioInputStream.read(audioBytes, 0, audioBytes.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (nBytesRead >= 0) {
                int nBytesWritten = line.write(audioBytes, 0, nBytesRead);
            }
        }
        line.drain();
        line.close();
        int[] audioData = null;
        if (audioFormat.getSampleSizeInBits() == 16) {
            int nlengthInSamples = audioBytes.length / 2;
            audioData = new int[nlengthInSamples];
            if (audioFormat.isBigEndian()) {
                for (int i = 0; i < nlengthInSamples; i++) {
                    int MSB = (int) audioBytes[2 * i];
                    int LSB = (int) audioBytes[2 * i + 1];
                    audioData[i] = MSB << 8 | (255 & LSB);
                }
            } else {
                for (int i = 0; i < nlengthInSamples; i++) {
                    int LSB = (int) audioBytes[2 * i];
                    int MSB = (int) audioBytes[2 * i + 1];
                    audioData[i] = MSB << 8 | (255 & LSB);
                }
            }
        } else if (audioFormat.getSampleSizeInBits() == 8) {
            int nlengthInSamples = audioBytes.length;
            audioData = new int[nlengthInSamples];
            if (audioFormat.getEncoding().toString().startsWith("PCM_SIGN")) {
                for (int i = 0; i < audioBytes.length; i++) {
                    audioData[i] = audioBytes[i];
                }
            } else {
                for (int i = 0; i < audioBytes.length; i++) {
                    audioData[i] = audioBytes[i] - 128;
                }
            }
        }
        XYSeries series1 = new XYSeries("DFT");
        XYSeries series2 = new XYSeries("DFT");
        int frames_per_pixel = audioBytes.length / audioFormat.getFrameSize() / n;
        byte my_byte = 0;
        double y_last = 0;
        int numChannels = audioFormat.getChannels();
        for (double x = 0; x < n / 2 && audioData != null; x++) {
            int idx = (int) (frames_per_pixel * numChannels * x);
            if (audioFormat.getSampleSizeInBits() == 8) {
                my_byte = (byte) audioData[idx];
            } else {
                my_byte = (byte) (128 * audioData[idx] / 32768);
            }
            double y_new = (double) (h * (128 - my_byte) / 256);
            series1.add(x, y_new);
            y_last = y_new;
        }
        LineChart demo = new LineChart("Line Chart con XYDataset");
        demo.setSerie(series1);
        demo.init("probori", "movimientos");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);
        String userHome = System.getProperty("user.home");
        try {
            demo.saveImage(userHome + "/movimientosTotales.jpg", 500, 450);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
