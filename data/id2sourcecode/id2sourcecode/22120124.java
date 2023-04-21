    public void load(String name, URL url) {
        if (counter >= maxClips) {
            log.warn(name + " : No more files can be loaded (Max:" + maxClips + ")");
            return;
        }
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(url);
            AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(newFormat, stream);
                format = newFormat;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(this);
            clip.open(stream);
            clipMap.put(name, clip);
            stream.close();
        } catch (LineUnavailableException e) {
            log.warn(name + " : Failed to open line", e);
        } catch (UnsupportedAudioFileException e) {
            log.warn(name + " : This is not a wave file", e);
        } catch (IOException e) {
            log.warn(name + " : Load failed", e);
        }
    }
