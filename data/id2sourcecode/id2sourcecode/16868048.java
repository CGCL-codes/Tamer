    private void outputAudio() {
        new Thread() {

            public void run() {
                try {
                    AudioInputStream in = new AudioInputStream(new ByteArrayInputStream(playing.data), format, numSamples);
                    AudioFormat audioFormat = in.getFormat();
                    AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, audioFormat.getSampleRate(), 16, audioFormat.getChannels(), audioFormat.getChannels() * 2, audioFormat.getSampleRate(), false);
                    AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                    SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(decodedFormat);
                    rawplay(din, line);
                    in.close();
                } catch (Exception e) {
                }
                if (next != null) {
                    playing = next;
                    next = null;
                    outputAudio();
                    System.out.println("play queued!");
                } else {
                    playing = null;
                }
            }
        }.start();
    }
