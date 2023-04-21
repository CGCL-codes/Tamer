    public byte[] openBytes(int no) throws FormatException, IOException {
        FormatTools.assertId(currentId, true, 1);
        FormatTools.checkPlaneNumber(this, no);
        LayerInfo info = (LayerInfo) layerInfoList[series].get(no);
        in.seek(info.layerStart);
        readTagHeader();
        if ((tag != 67 && tag != 68) || (!fmt.equals("PICT") && !fmt.equals("RAWi"))) {
            throw new FormatException("Corrupt LIFF file.");
        }
        in.skipBytes(24);
        int volumeType = in.readShort();
        in.skipBytes(272);
        int top, left, bottom, right;
        if (version == 2) {
            in.skipBytes(2);
            top = in.readShort();
            left = in.readShort();
            bottom = in.readShort();
            right = in.readShort();
            if (core.sizeX[series] == 0) core.sizeX[series] = right - left;
            if (core.sizeY[series] == 0) core.sizeY[series] = bottom - top;
        } else {
            core.sizeX[series] = in.readInt();
            core.sizeY[series] = in.readInt();
        }
        in.seek(info.layerStart);
        byte[] b = new byte[0];
        if (version == 2) {
            long nextTag = readTagHeader();
            if ((tag != 67 && tag != 68) || !fmt.equals("PICT")) {
                throw new FormatException("Corrupt LIFF file.");
            }
            in.skipBytes(298);
            Exception exception = null;
            try {
                b = new byte[(int) (nextTag - in.getFilePointer())];
                in.read(b);
                BufferedImage img = pict.open(b);
                byte[][] tmp = ImageTools.getBytes(img);
                b = new byte[tmp.length * tmp[0].length];
                int pt = 0;
                for (int i = 0; i < tmp[0].length; i++) {
                    for (int j = 0; j < tmp.length; j++) {
                        b[pt++] = tmp[j][i];
                    }
                }
            } catch (FormatException exc) {
                exception = exc;
            } catch (IOException exc) {
                exception = exc;
            }
            if (exception != null) {
                if (debug) trace(exception);
                b = null;
                in.seek(info.layerStart + 12);
                int blockSize = DataTools.read4SignedBytes(in, false);
                byte toRead = (byte) in.read();
                if (toRead == 1) in.skipBytes(128);
                in.skipBytes(169);
                byte[] q = new byte[blockSize];
                in.read(q);
                byte[] pixelData = new byte[blockSize];
                int pixPos = 0;
                int length = q.length;
                int num, size;
                int totalBlocks = -1;
                int expectedBlock = 0;
                int pos = 0;
                while (expectedBlock != totalBlocks) {
                    while (pos + 7 < length && (q[pos] != 73 || q[pos + 1] != 86 || q[pos + 2] != 69 || q[pos + 3] != 65 || q[pos + 4] != 100 || q[pos + 5] != 98 || q[pos + 6] != 112 || q[pos + 7] != 113)) {
                        pos++;
                    }
                    pos += 8;
                    num = DataTools.bytesToInt(q, pos, 4, false);
                    if (num != expectedBlock) {
                        throw new FormatException("Expected iPic block not found");
                    }
                    expectedBlock++;
                    if (totalBlocks == -1) {
                        totalBlocks = DataTools.bytesToInt(q, pos + 4, 4, false);
                    } else {
                        if (DataTools.bytesToInt(q, pos + 4, 4, false) != totalBlocks) {
                            throw new FormatException("Unexpected totalBlocks numbein.read");
                        }
                    }
                    pos += 16;
                    size = DataTools.bytesToInt(q, pos, 4, false);
                    pos += 8;
                    System.arraycopy(q, pos, pixelData, pixPos, size);
                    pixPos += size;
                }
                System.gc();
                b = new byte[pixPos];
                System.arraycopy(pixelData, 0, b, 0, b.length);
            }
        } else {
            readTagHeader();
            if (tag != 68 || !fmt.equals("RAWi")) {
                throw new FormatException("Corrupt LIFF file.");
            }
            if (subTag != 0) {
                throw new FormatException("Wrong compression type.");
            }
            in.skipBytes(24);
            volumeType = in.readShort();
            in.skipBytes(280);
            int size = in.readInt();
            int compressedSize = in.readInt();
            b = new byte[size];
            byte[] c = new byte[compressedSize];
            in.read(c);
            LZOCodec lzoc = new LZOCodec();
            b = lzoc.decompress(c);
            if (b.length != size) {
                LogTools.println("LZOCodec failed to predict image size");
                LogTools.println(size + " expected, got " + b.length + ". The image displayed may not be correct.");
            }
            if (volumeType == MAC_24_BIT) {
                bytesPerPixel = b.length >= core.sizeX[series] * core.sizeY[series] * 4 ? 4 : 3;
                int destRowBytes = core.sizeX[series] * bytesPerPixel;
                int srcRowBytes = b.length / core.sizeY[series];
                byte[] tmp = new byte[destRowBytes * core.sizeY[series]];
                int src = 0;
                int dest = 0;
                for (int y = 0; y < core.sizeY[series]; y++) {
                    System.arraycopy(b, src, tmp, dest, destRowBytes);
                    src += srcRowBytes;
                    dest += destRowBytes;
                }
                if (bytesPerPixel == 4) {
                    b = new byte[(3 * tmp.length) / 4];
                    dest = 0;
                    for (int i = 0; i < tmp.length; i += 4) {
                        b[dest] = tmp[i + 1];
                        b[dest + (b.length / 3)] = tmp[i + 2];
                        b[dest + ((2 * b.length) / 3)] = tmp[i + 3];
                        dest++;
                    }
                    bytesPerPixel = 3;
                }
            } else if (volumeType == MAC_256_GREYS) {
                byte[] tmp = b;
                b = new byte[core.sizeX[series] * core.sizeY[series]];
                for (int y = 0; y < core.sizeY[series]; y++) {
                    System.arraycopy(tmp, y * (core.sizeX[series] + 16), b, y * core.sizeX[series], core.sizeX[series]);
                }
            } else if (volumeType < MAC_24_BIT) {
                throw new FormatException("Unsupported image type : " + volumeType);
            }
        }
        int bpp = b.length / (core.sizeX[series] * core.sizeY[series]);
        int expected = core.sizeX[series] * core.sizeY[series] * bpp;
        if (b.length > expected) {
            byte[] tmp = b;
            b = new byte[expected];
            System.arraycopy(tmp, 0, b, 0, b.length);
        }
        return b;
    }
