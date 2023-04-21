    private Object loadSound(Object objSound) {
        Object rtnMe = null;
        this.myDuration = 0.0;
        if (objSound instanceof AudioInputStream) {
            try {
                AudioInputStream stream = (AudioInputStream) objSound;
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
                rtnMe = clip;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        } else if (objSound instanceof Sequence || objSound instanceof BufferedInputStream) {
            rtnMe = objSound;
            try {
                this.mySequencer.open();
                if (objSound instanceof Sequence) {
                    this.mySequencer.setSequence((Sequence) objSound);
                } else {
                    this.mySequencer.setSequence((BufferedInputStream) objSound);
                }
            } catch (InvalidMidiDataException imde) {
                System.out.println("Unsupported audio file.");
                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
        this.myDuration = getDuration(rtnMe);
        return rtnMe;
    }
