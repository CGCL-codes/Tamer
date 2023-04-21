    public void start(int streamNumber) {
        int channels = getChannels(streamNumber);
        if (channels == -1) {
            Debug.println("always used: no: " + streamNumber + ", ch: " + datum[streamNumber].channel);
            return;
        }
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, datum[streamNumber].sampleRate, 16, channels, 2 * channels, datum[streamNumber].sampleRate, false);
        Debug.println(audioFormat);
        try {
            InputStream[] iss = getInputStreams(streamNumber, channels);
            OutputStream os = null;
            if (pcmFileName != null) {
                Debug.println("���������������� output PCM to file: " + pcmFileName);
                os = new BufferedOutputStream(new FileOutputStream(pcmFileName));
            }
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();
            FloatControl gainControl = (FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN);
            double gain = .2d;
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            byte[] buf = new byte[1024];
            while (iss[0].available() > 0) {
                if (channels == 1) {
                    int l = iss[0].read(buf, 0, 1024);
                    line.write(buf, 0, l);
                    if (os != null) {
                        os.write(buf, 0, l);
                    }
                } else {
                    int lL = iss[0].read(buf, 0, 512);
                    iss[1].read(buf, 512, 512);
                    for (int i = 0; i < lL / 2; i++) {
                        byte[] temp = new byte[4];
                        temp[0] = buf[i * 2];
                        temp[1] = buf[i * 2 + 1];
                        temp[2] = buf[512 + i * 2];
                        temp[3] = buf[512 + i * 2 + 1];
                        line.write(temp, 0, 4);
                    }
                }
            }
            line.drain();
            line.stop();
            line.close();
            if (os != null) {
                os.flush();
                os.close();
            }
        } catch (IOException e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        } catch (LineUnavailableException e) {
            throw (RuntimeException) new IllegalStateException().initCause(e);
        }
    }
