    public Line getLine(Line.Info info) throws LineUnavailableException {
        return new TargetDataLine() {

            ByteBuffer buf = null;

            float[] floatBuffer = null;

            FloatBuffer flView = null;

            boolean isOpen = false;

            AudioFormat format;

            public void open(AudioFormat format, int bufferSize) throws LineUnavailableException {
                this.format = format;
                buf = ByteBuffer.allocate(bufferSize).order(format.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
                flView = buf.asFloatBuffer();
                floatBuffer = new float[flView.capacity()];
                try {
                    if (vst == null) {
                        if (vstiFile == null) {
                            JFileChooser fileChooser = new JFileChooser();
                            fileChooser.showDialog(null, "Select VSTi");
                            vstiFile = fileChooser.getSelectedFile();
                        }
                        vst = JVstHost2.newInstance(vstiFile, format.getSampleRate(), flView.capacity());
                        if (programChunk != null) vst.setProgramChunk(programChunk);
                        vst.addJVstHostListener(new AbstractJVstHostListener() {

                            @Override
                            public void onAudioMasterAutomate(JVstHost2 vst, int index, float value) {
                                if (gui != null) {
                                    gui.updateParameter(index, value);
                                }
                            }
                        });
                        System.out.println("vst blocksize: " + vst.getBlockSize());
                        if (vst.hasEditor()) {
                            vst.openEditor("VST GUI");
                        } else {
                            gui = new StringGui(vst);
                            gui.setVisible(true);
                        }
                        fInputs = new float[vst.numInputs()][flView.capacity() / format.getChannels()];
                        fOutputs = new float[vst.numOutputs()][flView.capacity() / format.getChannels()];
                        isOpen = true;
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(FrinikaJVSTSynth.class.getName()).log(Level.SEVERE, null, ex);
                } catch (JVstLoadException ex) {
                    Logger.getLogger(FrinikaJVSTSynth.class.getName()).log(Level.SEVERE, null, ex);
                }
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
                throw new UnsupportedOperationException("Not supported yet.");
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
    }
