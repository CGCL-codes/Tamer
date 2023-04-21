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
