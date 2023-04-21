    private void realPlay(TitrePerso titre) {
        if (titre == null) return;
        System.out.println("PLAY : " + titre.getNom());
        titreCourant = titre;
        notifyObserversPlayer();
        File file = titre.getFile();
        try {
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            if (in != null) {
                AudioFormat baseFormat = in.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                din = AudioSystem.getAudioInputStream(decodedFormat, in);
                rawplay(decodedFormat, din);
                in.close();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }
