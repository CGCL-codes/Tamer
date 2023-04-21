        private boolean loadSound(File file) {
            try {
                currentSound = AudioSystem.getAudioInputStream(file);
            } catch (Exception e1) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    currentSound = new BufferedInputStream(fileInputStream, 1024);
                } catch (Exception e3) {
                    currentSound = null;
                    return false;
                }
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
                } catch (Exception ex) {
                    currentSound = null;
                    return false;
                }
            } else if (currentSound instanceof Sequence || currentSound instanceof BufferedInputStream) {
                if (isMIDIEnabled) {
                    try {
                        sequencer.open();
                        if (currentSound instanceof Sequence) {
                            sequencer.setSequence((Sequence) currentSound);
                        } else {
                            sequencer.setSequence((BufferedInputStream) currentSound);
                        }
                    } catch (InvalidMidiDataException imde) {
                        currentSound = null;
                        return false;
                    } catch (Exception ex) {
                        currentSound = null;
                        return false;
                    }
                } else {
                    currentSound = null;
                    return false;
                }
            }
            return true;
        }
