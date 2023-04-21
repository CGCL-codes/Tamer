    public SynthWrapper(ProjectContainer project, final MidiDevice midiDevice) {
        this.midiDevice = midiDevice;
        this.project = project;
        injectDependencies();
        if (midiDevice instanceof AudioSynthesizer) {
            try {
                AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
                AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 44100, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
                AudioSynthesizer audosynth = (AudioSynthesizer) midiDevice;
                final AudioInputStream ais = audosynth.openStream(format, null);
                System.out.println("PCM_FLOAT Encoding used!");
                synthVoice = new AudioProcess() {

                    byte[] streamBuffer = null;

                    float[] floatArray = null;

                    FloatBuffer floatBuffer = null;

                    public void close() {
                    }

                    public void open() {
                    }

                    public int processAudio(AudioBuffer buffer) {
                        if (buffer == null) return 0;
                        if (streamBuffer == null || streamBuffer.length != buffer.getSampleCount() * 8) {
                            ByteBuffer bytebuffer = ByteBuffer.allocate(buffer.getSampleCount() * 8).order(ByteOrder.nativeOrder());
                            streamBuffer = bytebuffer.array();
                            floatArray = new float[buffer.getSampleCount() * 2];
                            floatBuffer = bytebuffer.asFloatBuffer();
                        }
                        if (supress_audio) {
                            float[] left = buffer.getChannel(0);
                            float[] right = buffer.getChannel(1);
                            Arrays.fill(left, 0, buffer.getSampleCount(), 0);
                            Arrays.fill(right, 0, buffer.getSampleCount(), 0);
                        } else {
                            try {
                                ais.read(streamBuffer, 0, buffer.getSampleCount() * 8);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            floatBuffer.position(0);
                            floatBuffer.get(floatArray);
                            float[] left = buffer.getChannel(0);
                            float[] right = buffer.getChannel(1);
                            for (int n = 0; n < buffer.getSampleCount() * 2; n += 2) {
                                left[n / 2] = floatArray[n];
                                right[n / 2] = floatArray[n + 1];
                            }
                        }
                        AudioProcess r_proc = render_audioprocess;
                        if (r_proc != null) r_proc.processAudio(buffer);
                        return AUDIO_OK;
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else try {
            midiDevice.open();
        } catch (MidiUnavailableException e1) {
            e1.printStackTrace();
        }
        if (midiDevice instanceof Mixer) {
            try {
                if (midiDevice.isOpen()) {
                    System.err.println(midiDevice + " Already open");
                } else {
                    midiDevice.open();
                }
                final TargetDataLine line = (TargetDataLine) ((Mixer) midiDevice).getLine(new Line.Info(TargetDataLine.class));
                AudioFormat.Encoding PCM_FLOAT = new AudioFormat.Encoding("PCM_FLOAT");
                AudioFormat format = new AudioFormat(PCM_FLOAT, 44100, 32, 2, 4 * 2, 44100, ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN));
                line.open(format);
                System.out.println("PCM_FLOAT Encoding used!");
                synthVoice = new AudioProcess() {

                    byte[] streamBuffer = null;

                    float[] floatArray = null;

                    FloatBuffer floatBuffer = null;

                    public void close() {
                    }

                    public void open() {
                    }

                    public int processAudio(AudioBuffer buffer) {
                        if (buffer == null) return 0;
                        if (streamBuffer == null || streamBuffer.length != buffer.getSampleCount() * 8) {
                            ByteBuffer bytebuffer = ByteBuffer.allocate(buffer.getSampleCount() * 8).order(ByteOrder.nativeOrder());
                            streamBuffer = bytebuffer.array();
                            floatArray = new float[buffer.getSampleCount() * 2];
                            floatBuffer = bytebuffer.asFloatBuffer();
                        }
                        if (supress_audio) {
                            float[] left = buffer.getChannel(0);
                            float[] right = buffer.getChannel(1);
                            Arrays.fill(left, 0, buffer.getSampleCount(), 0);
                            Arrays.fill(right, 0, buffer.getSampleCount(), 0);
                        } else {
                            line.read(streamBuffer, 0, buffer.getSampleCount() * 8);
                            floatBuffer.position(0);
                            floatBuffer.get(floatArray);
                            float[] left = buffer.getChannel(0);
                            float[] right = buffer.getChannel(1);
                            for (int n = 0; n < buffer.getSampleCount() * 2; n += 2) {
                                left[n / 2] = floatArray[n];
                                right[n / 2] = floatArray[n + 1];
                            }
                        }
                        AudioProcess r_proc = render_audioprocess;
                        if (r_proc != null) r_proc.processAudio(buffer);
                        return AUDIO_OK;
                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
