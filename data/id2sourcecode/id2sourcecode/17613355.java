        public void run() {
            try {
                stream = AudioSystem.getAudioInputStream(new File(_file));
                format = stream.getFormat();
                log.debug("Audio format: " + format);
                if ((format.getEncoding() == AudioFormat.Encoding.ULAW) || (format.getEncoding() == AudioFormat.Encoding.ALAW)) {
                    AudioFormat newFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), format.getSampleSizeInBits() * 2, format.getChannels(), format.getFrameSize() * 2, format.getFrameRate(), true);
                    stream = AudioSystem.getAudioInputStream(newFormat, stream);
                    System.out.println("Converted Audio format: " + newFormat);
                    format = newFormat;
                    log.debug("new converted Audio format: " + format);
                }
            } catch (UnsupportedAudioFileException e) {
                log.error("AudioFileException " + e.getMessage());
                return;
            } catch (IOException e) {
                log.error("IOException " + e.getMessage());
                return;
            }
            if (!streamingStop) {
                try {
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
                    if (!AudioSystem.isLineSupported(info)) {
                        log.error("Audio play() does not support: " + format);
                        return;
                    }
                    line = (SourceDataLine) AudioSystem.getLine(info);
                    line.open(format);
                } catch (Exception e) {
                    log.error("Exception while creating Audio out " + e.getMessage());
                    return;
                }
            }
            if (streamingStop) {
                line.close();
                return;
            }
            int numRead = 0;
            byte[] buffer = new byte[line.getBufferSize()];
            log.debug("streaming sound buffer size = " + line.getBufferSize());
            line.start();
            try {
                int offset;
                while ((numRead = stream.read(buffer, 0, buffer.length)) >= 0) {
                    offset = 0;
                    while (offset < numRead) offset += line.write(buffer, offset, numRead - offset);
                }
            } catch (IOException e) {
                log.error("IOException while reading sound file " + e.getMessage());
            }
            line.drain();
            line.stop();
            line.close();
        }
