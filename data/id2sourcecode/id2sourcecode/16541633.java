        private synchronized void setupInputStream() throws LineUnavailableException, IOException {
            try {
                tearDownInputStream();
                URL urlCon = new URL(this.url);
                this.undecodedInput = AudioSystem.getAudioInputStream(urlCon);
                AudioFileFormat sourceFileFormat = AudioSystem.getAudioFileFormat(undecodedInput);
                sourceFormat = undecodedInput.getFormat();
                targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
                this.inStream = AudioSystem.getAudioInputStream(targetFormat, undecodedInput);
                AudioFormat audioFormat = inStream.getFormat();
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
                this.dataLine = (SourceDataLine) AudioSystem.getLine(info);
                this.dataLine.open(audioFormat, dataLine.getBufferSize());
                this.dataLine.start();
                if (dataLine.isControlSupported(FloatControl.Type.PAN)) {
                    FloatControl tempPanControl = (FloatControl) dataLine.getControl(FloatControl.Type.PAN);
                    if (this.panControl != null) {
                        tempPanControl.setValue(this.panControl.getValue());
                    }
                    this.panControl = tempPanControl;
                    this.setPan(this.pan);
                }
                if (dataLine.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl tempGainControl = (FloatControl) dataLine.getControl(FloatControl.Type.MASTER_GAIN);
                    if (this.gainControl != null) {
                        tempGainControl.setValue(this.gainControl.getValue());
                    }
                    this.gainControl = tempGainControl;
                    this.setGain(this.gain);
                }
                if (dataLine.isControlSupported(BooleanControl.Type.MUTE)) {
                    BooleanControl newControl = (BooleanControl) dataLine.getControl(BooleanControl.Type.MUTE);
                    if (this.muteControl != null) {
                        newControl.setValue(this.muteControl.getValue());
                    }
                    this.muteControl = newControl;
                    this.setMuted(muted);
                }
            } catch (MalformedURLException e) {
                Logger.error(AudioSystemCodes.MALFORMED_STREAM_URL, new Object[] { url, e.getMessage() });
            } catch (UnsupportedAudioFileException e) {
                Logger.error(AudioSystemCodes.UNSUPPORTED_AUDIO_FORMAT, new Object[] { url, e.getMessage(), e.getStackTrace() });
            }
        }
