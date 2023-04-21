    public void createWaveForm(byte[] audioBytes, AudioInputStream audioInputStream, double duration) {
        this.audioInputStream = audioInputStream;
        this.duration = duration;
        leftLine.removeAllElements();
        rightLine.removeAllElements();
        XLeftLimWave.add(0.0);
        YLeftLimWave.add(0.0);
        XRightLimWave.add(0.0);
        YRightLimWave.add(0.0);
        AudioFormat format = audioInputStream.getFormat();
        if (audioBytes == null) {
            try {
                audioBytes = new byte[(int) (audioInputStream.getFrameLength() * format.getFrameSize())];
                audioInputStream.read(audioBytes);
            } catch (Exception ex) {
                return;
            }
        }
        Dimension d = getSize();
        int w = d.width;
        int h = d.height;
        int[] audioData = null;
        if (format.getSampleSizeInBits() == 16) {
            int nlengthInSamples = audioBytes.length / 2;
            audioData = new int[nlengthInSamples];
            if (format.isBigEndian()) {
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
        } else if (format.getSampleSizeInBits() == 8) {
            int nlengthInSamples = audioBytes.length;
            audioData = new int[nlengthInSamples];
            if (format.getEncoding().toString().startsWith("PCM_SIGN")) {
                for (int i = 0; i < audioBytes.length; i++) {
                    audioData[i] = audioBytes[i];
                }
            } else {
                for (int i = 0; i < audioBytes.length; i++) {
                    audioData[i] = audioBytes[i] - 128;
                }
            }
        }
        int frames_per_pixel = 0;
        try {
            frames_per_pixel = audioBytes.length / format.getFrameSize() / w;
        } catch (Exception ex) {
            ex.toString();
            return;
        }
        byte my_left_byte = 0;
        byte my_right_byte = 0;
        double y_left_last = 0;
        double y_right_last = 0;
        int numChannels = format.getChannels();
        for (double x = 0; x < w && audioData != null; x++) {
            int idx = (int) (frames_per_pixel * numChannels * x);
            if (format.getSampleSizeInBits() == 8) {
                my_left_byte = (byte) audioData[idx + 1];
                my_right_byte = (byte) audioData[idx];
            } else {
                my_left_byte = (byte) (128 * audioData[idx + 1] / format.getSampleRate());
                my_right_byte = (byte) (128 * audioData[idx] / format.getSampleRate());
            }
            double y_left_new = (double) (h * (128 - my_left_byte) / (256));
            double y_right_new = (double) (h * (128 - my_right_byte) / (256));
            leftLine.add(new Line2D.Double(x, y_left_last, x, y_left_new));
            rightLine.add(new Line2D.Double(x, y_right_last, x, y_right_new));
            y_left_last = y_left_new;
            y_right_last = y_right_new;
            if (y_left_new > limitmax || y_left_new < limitmin) {
                XLeftLimWave.add((x / w) * duration);
                YLeftLimWave.add(y_left_new);
            }
            if (y_right_new > limitmax || y_right_new < limitmin) {
                XRightLimWave.add((x / w) * duration);
                YRightLimWave.add(y_right_new);
            }
        }
        analiseLeftWaveForm(XLeftLimWave, YLeftLimWave);
        XLeftLimWave.removeAllElements();
        YLeftLimWave.removeAllElements();
        analiseRightWaveForm(XRightLimWave, YRightLimWave);
        XRightLimWave.removeAllElements();
        YRightLimWave.removeAllElements();
        repaint();
    }
