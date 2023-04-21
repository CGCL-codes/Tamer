    public void run() {
        try {
            byte[] buf = new byte[BUFFER_SIZE];
            AACDecoder decoder = null;
            while (audioPlayerThread != null) {
                if (decoder == null) {
                    decoder = new AACDecoder(bitstream);
                }
                if (!audioDevice.isOpened()) {
                    audioDevice.open(decoder.getSampleFrequency(), decoder.getChannelCount());
                }
                if (!readyToPlay && audioDevice.isReady()) {
                    synchronized (this) {
                        readyToPlay = true;
                        notifyAll();
                    }
                }
                int bufSize = decoder.decodeFrame(buf);
                if (bufSize > 0) {
                    audioDevice.write(buf, bufSize);
                }
            }
        } catch (InterruptedIOException ioex) {
        } catch (EOFException ex) {
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            decoding = false;
            readyToPlay = true;
            audioPlayerThread = null;
        }
    }
