    public SoundMp3(String filename) {
        super();
        this.filename = filename;
        stop = false;
        try {
            InputStream is = AssetsController.getInputStream(filename);
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            AudioFormat baseFormat = ais.getFormat();
            decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            audioInputStream = AudioSystem.getAudioInputStream(decodedFormat, ais);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(decodedFormat);
        } catch (UnsupportedAudioFileException e) {
            System.err.println("WARNING - \"" + filename + "\" is a no supported MP3 file - sound will be disabled");
        } catch (IOException e) {
            System.err.println("WARNING - could not open \"" + filename + "\" - sound will be disabled");
        } catch (LineUnavailableException e) {
            System.err.println("WARNING - audio device is unavailable to play \"" + filename + "\" - sound will be disabled");
        }
    }
