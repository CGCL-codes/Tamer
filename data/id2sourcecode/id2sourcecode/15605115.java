    public void send(String aFilename) throws UnsupportedAudioFileException, IOException {
        Object wp = new Object();
        double time = transferSize / (getFormat().getSampleRate() * getFormat().getSampleSizeInBits() / 8 * getFormat().getChannels());
        try {
            System.out.println("Now Sending " + aFilename);
            AudioFormat orgFormat;
            byte[] data = new byte[transferSize];
            File mySong = new File(aFilename);
            time = time * 1.01;
            Ticker wait = new Ticker((float) time, wp);
            try {
                int dataRead;
                AudioInputStream orgStream, sendStream;
                orgStream = AudioSystem.getAudioInputStream(mySong);
                orgFormat = orgStream.getFormat();
                System.out.println("Input Format is: " + orgFormat);
                System.out.println("Network Format is: " + getFormat());
                if (!orgFormat.matches(getFormat())) {
                    System.out.println("Audio formats differ. Trying conversion.");
                    if (!AudioSystem.isConversionSupported(getFormat(), orgStream.getFormat())) {
                        System.out.println("Conversion is not supported");
                        return;
                    }
                    sendStream = AudioSystem.getAudioInputStream(getFormat(), orgStream);
                } else {
                    sendStream = orgStream;
                }
                while (sendStream.available() > 0) {
                    synchronized (wp) {
                        dataRead = sendStream.read(data);
                        DatagramPacket beat = new DatagramPacket(data, dataRead, memberGroup, port);
                        wp.wait();
                        servicePort.send(beat);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                wait.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
