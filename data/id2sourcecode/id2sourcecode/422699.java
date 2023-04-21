    public void suona(String filename) throws IOException {
        AudioInputStream ain = null;
        SourceDataLine line = null;
        try {
            ain = AudioSystem.getAudioInputStream(getClass().getResource(filename));
            AudioFormat format = ain.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            if (!AudioSystem.isLineSupported(info)) {
                AudioFormat pcm = new AudioFormat(format.getSampleRate(), 16, format.getChannels(), true, false);
                ain = AudioSystem.getAudioInputStream(pcm, ain);
                format = ain.getFormat();
                info = new DataLine.Info(SourceDataLine.class, format);
            }
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            int framesize = format.getFrameSize();
            byte[] buffer = new byte[4 * 1024 * framesize];
            int numbytes = 0;
            boolean started = false;
            for (; ; ) {
                int bytesread = ain.read(buffer, numbytes, buffer.length - numbytes);
                if (bytesread == -1) break;
                numbytes += bytesread;
                if (!started) {
                    line.start();
                    started = true;
                }
                int bytestowrite = (numbytes / framesize) * framesize;
                line.write(buffer, 0, bytestowrite);
                int remaining = numbytes - bytestowrite;
                if (remaining > 0) System.arraycopy(buffer, bytestowrite, buffer, 0, remaining);
                numbytes = remaining;
            }
            line.drain();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (line != null) line.close();
            if (ain != null) ain.close();
        }
    }
