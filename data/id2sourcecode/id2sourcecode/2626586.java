    public void addFrame(BufferedImage imagedata, int x_offset, int y_offset, int delaytime, int disposalmethod) {
        if (!mbReadyToWrite) {
            int width = imagedata.getWidth();
            int height = imagedata.getHeight();
            int blendmethod;
            if (meMode == Emodes.ARGB) {
                blendmethod = ChunkfcTL.APNG_BLEND_OP_OVER;
            } else {
                blendmethod = ChunkfcTL.APNG_BLEND_OP_SOURCE;
            }
            maChunks.add(new ChunkfcTL(nextSequenceNumer(), width, height, x_offset, y_offset, delaytime, disposalmethod, blendmethod));
            byte[] image = {};
            if (meMode == Emodes.PALETTE) {
                int[] indices = imagedata.getRaster().getSamples(0, 0, width, height, 0, (int[]) null);
                image = new byte[indices.length + imagedata.getHeight()];
                int read = 0;
                for (int i = 0; i < image.length; i++) {
                    if (i % (width + 1) == 0) {
                        image[i] = 0;
                    } else {
                        image[i] = (byte) indices[read];
                        read++;
                    }
                }
            } else {
                int byte_per_pixel;
                if (meMode == Emodes.RGB) {
                    byte_per_pixel = 3;
                } else {
                    byte_per_pixel = 4;
                }
                image = new byte[width * height * byte_per_pixel + imagedata.getHeight()];
                int filterBytePos = byte_per_pixel * width + 1;
                int[] colorValues = imagedata.getRGB(0, 0, width, height, null, 0, width);
                int i = 0;
                for (int color : colorValues) {
                    if (i % (filterBytePos) == 0) {
                        image[i] = 0;
                        i++;
                    }
                    image[i + 2] = (byte) color;
                    color >>= 8;
                    image[i + 1] = (byte) color;
                    color >>= 8;
                    image[i] = (byte) color;
                    if (meMode == Emodes.ARGB) {
                        color >>= 8;
                        image[i + 3] = (byte) color;
                    }
                    i += byte_per_pixel;
                }
            }
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
            if (miNumFrames == 0) {
                maChunks.add(new ChunkIDAT(image));
            } else {
                maChunks.add(new ChunkfdAT(nextSequenceNumer(), image));
            }
            miNumFrames++;
        } else {
            throw new IllegalStateException("APNG is ready to write: no more frames can be added");
        }
    }
