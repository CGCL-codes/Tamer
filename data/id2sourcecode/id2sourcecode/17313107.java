        public void run() {
            boolean x = true;
            int transcriptionIndex = 0;
            while (x) {
                synchronized (isListening) {
                    try {
                        while (!isListening) {
                            isListening.wait();
                        }
                    } catch (InterruptedException ex) {
                        LOG.debug("capture thread interrupted");
                        return;
                    }
                }
                int byteBufferSize = buffer.getByteArrayBufferSize(targetDataLine.getFormat());
                rawBytes = new byte[byteBufferSize];
                LOG.info("starting capture with " + bufferSize + " buffer size and " + byteBufferSize + " byte buffer length");
                byteArrayOutputStream = new ByteArrayOutputStream();
                stopCapture = false;
                try {
                    while (!stopCapture) {
                        int cnt = targetDataLine.read(rawBytes, 0, rawBytes.length);
                        buffer.setSamplesFromBytes(rawBytes, 0, targetDataLine.getFormat(), 0, buffer.getSampleCount());
                        rms = level(buffer.getChannel(0));
                        if (rms > rmsThreshold) {
                            LOG.info("rms " + rms + " will begin recording ");
                            isCapturing = true;
                            captureStartTimeMS = System.currentTimeMillis();
                        }
                        if (cnt > 0 && isCapturing) {
                            byteArrayOutputStream.write(rawBytes, 0, cnt);
                        }
                        captureTimeMS = System.currentTimeMillis() - captureStartTimeMS;
                        if (isCapturing == true && captureTimeMS > captureTimeMinimumMS && rms < rmsThreshold) {
                            isCapturing = false;
                            stopCapture = true;
                        }
                    }
                    byteArrayOutputStream.flush();
                    byteArrayOutputStream.close();
                    ++transcriptionIndex;
                    saveWavAsFile(byteArrayOutputStream.toByteArray(), audioFormat, "googletts_" + transcriptionIndex + ".wav");
                    encoder.encode(new File("googletts_" + transcriptionIndex + ".wav"), new File("googletts_" + transcriptionIndex + ".flac"));
                    transcribe("googletts_" + transcriptionIndex + ".flac");
                    stopCapture = false;
                } catch (Exception e) {
                    LOG.error(Service.stackToString(e));
                }
            }
        }
