    private Clip loadClipfromRessources(String fname) {
        File f = null;
        AudioInputStream ais = null;
        AudioFormat format;
        Clip cl = null;
        try {
            f = new File("ressources/" + fname);
            ais = AudioSystem.getAudioInputStream(f);
        } catch (IOException e) {
            try {
                ais = AudioSystem.getAudioInputStream(new Object().getClass().getResource("/ressources/" + fname));
            } catch (UnsupportedAudioFileException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (UnsupportedAudioFileException e) {
            System.out.println("Boeses Fileformat");
            e.printStackTrace();
        }
        try {
            format = ais.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                ais = AudioSystem.getAudioInputStream(tmp, ais);
                format = tmp;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, ais.getFormat(), ((int) ais.getFrameLength() * format.getFrameSize()));
            cl = (Clip) AudioSystem.getLine(info);
            cl.open(ais);
        } catch (IOException e) {
            System.out.println("Fehler beim Einlesen der Sounddatei!");
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            System.out.println("Line nicht verf?gbar!");
        }
        return cl;
    }
