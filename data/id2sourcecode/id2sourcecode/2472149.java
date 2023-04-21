        private boolean playSound(File file) {
            BufferedInputStream bis;
            try {
                bis = new BufferedInputStream(new FileInputStream(file));
                bis.mark(1000);
                bis.skip(1);
                bis.reset();
            } catch (FileNotFoundException e) {
                logger.warning("Could not find audio file: " + file.getName());
                return false;
            } catch (IOException e) {
                logger.warning("Could not prepare stream for: " + file.getName());
                return false;
            }
            AudioInputStream in;
            try {
                in = AudioSystem.getAudioInputStream(bis);
            } catch (Exception e) {
                logger.warning("Could not get audio input stream for: " + file.getName());
                return false;
            }
            boolean ret = false;
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * (16 / 8), baseFormat.getSampleRate(), baseFormat.isBigEndian());
            AudioInputStream din = AudioSystem.getAudioInputStream(decodedFormat, in);
            if (din == null) {
                logger.warning("Can not get decoded audio input stream");
            } else {
                SourceDataLine line = openLine(decodedFormat);
                if (line != null) {
                    try {
                        startPlaying();
                        rawplay(din, line);
                        ret = true;
                    } catch (IOException e) {
                        logger.log(Level.WARNING, "Error playing: " + file.getName(), e);
                    } finally {
                        stopPlaying();
                        line.drain();
                        line.stop();
                        line.close();
                    }
                }
            }
            try {
                if (din != null) din.close();
                in.close();
            } catch (IOException e) {
            }
            return ret;
        }
