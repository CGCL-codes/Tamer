    public void playSoundFile(File song) {
        AudioInputStream din = null;
        @SuppressWarnings("serial")
        class SoundFinishedException extends Exception {
        }
        try {
            File file = song;
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioFormat baseFormat = in.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            din = AudioSystem.getAudioInputStream(decodedFormat, in);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            if (line != null) {
                line.open(decodedFormat);
                byte[] data = new byte[4096];
                line.start();
                int nBytesRead;
                while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                    if (isStopped) throw new SoundFinishedException();
                    line.write(data, 0, nBytesRead);
                }
                line.drain();
                line.stop();
                line.close();
                din.close();
            }
        } catch (SoundFinishedException sfe) {
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (din != null) {
                try {
                    din.close();
                } catch (IOException e) {
                }
            }
        }
    }
