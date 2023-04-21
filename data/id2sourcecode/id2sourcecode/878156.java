    public void play(String filename, int song) {
        byte[] module;
        int moduleLen;
        try {
            InputStream is = new URL(getDocumentBase(), filename).openStream();
            module = new byte[ASAPInfo.MAX_MODULE_LENGTH];
            moduleLen = readAndClose(is, module);
        } catch (IOException e) {
            showStatus("ERROR LOADING " + filename);
            return;
        }
        ASAPInfo info;
        synchronized (asap) {
            try {
                asap.load(filename, module, moduleLen);
                info = asap.getInfo();
                if (song < 0) song = info.getDefaultSong();
                asap.playSong(song, info.getLoop(song) ? -1 : info.getDuration(song));
            } catch (Exception e) {
                showStatus(e.getMessage());
                return;
            }
        }
        AudioFormat format = new AudioFormat(ASAP.SAMPLE_RATE, 16, info.getChannels(), true, false);
        try {
            line = (SourceDataLine) AudioSystem.getLine(new DataLine.Info(SourceDataLine.class, format));
            line.open(format);
        } catch (LineUnavailableException e) {
            showStatus("ERROR OPENING AUDIO");
            return;
        }
        line.start();
        if (!running) {
            running = true;
            new Thread(this).start();
        }
        synchronized (this) {
            paused = false;
            notify();
        }
        repaint();
    }
