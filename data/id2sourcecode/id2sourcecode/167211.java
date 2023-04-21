            public void run() {
                if (bufferArgs.length > 0) {
                    String bufferName = bufferArgs[0].getString();
                    if (MSPBuffer.getChannels(bufferName) != 0) {
                        if (MSPBuffer.getFrames(bufferName) <= Integer.MAX_VALUE) {
                            int channel = 0;
                            if (bufferArgs.length > 1) {
                                try {
                                    channel = bufferArgs[1].getInt();
                                    if (MSPBuffer.getChannels(bufferName) <= channel) {
                                        post("MP7 Encoder Error: illegal channel number, defaulting to channel zero");
                                    }
                                } catch (NumberFormatException e) {
                                    post("MP7 Encoder Error: malformed channel number, defaulting to channel zero");
                                }
                            }
                            bufferOperation = true;
                            if (lcd instanceof MaxBox) {
                                lcd.send("clear", null);
                            }
                            float[] buffer = new float[(int) MSPBuffer.getFrames(bufferName)];
                            buffer = MSPBuffer.peek(bufferName, 0);
                            byte[] bytes = new byte[(int) MSPBuffer.getFrames(bufferName) * 2];
                            for (int i = 0; i < buffer.length; i++) {
                                int data = 0;
                                if (buffer[i] < 0f) {
                                    if (buffer[i] > -1f) {
                                        data = (int) ((buffer[i]) * -1f * ((float) Integer.MIN_VALUE));
                                    } else {
                                        data = Integer.MIN_VALUE;
                                    }
                                } else {
                                    if (buffer[i] < 1f) {
                                        data = (int) ((buffer[i]) * ((float) Integer.MAX_VALUE));
                                    } else {
                                        data = Integer.MAX_VALUE;
                                    }
                                }
                                bytes[i * 2] = (byte) (data >>> 16);
                                bytes[i * 2 + 1] = (byte) (data >>> 24);
                            }
                            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
                            bufferSampleRate = (float) MSPBuffer.getFrames(bufferName) * 1000f / (float) MSPBuffer.getLength(bufferName);
                            AudioFormat af = new AudioFormat(bufferSampleRate, 16, 1, true, false);
                            ais = new AudioInputStream(bais, af, buffer.length);
                            configure();
                            mpeg7 = new Mpeg7(config, ais);
                            outletBang(getInfoIdx());
                            try {
                                ais.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            post("MP7 Encoder Error: Buffer " + bufferName + " too big to process\n");
                        }
                    } else {
                        post("MP7 Encoder Error: Buffer " + bufferName + " not found\n");
                    }
                } else {
                    post("MP7 Encoder Error: bufffer requires at least one argument\n");
                    post("Example 'buffer buffername bufferchannel filename'\n");
                    post("the second and third optional arguments are the channel number of the buffer you wish to analyze\n");
                    post("and the base file name you wish to assign to any samples you save from this buffer");
                }
            }
