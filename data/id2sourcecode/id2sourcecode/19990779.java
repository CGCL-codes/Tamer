    private void read4Bit(byte[] bdata) throws IOException {
        int bytesPerScanline = (width + 1) / 2;
        int padding = bytesPerScanline % 4;
        if (padding != 0) padding = 4 - padding;
        int lineLength = bytesPerScanline + padding;
        if (noTransform) {
            int j = isBottomUp ? (height - 1) * bytesPerScanline : 0;
            for (int i = 0; i < height; i++) {
                if (abortRequested()) {
                    break;
                }
                iis.readFully(bdata, j, bytesPerScanline);
                iis.skipBytes(padding);
                j += isBottomUp ? -bytesPerScanline : bytesPerScanline;
                processImageUpdate(bi, 0, i, destinationRegion.width, 1, 1, 1, new int[] { 0 });
                processImageProgress(100.0F * i / destinationRegion.height);
            }
        } else {
            byte[] buf = new byte[lineLength];
            int lineStride = ((MultiPixelPackedSampleModel) sampleModel).getScanlineStride();
            if (isBottomUp) {
                int lastLine = sourceRegion.y + (destinationRegion.height - 1) * scaleY;
                iis.skipBytes(lineLength * (height - 1 - lastLine));
            } else iis.skipBytes(lineLength * sourceRegion.y);
            int skipLength = lineLength * (scaleY - 1);
            int[] srcOff = new int[destinationRegion.width];
            int[] destOff = new int[destinationRegion.width];
            int[] srcPos = new int[destinationRegion.width];
            int[] destPos = new int[destinationRegion.width];
            for (int i = destinationRegion.x, x = sourceRegion.x, j = 0; i < destinationRegion.x + destinationRegion.width; i++, j++, x += scaleX) {
                srcPos[j] = x >> 1;
                srcOff[j] = (1 - (x & 1)) << 2;
                destPos[j] = i >> 1;
                destOff[j] = (1 - (i & 1)) << 2;
            }
            int k = destinationRegion.y * lineStride;
            if (isBottomUp) k += (destinationRegion.height - 1) * lineStride;
            for (int j = 0, y = sourceRegion.y; j < destinationRegion.height; j++, y += scaleY) {
                if (abortRequested()) break;
                iis.read(buf, 0, lineLength);
                for (int i = 0; i < destinationRegion.width; i++) {
                    int v = (buf[srcPos[i]] >> srcOff[i]) & 0x0F;
                    bdata[k + destPos[i]] |= v << destOff[i];
                }
                k += isBottomUp ? -lineStride : lineStride;
                iis.skipBytes(skipLength);
                processImageUpdate(bi, 0, j, destinationRegion.width, 1, 1, 1, new int[] { 0 });
                processImageProgress(100.0F * j / destinationRegion.height);
            }
        }
    }
