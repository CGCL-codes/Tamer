    private Image readRLE4() throws IOException, BadElementException {
        int imSize = (int) imageSize;
        if (imSize == 0) {
            imSize = (int) (bitmapFileSize - bitmapOffset);
        }
        byte values[] = new byte[imSize];
        int bytesRead = 0;
        while (bytesRead < imSize) {
            bytesRead += inputStream.read(values, bytesRead, imSize - bytesRead);
        }
        byte val[] = decodeRLE(false, values);
        if (isBottomUp) {
            byte inverted[] = val;
            val = new byte[width * height];
            int l = 0, index, lineEnd;
            for (int i = height - 1; i >= 0; i--) {
                index = i * width;
                lineEnd = l + width;
                while (l != lineEnd) {
                    val[l++] = inverted[index++];
                }
            }
        }
        int stride = (width + 1) / 2;
        byte bdata[] = new byte[stride * height];
        int ptr = 0;
        int sh = 0;
        for (int h = 0; h < height; ++h) {
            for (int w = 0; w < width; ++w) {
                if ((w & 1) == 0) bdata[sh + w / 2] = (byte) (val[ptr++] << 4); else bdata[sh + w / 2] |= (byte) (val[ptr++] & 0x0f);
            }
            sh += stride;
        }
        return indexedModel(bdata, 4, 4);
    }
