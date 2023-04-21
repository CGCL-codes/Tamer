    public void openFile(String filename, boolean resetPlaylist) throws FileNotFoundException {
        if (resetPlaylist) playlist = null;
        if (!status.equals(Status.paused) && !status.equals(Status.stopped)) {
            stopPlayback();
        }
        if (filename.endsWith(".m3u") || filename.endsWith(".pls")) {
            openPlaylist(filename);
            filename = (String) playlist.get(0);
        }
        MpegAudioFileReader reader = new MpegAudioFileReader();
        currentFile = new File(filename);
        openedFile = filename;
        AudioInputStream in = null;
        try {
            in = reader.getAudioInputStream(currentFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        }
        AudioFormat baseFormat = in.getFormat();
        decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        AudioFileFormat baseFileFormat = null;
        try {
            baseFileFormat = AudioSystem.getAudioFileFormat(currentFile);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (baseFileFormat instanceof TAudioFileFormat) {
            Map properties = ((TAudioFileFormat) baseFileFormat).properties();
            String key = "duration";
            length = (Long) properties.get(key);
            key = "title";
            title = (String) properties.get(key);
            if (title == null || title.equals("")) title = currentFile.getName();
        }
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        opened = true;
        play();
    }
