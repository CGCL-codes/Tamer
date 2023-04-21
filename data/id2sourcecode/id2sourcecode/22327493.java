    public boolean open(InputStream stream, String type) {
        strType = type;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(stream));
            AudioFormat format = audioInputStream.getFormat();
            if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
                AudioFormat baseFormat = audioInputStream.getFormat();
                AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
                decodedStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
                int frameLength = (int) decodedStream.getFrameLength();
                int frameSize = decodedFormat.getFrameSize();
                DataLine.Info info = new DataLine.Info(Clip.class, decodedFormat, frameLength * frameSize);
                clip = (Clip) AudioSystem.getLine(info);
                clip.open(decodedStream);
            } else {
                DataLine.Info info2 = new DataLine.Info(Clip.class, format, AudioSystem.NOT_SPECIFIED);
                clip = (Clip) AudioSystem.getLine(info2);
                clip.open(audioInputStream);
            }
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
