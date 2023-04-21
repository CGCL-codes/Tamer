    public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
        LineEvent event = null;
        if (bufferSize < format.getFrameSize() * 32) bufferSize = format.getFrameSize() * 32;
        synchronized (control_mutex) {
            if (!isOpen()) {
                if (!mixer.isOpen()) {
                    mixer.open();
                    mixer.implicitOpen = true;
                }
                event = new LineEvent(this, LineEvent.Type.OPEN, 0);
                this.bufferSize = bufferSize - bufferSize % format.getFrameSize();
                this.format = format;
                this.framesize = format.getFrameSize();
                this.outputformat = mixer.getFormat();
                out_nrofchannels = outputformat.getChannels();
                in_nrofchannels = format.getChannels();
                open = true;
                mixer.getMainMixer().openLine(this);
                cycling_buffer = new byte[framesize * bufferSize];
                cycling_read_pos = 0;
                cycling_write_pos = 0;
                cycling_avail = 0;
                cycling_framepos = 0;
                InputStream cycling_inputstream = new InputStream() {

                    public int read() throws IOException {
                        byte[] b = new byte[1];
                        int ret = read(b);
                        if (ret < 0) return ret;
                        return b[0] & 0xFF;
                    }

                    public int available() throws IOException {
                        synchronized (cycling_buffer) {
                            return cycling_avail;
                        }
                    }

                    public int read(byte[] b, int off, int len) throws IOException {
                        synchronized (cycling_buffer) {
                            if (len > cycling_avail) len = cycling_avail;
                            int pos = cycling_read_pos;
                            byte[] buff = cycling_buffer;
                            int buff_len = buff.length;
                            for (int i = 0; i < len; i++) {
                                b[off++] = buff[pos];
                                pos++;
                                if (pos == buff_len) pos = 0;
                            }
                            cycling_read_pos = pos;
                            cycling_avail -= len;
                            cycling_framepos += len / framesize;
                        }
                        return len;
                    }
                };
                afis = AudioFloatInputStream.getInputStream(new AudioInputStream(cycling_inputstream, format, AudioSystem.NOT_SPECIFIED));
                afis = new NonBlockingFloatInputStream(afis);
                if (Math.abs(format.getSampleRate() - outputformat.getSampleRate()) > 0.000001) afis = new AudioFloatInputStreamResampler(afis, outputformat);
            } else {
                if (!format.matches(getFormat())) {
                    throw new IllegalStateException("Line is already open with format " + getFormat() + " and bufferSize " + getBufferSize());
                }
            }
        }
        if (event != null) sendEvent(event);
    }
