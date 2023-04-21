    public byte[] getImageData() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            for (int i = 0; i < mNumberOfChunks; i++) {
                PNGChunk chunk = mChunks[i];
                if (chunk.getTypeString().equals("IDAT")) {
                    out.write(chunk.getData());
                }
            }
            out.flush();
            InflaterInputStream in = new InflaterInputStream(new ByteArrayInputStream(out.toByteArray()));
            ByteArrayOutputStream inflatedOut = new ByteArrayOutputStream();
            int readLength;
            byte[] block = new byte[8192];
            while ((readLength = in.read(block)) != -1) inflatedOut.write(block, 0, readLength);
            inflatedOut.flush();
            byte[] imageData = inflatedOut.toByteArray();
            int width = (int) getWidth();
            int height = (int) getHeight();
            int bitsPerPixel = getBitsPerPixel();
            int length = width * height * bitsPerPixel / 8;
            byte[] prunedData = new byte[length];
            if (getInterlace() == 0) {
                int index = 0;
                for (int i = 0; i < length; i++) {
                    if ((i * 8 / bitsPerPixel) % width == 0) {
                        index++;
                    }
                    prunedData[i] = imageData[index++];
                }
            } else System.out.println("Couldn't undo interlacing.");
            return prunedData;
        } catch (IOException ioe) {
        }
        return null;
    }
