    private boolean loadSound(Object o) {
        if (o instanceof URL) {
            try {
                soundObject = AudioSystem.getAudioInputStream((URL) o);
            } catch (Exception e) {
                try {
                    soundObject = MidiSystem.getSequence((URL) o);
                } catch (InvalidMidiDataException e2) {
                    System.out.println("Unsupported audio file.");
                    return false;
                } catch (Exception e3) {
                    e3.printStackTrace();
                    soundObject = null;
                    return false;
                }
            }
        } else if (o instanceof File) {
            try {
                soundObject = AudioSystem.getAudioInputStream((File) o);
            } catch (Exception e1) {
                try {
                    FileInputStream fis = new FileInputStream((File) o);
                    soundObject = new BufferedInputStream(fis, 1024);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    soundObject = null;
                    return false;
                }
            }
        }
        if (sequencer == null) {
            soundObject = null;
            return false;
        }
        if (soundObject instanceof AudioInputStream) {
            try {
                AudioInputStream stream = (AudioInputStream) soundObject;
                AudioFormat format = stream.getFormat();
                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                    AudioFormat tmp = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                    stream = AudioSystem.getAudioInputStream(tmp, stream);
                    format = tmp;
                }
                DataLine.Info info = new DataLine.Info(Clip.class, stream.getFormat(), ((int) stream.getFrameLength() * format.getFrameSize()));
                Clip clip = (Clip) AudioSystem.getLine(info);
                clip.addLineListener(this);
                clip.open(stream);
                soundObject = clip;
            } catch (Exception ex) {
                ex.printStackTrace();
                soundObject = null;
                return false;
            }
        } else if (soundObject instanceof Sequence || soundObject instanceof BufferedInputStream) {
            try {
                sequencer.open();
                if (soundObject instanceof Sequence) {
                    sequencer.setSequence((Sequence) soundObject);
                } else {
                    sequencer.setSequence((BufferedInputStream) soundObject);
                }
            } catch (InvalidMidiDataException e2) {
                System.out.println("Unsupported audio file.");
                soundObject = null;
                return false;
            } catch (Exception e3) {
                e3.printStackTrace();
                soundObject = null;
                return false;
            }
        }
        return true;
    }
