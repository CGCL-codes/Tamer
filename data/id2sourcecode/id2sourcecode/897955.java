    public boolean loadSound(Object object) {
        duration = 0.0;
        (loading = new Loading()).start();
        if (object instanceof URL) {
            currentName = ((URL) object).getFile();
            playbackMonitor.repaint();
            try {
                currentSound = AudioSystem.getAudioInputStream((URL) object);
            } catch (Exception e) {
                try {
                    currentSound = MidiSystem.getSequence((URL) object);
                } catch (InvalidMidiDataException imde) {
                    System.out.println("Unsupported audio file.");
                    return false;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    currentSound = null;
                    return false;
                }
            }
        } else if (object instanceof File) {
            currentName = ((File) object).getName();
            playbackMonitor.repaint();
            try {
                currentSound = AudioSystem.getAudioInputStream((File) object);
            } catch (Exception e1) {
                try {
                    FileInputStream is = new FileInputStream((File) object);
                    currentSound = new BufferedInputStream(is, 1024);
                } catch (Exception e3) {
                    e3.printStackTrace();
                    currentSound = null;
                    return false;
                }
            }
        }
        loading.interrupt();
        if (sequencer == null) {
            currentSound = null;
            return false;
        }
        if (currentSound instanceof AudioInputStream) {
            try {
                AudioInputStream stream = (AudioInputStream) currentSound;
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
                currentSound = clip;
                seekSlider.setMaximum((int) stream.getFrameLength());
            } catch (Exception ex) {
                ex.printStackTrace();
                currentSound = null;
                return false;
            }
        } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
            try {
                sequencer.open();
                if (currentSound instanceof Sequence) {
                    sequencer.setSequence((Sequence) currentSound);
                } else {
                    sequencer.setSequence((BufferedInputStream) currentSound);
                }
                seekSlider.setMaximum((int) (sequencer.getMicrosecondLength() / 1000));
            } catch (InvalidMidiDataException imde) {
                System.out.println("Unsupported audio file.");
                currentSound = null;
                return false;
            } catch (Exception ex) {
                ex.printStackTrace();
                currentSound = null;
                return false;
            }
        }
        seekSlider.setValue(0);
        seekSlider.setEnabled(true);
        panSlider.setEnabled(true);
        gainSlider.setEnabled(true);
        duration = getDuration();
        return true;
    }
