    @Override
    public synchronized void send(String filename) throws UnsupportedAudioFileException, IOException {
        Object barrier = new Object();
        float tickerTime = transferSize / (getFormat().getSampleRate() * getFormat().getSampleSizeInBits() / 8 * getFormat().getChannels());
        AudioInputStream orgStream, sendStream;
        AudioFormat orgFormat;
        byte[] data = new byte[transferSize];
        Ticker tickerThread = new Ticker(tickerTime, barrier);
        int dataRead;
        log.info("Now broadcasting " + filename);
        try {
            orgStream = AudioSystem.getAudioInputStream(new File(filename));
            orgFormat = orgStream.getFormat();
            log.fine("Input Format is: " + orgFormat);
            log.fine("Network Format is: " + getFormat());
            if (!orgFormat.matches(getFormat())) {
                if (!AudioSystem.isConversionSupported(getFormat(), orgStream.getFormat())) {
                    log.severe("Conversion is not supported");
                    throw new IllegalArgumentException();
                }
                sendStream = AudioSystem.getAudioInputStream(getFormat(), orgStream);
            } else {
                sendStream = orgStream;
            }
            while (sendStream.available() > 0) {
                synchronized (barrier) {
                    dataRead = sendStream.read(data);
                    DatagramPacket packet = new DatagramPacket(data, dataRead, fmgr.getAddr(), fmgr.getPort());
                    barrier.wait();
                    servicePort.send(packet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tickerThread.stop();
        }
    }
