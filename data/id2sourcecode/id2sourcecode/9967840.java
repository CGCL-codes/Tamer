    public void demoSound(String fnm) {
        try {
            AudioInputStream stream = AudioSystem.getAudioInputStream(SoundTests.class.getResourceAsStream(fnm));
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
                System.exit(0);
            }
            clip = AudioSystem.getClip();
            clip.addLineListener(this);
            clip.open(stream);
            stream.close();
            double duration = clip.getMicrosecondLength() / 1000000.0;
            System.out.println("Duration: " + df.format(duration) + " secs");
        } catch (UnsupportedAudioFileException audioException) {
            audioException.printStackTrace();
            System.out.println("Unsupported audio file: " + fnm);
            System.exit(0);
        } catch (LineUnavailableException noLineException) {
            noLineException.printStackTrace();
            System.out.println("No audio line available for : " + fnm);
            System.exit(0);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("Could not read: " + fnm);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem with " + fnm);
            System.exit(0);
        }
    }
