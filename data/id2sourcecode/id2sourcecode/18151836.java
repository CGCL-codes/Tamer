    public void run() {
        AudioInputStream audioInputStream = null;
        SourceDataLine dataLine = null;
        int read = 0;
        byte[] data = new byte[524288];
        try {
            audioInputStream = AudioSystem.getAudioInputStream(fileUrl);
            AudioFormat format = audioInputStream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            dataLine = (SourceDataLine) AudioSystem.getLine(info);
            dataLine.open(format);
            dataLine.start();
            while (read != -1) {
                read = audioInputStream.read(data, 0, data.length);
                if (read >= 0) {
                    dataLine.write(data, 0, read);
                }
            }
        } catch (Exception e) {
            this.processThreadFailed(e);
        } finally {
            if (dataLine != null) {
                try {
                    dataLine.drain();
                } catch (Exception ignored) {
                }
                try {
                    dataLine.close();
                } catch (Exception ignore) {
                }
            }
        }
        this.processThreadFinished();
    }
