    public void open() {
        receiver = new Receiver() {

            public void send(MidiMessage message, long timeStamp) {
                if (targetDataLine.isOpen() && ShortMessage.class.isInstance(message)) {
                    ShortMessage shm = (ShortMessage) message;
                    int channelNo = shm.getChannel();
                    MidiChannel channel = channelNo >= 0 && channelNo <= 15 ? midiChannels[channelNo] : null;
                    switch(shm.getCommand()) {
                        case ShortMessage.PROGRAM_CHANGE:
                            channel.programChange(shm.getData1());
                            break;
                        case ShortMessage.NOTE_ON:
                            channel.noteOn(shm.getData1(), shm.getData2());
                            break;
                        case ShortMessage.NOTE_OFF:
                            channel.noteOff(shm.getData1());
                            break;
                        case ShortMessage.PITCH_BEND:
                            channel.setPitchBend((0xff & shm.getData1()) + ((0xff & shm.getData2()) << 7));
                            break;
                    }
                }
            }

            public void close() {
            }
        };
        targetDataLine = new TargetDataLine() {

            ByteBuffer buf = null;

            float[] floatBuffer = null;

            FloatBuffer flView = null;

            boolean isOpen = false;

            public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
                PJSynth.this.format = format;
                buf = ByteBuffer.allocate(bufferSize).order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                flView = buf.asFloatBuffer();
                floatBuffer = new float[flView.capacity()];
                for (int n = 0; n < midiChannels.length; n++) midiChannels[n] = new PJSynthMidiChannel(PJSynth.this, flView.capacity());
                isOpen = true;
            }

            public void open(AudioFormat format) throws LineUnavailableException {
                open(format, 16384);
            }

            public int read(byte[] b, int off, int len) {
                if (!isOpen) return len;
                int bytesLeft = len;
                while (bytesLeft > 0) {
                    int templen = bytesLeft > buf.capacity() ? buf.capacity() : bytesLeft;
                    for (int n = 0; n < floatBuffer.length; n++) floatBuffer[n] = 0;
                    fillBuffer(floatBuffer, templen / format.getFrameSize(), format.getChannels());
                    flView.position(0);
                    flView.put(floatBuffer);
                    buf.position(0);
                    buf.get(b, off, templen);
                    off += buf.capacity();
                    bytesLeft -= templen;
                }
                return len;
            }

            public void drain() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void flush() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void start() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void stop() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isRunning() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isActive() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public AudioFormat getFormat() {
                return format;
            }

            public int getBufferSize() {
                return buf.limit();
            }

            public int available() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getFramePosition() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public long getLongFramePosition() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public long getMicrosecondPosition() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public float getLevel() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Info getLineInfo() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void open() throws LineUnavailableException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void close() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isOpen() {
                return isOpen;
            }

            public Control[] getControls() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isControlSupported(Type control) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Control getControl(Type control) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void addLineListener(LineListener listener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void removeLineListener(LineListener listener) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        isOpen = true;
    }
