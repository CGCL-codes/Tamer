    private void createFileHandles(File clipFile) {
        AudioServer audioServer = lane.getProject().getAudioServer();
        boolean newEnvelope = envelope == null;
        if (newEnvelope) {
            envelope = new Envelope();
        }
        double lengthInMicros = 0;
        if (!(clipFile == null)) {
            try {
                RandomAccessFile raf = new RandomAccessFile(clipFile, "r");
                BufferedRandomAccessFile braf = new BufferedRandomAccessFile(raf, buffSize, lane.getProject().getAudioFileManager());
                audioPlayerIn = new EnvelopedAudioReader(braf, FrinikaConfig.sampleRate);
                RandomAccessFile rafG = new RandomAccessFile(clipFile, "r");
                thumbNailIn = new AudioReader(new VanillaRandomAccessFile(rafG), FrinikaConfig.sampleRate);
                if (audioPlayerIn.getFormat().getSampleRate() != FrinikaConfig.sampleRate) {
                    try {
                        throw new Exception(" unsupport format " + audioPlayerIn.getFormat());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                lengthInMicros = audioPlayerIn.getLengthInFrames() / audioPlayerIn.getFormat().getSampleRate() * 1000000.0;
                System.out.println("audioPart:" + clipFile + " " + lengthInMicros / 1000000.0 + " secs");
                outputProcess = new AudioStreamVoice(audioServer, lane.getProject().getSequencer(), audioPlayerIn, (long) realStartTimeInMicros);
                nChannel = audioPlayerIn.getFormat().getChannels();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        envelope.setMaxTime(lengthInMicros);
        if (newEnvelope) {
            envelope.setTOn(0.0);
            envelope.setTOff(lengthInMicros);
        }
        refreshEnvelope();
    }
