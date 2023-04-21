    public WavePlayer(final WaveDataProducer producer) {
        try {
            final AudioFormat audioFormat = new AudioFormat(producer.getSampleRate(), producer.getBitsPerSample(), producer.getChannels(), true, false);
            final DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
            this.dataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            this.dataLine.open(dataLine.getFormat());
            this.dataLine.start();
            this.datalineWriterThread = new Thread() {

                @Override
                public void run() {
                    while (dataLine.isOpen()) {
                        final byte[] data = buffer.toByteArray();
                        dataLine.write(data, 0, data.length);
                        synchronized (buffer) {
                            buffer.reset();
                            try {
                                buffer.wait();
                            } catch (InterruptedException e) {
                            }
                        }
                    }
                }
            };
            this.datalineWriterThread.start();
        } catch (Exception e) {
        }
    }
