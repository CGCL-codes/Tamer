    public void run() {
        imageOutputBuffer = new SAWByteArrayOutputStream();
        codec = new SAWFrameDifferenceCodecV4();
        codec.setMacroblockDataBuffer(imageOutputBuffer);
        while (!stopped) {
            try {
                synchronized (this) {
                    while (!stopped && !needRefresh) {
                        wait();
                    }
                }
                if (!stopped) {
                    if (currentDevice != nextDevice) {
                        currentDevice = nextDevice;
                        viewProvider.setGraphicsDevice(currentDevice);
                    }
                    if (!refreshInterrupted) {
                        if (clearRequested) {
                            if (imageDataBuffer != null) {
                                SAWImageIO.clearImage(imageDataBuffer);
                            }
                            lastWidth = -1;
                            lastHeight = -1;
                            lastDepth = -1;
                            lastDataType = -1;
                            clearRequested = false;
                        } else if (!screenCaptureModeComplete && captureArea != null && captureArea.width > 0 && captureArea.height > 0) {
                            imageDataBuffer = viewProvider.createScreenCapture(drawPointer, captureArea);
                        } else if (screenCaptureModeComplete) {
                            imageDataBuffer = viewProvider.createScreenCapture(drawPointer);
                        }
                        if (imageDataBuffer != null) {
                            if (imageDataBuffer.getWidth() == lastWidth && imageDataBuffer.getHeight() == lastHeight && imageDataBuffer.getColorModel().getPixelSize() == lastDepth && imageDataBuffer.getRaster().getDataBuffer().getDataType() == lastDataType) {
                                if (imageDataBuffer.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_BYTE) {
                                    if (!Arrays.equals(lastImageBufferByte, previousImageBufferByte)) {
                                        codec.makeFrameDifference(previousImageBufferByte, 0, lastImageBufferByte, 0, imageDataBuffer.getWidth(), imageDataBuffer.getHeight(), imageDataBuffer.getColorModel().getPixelSize(), dynamicCoding);
                                        sendDifference();
                                    } else {
                                        sendRefreshNotNeeded();
                                    }
                                } else if (imageDataBuffer.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_USHORT) {
                                    if (!Arrays.equals(lastImageBufferUShort, previousImageBufferUShort)) {
                                        codec.makeFrameDifference(previousImageBufferUShort, 0, lastImageBufferUShort, 0, imageDataBuffer.getWidth(), imageDataBuffer.getHeight(), imageDataBuffer.getColorModel().getPixelSize(), dynamicCoding);
                                        sendDifference();
                                    } else {
                                        sendRefreshNotNeeded();
                                    }
                                } else if (imageDataBuffer.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_INT) {
                                    if (!Arrays.equals(lastImageBufferInt, previousImageBufferInt)) {
                                        codec.makeFrameDifference(previousImageBufferInt, 0, lastImageBufferInt, 0, imageDataBuffer.getWidth(), imageDataBuffer.getHeight(), imageDataBuffer.getColorModel().getPixelSize(), dynamicCoding);
                                        sendDifference();
                                    } else {
                                        sendRefreshNotNeeded();
                                    }
                                }
                            } else {
                                lastWidth = imageDataBuffer.getWidth();
                                lastHeight = imageDataBuffer.getHeight();
                                lastDepth = imageDataBuffer.getColorModel().getPixelSize();
                                lastDataType = imageDataBuffer.getRaster().getDataBuffer().getDataType();
                                imageOutputBuffer.reset();
                                imageIO.write(imageDataBuffer, imageOutputBuffer);
                                sendRefresh();
                                imageOutputBuffer.reset();
                                if (imageDataBuffer.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_BYTE) {
                                    lastImageBufferByte = ((DataBufferByte) imageDataBuffer.getRaster().getDataBuffer()).getData();
                                    previousImageBufferByte = new byte[lastImageBufferByte.length];
                                    lastImageBufferUShort = null;
                                    previousImageBufferUShort = null;
                                    lastImageBufferInt = null;
                                    previousImageBufferInt = null;
                                    System.arraycopy(lastImageBufferByte, 0, previousImageBufferByte, 0, lastImageBufferByte.length);
                                } else if (imageDataBuffer.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_USHORT) {
                                    lastImageBufferByte = null;
                                    previousImageBufferByte = null;
                                    lastImageBufferUShort = ((DataBufferUShort) imageDataBuffer.getRaster().getDataBuffer()).getData();
                                    previousImageBufferUShort = new short[lastImageBufferUShort.length];
                                    lastImageBufferInt = null;
                                    previousImageBufferInt = null;
                                    System.arraycopy(lastImageBufferUShort, 0, previousImageBufferUShort, 0, lastImageBufferUShort.length);
                                } else if (imageDataBuffer.getRaster().getDataBuffer().getDataType() == DataBuffer.TYPE_INT) {
                                    lastImageBufferByte = null;
                                    previousImageBufferByte = null;
                                    lastImageBufferUShort = null;
                                    previousImageBufferUShort = null;
                                    lastImageBufferInt = ((DataBufferInt) imageDataBuffer.getRaster().getDataBuffer()).getData();
                                    previousImageBufferInt = new int[lastImageBufferInt.length];
                                    System.arraycopy(lastImageBufferInt, 0, previousImageBufferInt, 0, lastImageBufferInt.length);
                                }
                            }
                        }
                        synchronized (screenCaptureIntervalSynchronizer) {
                            if (screenCaptureInterval > 0) {
                                screenCaptureIntervalSynchronizer.wait(screenCaptureInterval);
                            }
                        }
                    } else {
                        Dimension screenSize = viewProvider.getCurrentScreenSize();
                        if (screenSize != null && screenSize.width != interruptedLastWidth || screenSize.height != interruptedLastHeight) {
                            interruptedLastWidth = screenSize.width;
                            interruptedLastHeight = screenSize.height;
                            sendRemoteInterfaceAreaChange(interruptedLastWidth, interruptedLastHeight);
                        }
                        synchronized (screenCaptureIntervalSynchronizer) {
                            screenCaptureIntervalSynchronizer.wait(125);
                        }
                    }
                }
            } catch (Exception e) {
                stopped = true;
                break;
            }
        }
        sendSessionEnding();
        synchronized (session) {
            session.notify();
        }
    }
