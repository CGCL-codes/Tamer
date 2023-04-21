    private void loadClip(String fnm) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(getClass().getResource(fnm));
            AudioFormat format = stream.getFormat();
            if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                stream = AudioSystem.getAudioInputStream(newFormat, stream);
                System.out.println("Converted Audio format: " + newFormat);
                format = newFormat;
            }
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Unsupported Clip File: " + fnm);
                return;
            }
            clip = (Clip) AudioSystem.getLine(info);
            clip.addLineListener(this);
            clip.open(stream);
            stream.close();
            checkDuration();
        } catch (UnsupportedAudioFileException audioException) {
            System.out.println("Unsupported audio file: " + fnm);
        } catch (LineUnavailableException noLineException) {
            System.out.println("No audio line available for : " + fnm);
        } catch (IOException ioException) {
            System.out.println("Could not read: " + fnm);
        } catch (Exception e) {
            System.out.println("Problem with " + fnm);
        }
    }
