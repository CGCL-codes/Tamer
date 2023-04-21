    private void playSample(byte[] content, int size) throws UnsupportedAudioFileException, IOException {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        setVolume(volume);
        byte[] abuf = new byte[JSASoundManager.format.getFrameSize() * 16];
        AudioInputStream ais = null;
        try {
            for (int i = 0, max = loop >= 0 ? loop : Integer.MAX_VALUE; i < max; i++) {
                InputStream bis = new ByteArrayInputStream(content, 0, size);
                if (ais != null) {
                    ais.close();
                }
                ais = AudioSystem.getAudioInputStream(JSASoundManager.format, AudioSystem.getAudioInputStream(bis));
                int cf = 0;
                int aread;
                while ((aread = ais.read(abuf)) >= 0) {
                    if (shutdown) {
                        line.flush();
                        return;
                    }
                    if (aread == 0) {
                        continue;
                    }
                    while (line.available() < aread) {
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                        }
                        if (shutdown) {
                            line.flush();
                            return;
                        }
                    }
                    line.write(abuf, 0, aread);
                    cf += aread / JSASoundManager.format.getFrameSize();
                    synchronized (lock) {
                        status.setPos((long) (1000f * cf / JSASoundManager.format.getFrameRate()));
                        status.setLoopCount(i + 1);
                    }
                }
            }
        } finally {
            try {
                ais.close();
            } catch (Exception e) {
            }
        }
    }
