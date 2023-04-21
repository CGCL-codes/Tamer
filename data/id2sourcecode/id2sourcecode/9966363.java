    public IplImage nextFrameImage(int objectMouseX, int objectMouseY, boolean mouseClick) throws Exception {
        IplImage frameImage = imageToProject;
        if (desktopScreen != null && robot != null) {
            int w = videoToProject != null ? videoToProject.getImageWidth() : imageToProject.width();
            int h = videoToProject != null ? videoToProject.getImageHeight() : imageToProject.height();
            final int desktopMouseX = objectMouseX * w / (textureImage != null ? textureImage.width() : projector.imageWidth);
            final int desktopMouseY = objectMouseY * h / (textureImage != null ? textureImage.height() : projector.imageHeight);
            if (desktopMouseX >= 0 && desktopMouseY >= 0) {
                robot.mouseMove(desktopMouseX, desktopMouseY);
                if (mouseClick) {
                    robot.mousePress(InputEvent.BUTTON1_MASK);
                    robot.waitForIdle();
                    robot.mouseRelease(InputEvent.BUTTON1_MASK);
                    robot.waitForIdle();
                }
            }
            if (videoToProject != null) {
                frameImage = videoToProject.grab();
            } else {
                final int dstStep = imageToProject.widthStep();
                final int dstChannels = imageToProject.nChannels();
                final ByteBuffer dstBuf = imageToProject.getByteBuffer();
                final IntBuffer dstBufInt = dstChannels == 4 ? dstBuf.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer() : null;
                final int dstWidth = imageToProject.width();
                final int dstHeight = imageToProject.height();
                BufferedImage screenCapture = robot.createScreenCapture(new Rectangle(dstWidth, dstHeight));
                if (desktopMouseX >= 0 && desktopMouseY >= 0) {
                    Graphics2D g = screenCapture.createGraphics();
                    g.drawImage(handMouseCursor, desktopMouseX, desktopMouseY, null);
                }
                final int srcStep = ((SinglePixelPackedSampleModel) screenCapture.getSampleModel()).getScanlineStride();
                final int[] srcData = ((DataBufferInt) screenCapture.getRaster().getDataBuffer()).getData();
                Parallel.loop(0, dstHeight, new Parallel.Looper() {

                    public void loop(int from, int to, int looperID) {
                        for (int y = from; y < to; y++) {
                            int srcPixel = y * srcStep;
                            int dstPixel = y * dstStep;
                            for (int x = 0; x < dstWidth; x++, srcPixel++, dstPixel += dstChannels) {
                                int rgb = srcData[srcPixel];
                                switch(dstChannels) {
                                    case 1:
                                        int lumi = (IplImage.decodeGamma22((rgb >> 16) & 0xFF) + IplImage.decodeGamma22((rgb >> 8) & 0xFF) + IplImage.decodeGamma22((rgb) & 0xFF)) / 3;
                                        dstBuf.put(dstPixel, (byte) lumi);
                                        break;
                                    case 3:
                                        dstBuf.put(dstPixel + 0, (byte) IplImage.decodeGamma22((rgb) & 0xFF));
                                        dstBuf.put(dstPixel + 1, (byte) IplImage.decodeGamma22((rgb >> 8) & 0xFF));
                                        dstBuf.put(dstPixel + 2, (byte) IplImage.decodeGamma22((rgb >> 16) & 0xFF));
                                        break;
                                    case 4:
                                        int rgba = (IplImage.decodeGamma22((rgb >> 16) & 0xFF)) | (IplImage.decodeGamma22((rgb >> 8) & 0xFF) << 8) | (IplImage.decodeGamma22((rgb) & 0xFF) << 16) | (0xFF << 24);
                                        dstBufInt.put(dstPixel / 4, rgba);
                                        break;
                                    default:
                                        assert false;
                                }
                            }
                        }
                    }
                });
            }
        } else if (videoToProject != null) {
            frameImage = videoToProject.grab();
            if (frameImage == null) {
                videoToProject.restart();
                frameImage = videoToProject.grab();
            }
            if (imageToProject == null) {
            } else {
                int w = Math.min(imageToProject.width(), frameImage.width());
                int h = Math.min(imageToProject.height(), frameImage.height());
                IplROI srcRoi = imageToProject.roi();
                final int srcStep = imageToProject.widthStep(), dstStep = frameImage.widthStep();
                final int srcChannels = imageToProject.nChannels(), dstChannels = frameImage.nChannels();
                int srcIndex = 0, dstIndex = 0;
                if (srcRoi != null) {
                    srcIndex = srcRoi.yOffset() * srcStep + srcRoi.xOffset() * srcChannels;
                    dstIndex = srcRoi.yOffset() * dstStep + srcRoi.xOffset() * dstChannels;
                    w = srcRoi.width();
                    h = srcRoi.height();
                }
                final ByteBuffer srcBuf = imageToProject.getByteBuffer(srcIndex);
                final ByteBuffer dstBuf = frameImage.getByteBuffer(dstIndex);
                final IntBuffer srcBufInt = srcChannels == 4 ? srcBuf.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer() : null;
                final IntBuffer dstBufInt = dstChannels == 4 ? dstBuf.order(ByteOrder.LITTLE_ENDIAN).asIntBuffer() : null;
                final int width = w;
                final int height = h;
                Parallel.loop(0, height, new Parallel.Looper() {

                    public void loop(int from, int to, int looperID) {
                        for (int y = from; y < to; y++) {
                            int srcPixel = y * srcStep;
                            int dstPixel = y * dstStep;
                            for (int x = 0; x < width; x++, srcPixel += srcChannels, dstPixel += dstChannels) {
                                int r = 0, g = 0, b = 0, a = 128;
                                switch(srcChannels) {
                                    case 1:
                                        r = g = b = srcBuf.get(srcPixel) & 0xFF;
                                        break;
                                    case 3:
                                        b = srcBuf.get(srcPixel) & 0xFF;
                                        g = srcBuf.get(srcPixel + 1) & 0xFF;
                                        r = srcBuf.get(srcPixel + 2) & 0xFF;
                                        break;
                                    case 4:
                                        int rgba = srcBufInt.get(srcPixel / 4);
                                        r = (rgba) & 0xFF;
                                        g = (rgba >> 8) & 0xFF;
                                        b = (rgba >> 16) & 0xFF;
                                        a = (rgba >> 24) & 0xFF;
                                        break;
                                    default:
                                        assert false;
                                }
                                switch(dstChannels) {
                                    case 1:
                                        int lumi = (r + g + b) / 3;
                                        dstBuf.put(dstPixel, (byte) ((lumi * a + IplImage.decodeGamma22(dstBuf.get(dstPixel)) * (255 - a)) / 255));
                                        break;
                                    case 3:
                                        dstBuf.put(dstPixel, (byte) ((b * a + IplImage.decodeGamma22(dstBuf.get(dstPixel)) * (255 - a)) / 255));
                                        dstBuf.put(dstPixel + 1, (byte) ((g * a + IplImage.decodeGamma22(dstBuf.get(dstPixel + 1)) * (255 - a)) / 255));
                                        dstBuf.put(dstPixel + 2, (byte) ((r * a + IplImage.decodeGamma22(dstBuf.get(dstPixel + 2)) * (255 - a)) / 255));
                                        break;
                                    case 4:
                                        int rgba = dstBufInt.get(dstPixel / 4);
                                        r = (r * a + IplImage.decodeGamma22((rgba) & 0xFF) * (255 - a)) / 255;
                                        g = (g * a + IplImage.decodeGamma22((rgba >> 8) & 0xFF) * (255 - a)) / 255;
                                        b = (b * a + IplImage.decodeGamma22((rgba >> 16) & 0xFF) * (255 - a)) / 255;
                                        a = 0xFF;
                                        rgba = r | (g << 8) | (b << 16) | (a << 24);
                                        dstBufInt.put(dstPixel / 4, rgba);
                                        break;
                                    default:
                                        assert false;
                                }
                            }
                        }
                    }
                });
            }
        }
        Rectangle r = virtualSettings.chronometerBounds;
        if (r == null || r.width <= 0 || r.height <= 0) {
            chronometer = null;
        } else if (chronometer == null) {
            chronometer = new Chronometer(r, frameImage.getBufferedImageType());
        }
        if (chronometer != null) {
            chronometer.draw(frameImage);
        }
        return frameImage;
    }
