        @Override
        public AudioInputStream nextElement() {
            AudioInputStream stream = null;
            if (lastFile == null) {
                nextFile = readNext();
            }
            if (nextFile != null) {
                try {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(nextFile);
                    AudioFormat format = ais.getFormat();
                    if (!isInitialized) {
                        isInitialized = true;
                        bigEndian = format.isBigEndian();
                        sampleRate = (int) format.getSampleRate();
                        signedData = format.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED);
                        bytesPerValue = format.getSampleSizeInBits() / 8;
                    }
                    if (format.getSampleRate() != sampleRate || format.getChannels() != 1 || format.isBigEndian() != bigEndian) {
                        throw new RuntimeException("format mismatch for subsequent files");
                    }
                    stream = ais;
                    logger.finer("Strating processing of '" + lastFile.getFile() + '\'');
                    for (AudioFileProcessListener fl : fileListeners) fl.audioFileProcStarted(new File(nextFile.getFile()));
                    lastFile = nextFile;
                    nextFile = null;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    throw new Error("Cannot convert " + nextFile + " to a FileInputStream");
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
            return stream;
        }
