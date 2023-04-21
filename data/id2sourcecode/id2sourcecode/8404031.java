    public Editor(DataBaseController dbc, String user, JPanel pane, int test) {
        File file;
        JFileChooser fc = new JFileChooser();
        ReadIniFile();
        if (thpath != null) {
            fc = new JFileChooser(thpath);
        }
        fc.addChoosableFileFilter(new SpectrogramFileFilter());
        fc.setMultiSelectionEnabled(false);
        int returnVal = fc.showOpenDialog(pane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            thpath = file.getPath();
            name = file.getName();
            WriteIniFile(thpath, name);
            if (name.endsWith(".wav")) {
                try {
                    Song song = new Song();
                    song.tDate = file.lastModified();
                    AudioInputStream AFStream = AudioSystem.getAudioInputStream(file);
                    song.sampleRate = AFStream.getFormat().getSampleRate();
                    song.stereo = AFStream.getFormat().getChannels();
                    song.frameSize = AFStream.getFormat().getFrameSize();
                    int length = (int) (song.frameSize * AFStream.getFrameLength());
                    song.bigEnd = AFStream.getFormat().isBigEndian();
                    AudioFormat.Encoding afe = AFStream.getFormat().getEncoding();
                    song.signed = false;
                    if (afe.toString().startsWith("PCM_SIGNED")) {
                        song.signed = true;
                    }
                    song.ssizeInBits = AFStream.getFormat().getSampleSizeInBits();
                } catch (Exception e) {
                }
            }
        }
        file = null;
    }
