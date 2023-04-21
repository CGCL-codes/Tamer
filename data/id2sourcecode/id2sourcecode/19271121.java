        @Override
        public void run() {
            AudioFormat audioFormat = null;
            long bytesPerSecond = 44100 * 4;
            int nSampleSizeInBits = 16;
            String strMixerName = null;
            int nInternalBufferSize = AudioSystem.NOT_SPECIFIED;
            int bufferSecs = 1;
            SourceDataLine line = null;
            try {
                AudioInputStream audioInputStream;
                audioInputStream = AudioSystem.getAudioInputStream(inStream);
                audioFormat = audioInputStream.getFormat();
                bytesPerSecond = (long) audioFormat.getSampleRate() * audioFormat.getChannels() * (nSampleSizeInBits / 8);
                nInternalBufferSize = (int) bytesPerSecond * bufferSecs;
                DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, nInternalBufferSize);
                boolean bIsSupportedDirectly = AudioSystem.isLineSupported(info);
                if (!bIsSupportedDirectly) {
                    AudioFormat sourceFormat = audioFormat;
                    AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), nSampleSizeInBits, sourceFormat.getChannels(), sourceFormat.getChannels() * (nSampleSizeInBits / 8), sourceFormat.getSampleRate(), false);
                    audioInputStream = AudioSystem.getAudioInputStream(targetFormat, audioInputStream);
                    audioFormat = audioInputStream.getFormat();
                }
                line = null;
                line = AudioPlayer.getSourceDataLine(mixerName, audioFormat, nInternalBufferSize);
                line.start();
                FloatControl volumeControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
                String gainstr = mixerGain;
                float gain = 0;
                if (gainstr != null) {
                    try {
                        gain = Float.parseFloat(gainstr);
                        gain = (float) 10.0 * (float) Math.log10((double) gain * 0.01);
                        float speechGain = (float) 10.0 * (float) Math.log10((double) volume * 0.01);
                        gain += speechGain;
                    } catch (NumberFormatException ex) {
                        gain = 0;
                        System.err.println("Invalid gain value: " + gain + ", set to 0");
                    }
                    if (gain > volumeControl.getMaximum()) {
                        System.err.println("Gain value: " + gain + " too high, set to Maximum value: " + volumeControl.getMaximum());
                        gain = volumeControl.getMaximum();
                    }
                    if (gain < volumeControl.getMinimum()) {
                        System.err.println("Gain value: " + gain + " too low, set to Minimum value: " + volumeControl.getMinimum());
                        gain = volumeControl.getMinimum();
                    }
                }
                volumeControl.setValue(gain);
                int nBytesRead = 0;
                byte[] abData;
                abData = new byte[4608];
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                while (nBytesRead != -1) {
                    if (nBytesRead >= 0) line.write(abData, 0, nBytesRead);
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (line != null) {
                    line.drain();
                    line.close();
                }
            }
        }
