    public Clip loadAudioClip(URL clipName) {
        Clip loadedClip = null;
        try {
            AudioInputStream inputStream;
            if (clipName.getPath().toUpperCase().endsWith(".MP3")) {
                InputStream converted = StreamConverter.convert(clipName.openStream());
                inputStream = AudioSystem.getAudioInputStream(converted);
            } else {
                inputStream = AudioSystem.getAudioInputStream(clipName);
            }
            AudioFormat format = inputStream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                inputStream = AudioSystem.getAudioInputStream(pcmFormat, inputStream);
                format = pcmFormat;
            }
            DataLine.Info clipInfo = new DataLine.Info(Clip.class, format);
            if (AudioSystem.isLineSupported(clipInfo) == false) {
                throw new IllegalArgumentException("AssetLoader.loadAudioClip: " + "Clip format for " + clipName + " is not supported.");
            }
            loadedClip = (Clip) AudioSystem.getLine(clipInfo);
            loadedClip.open(inputStream);
            inputStream.close();
            loadedClip.setFramePosition(0);
        } catch (UnsupportedAudioFileException exception) {
            throw new IllegalArgumentException("SoundAsset.loadAudioClip: " + "Unsupported audio file exception generated for " + clipName);
        } catch (LineUnavailableException exception) {
            throw new IllegalArgumentException("SoundAsset.loadAudioClip: " + "Unsupported audio file exception generated for " + clipName);
        } catch (IOException exception) {
            throw new IllegalArgumentException("SoundAsset.loadAudioClip: " + "Could not read from " + clipName);
        }
        return loadedClip;
    }
