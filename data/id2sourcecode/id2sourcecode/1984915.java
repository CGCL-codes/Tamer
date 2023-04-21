    public synchronized Raster getTile(int tileX, int tileY) {
        if ((tileX < 0) || (tileX >= tilesX) || (tileY < 0) || (tileY >= tilesY)) {
            throw new IllegalArgumentException("TIFFImage12");
        }
        byte bdata[] = null;
        short sdata[] = null;
        int idata[] = null;
        SampleModel sampleModel = getSampleModel();
        WritableRaster tile = makeTile(tileX, tileY);
        DataBuffer buffer = tile.getDataBuffer();
        int dataType = sampleModel.getDataType();
        if (dataType == DataBuffer.TYPE_BYTE) {
            bdata = ((DataBufferByte) buffer).getData();
        } else if (dataType == DataBuffer.TYPE_USHORT) {
            sdata = ((DataBufferUShort) buffer).getData();
        } else if (dataType == DataBuffer.TYPE_SHORT) {
            sdata = ((DataBufferShort) buffer).getData();
        } else if (dataType == DataBuffer.TYPE_INT) {
            idata = ((DataBufferInt) buffer).getData();
        }
        byte bswap;
        short sswap;
        int iswap;
        long save_offset = 0;
        try {
            save_offset = stream.getFilePointer();
            stream.seek(tileOffsets[tileY * tilesX + tileX]);
        } catch (IOException ioe) {
            throw new RuntimeException("TIFFImage13");
        }
        int byteCount = (int) tileByteCounts[tileY * tilesX + tileX];
        Rectangle newRect;
        if (!tiled) newRect = tile.getBounds(); else newRect = new Rectangle(tile.getMinX(), tile.getMinY(), tileWidth, tileHeight);
        int unitsInThisTile = newRect.width * newRect.height * numBands;
        byte data[] = compression != COMP_NONE || imageType == TYPE_PALETTE ? new byte[byteCount] : null;
        if (imageType == TYPE_BILEVEL) {
            try {
                if (compression == COMP_PACKBITS) {
                    stream.readFully(data, 0, byteCount);
                    int bytesInThisTile;
                    if ((newRect.width % 8) == 0) {
                        bytesInThisTile = (newRect.width / 8) * newRect.height;
                    } else {
                        bytesInThisTile = (newRect.width / 8 + 1) * newRect.height;
                    }
                    decodePackbits(data, bytesInThisTile, bdata);
                } else if (compression == COMP_LZW) {
                    stream.readFully(data, 0, byteCount);
                    lzwDecoder.decode(data, bdata, newRect.height);
                } else if (compression == COMP_FAX_G3_1D) {
                    stream.readFully(data, 0, byteCount);
                    decoder.decode1D(bdata, data, 0, newRect.height);
                } else if (compression == COMP_FAX_G3_2D) {
                    stream.readFully(data, 0, byteCount);
                    decoder.decode2D(bdata, data, 0, newRect.height, tiffT4Options);
                } else if (compression == COMP_FAX_G4_2D) {
                    stream.readFully(data, 0, byteCount);
                    decoder.decodeT6(bdata, data, 0, newRect.height, tiffT6Options);
                } else if (compression == COMP_DEFLATE) {
                    stream.readFully(data, 0, byteCount);
                    inflate(data, bdata);
                } else if (compression == COMP_NONE) {
                    stream.readFully(bdata, 0, byteCount);
                }
                stream.seek(save_offset);
            } catch (IOException ioe) {
                throw new RuntimeException("TIFFImage13");
            }
        } else if (imageType == TYPE_PALETTE) {
            if (sampleSize == 16) {
                if (decodePaletteAsShorts) {
                    short tempData[] = null;
                    int unitsBeforeLookup = unitsInThisTile / 3;
                    int entries = unitsBeforeLookup * 2;
                    try {
                        if (compression == COMP_PACKBITS) {
                            stream.readFully(data, 0, byteCount);
                            byte byteArray[] = new byte[entries];
                            decodePackbits(data, entries, byteArray);
                            tempData = new short[unitsBeforeLookup];
                            interpretBytesAsShorts(byteArray, tempData, unitsBeforeLookup);
                        } else if (compression == COMP_LZW) {
                            stream.readFully(data, 0, byteCount);
                            byte byteArray[] = new byte[entries];
                            lzwDecoder.decode(data, byteArray, newRect.height);
                            tempData = new short[unitsBeforeLookup];
                            interpretBytesAsShorts(byteArray, tempData, unitsBeforeLookup);
                        } else if (compression == COMP_DEFLATE) {
                            stream.readFully(data, 0, byteCount);
                            byte byteArray[] = new byte[entries];
                            inflate(data, byteArray);
                            tempData = new short[unitsBeforeLookup];
                            interpretBytesAsShorts(byteArray, tempData, unitsBeforeLookup);
                        } else if (compression == COMP_NONE) {
                            tempData = new short[byteCount / 2];
                            readShorts(byteCount / 2, tempData);
                        }
                        stream.seek(save_offset);
                    } catch (IOException ioe) {
                        throw new RuntimeException("TIFFImage13");
                    }
                    if (dataType == DataBuffer.TYPE_USHORT) {
                        int cmapValue;
                        int count = 0, lookup, len = colormap.length / 3;
                        int len2 = len * 2;
                        for (int i = 0; i < unitsBeforeLookup; i++) {
                            lookup = tempData[i] & 0xffff;
                            cmapValue = colormap[lookup + len2];
                            sdata[count++] = (short) (cmapValue & 0xffff);
                            cmapValue = colormap[lookup + len];
                            sdata[count++] = (short) (cmapValue & 0xffff);
                            cmapValue = colormap[lookup];
                            sdata[count++] = (short) (cmapValue & 0xffff);
                        }
                    } else if (dataType == DataBuffer.TYPE_SHORT) {
                        int cmapValue;
                        int count = 0, lookup, len = colormap.length / 3;
                        int len2 = len * 2;
                        for (int i = 0; i < unitsBeforeLookup; i++) {
                            lookup = tempData[i] & 0xffff;
                            cmapValue = colormap[lookup + len2];
                            sdata[count++] = (short) cmapValue;
                            cmapValue = colormap[lookup + len];
                            sdata[count++] = (short) cmapValue;
                            cmapValue = colormap[lookup];
                            sdata[count++] = (short) cmapValue;
                        }
                    }
                } else {
                    try {
                        if (compression == COMP_PACKBITS) {
                            stream.readFully(data, 0, byteCount);
                            int bytesInThisTile = unitsInThisTile * 2;
                            byte byteArray[] = new byte[bytesInThisTile];
                            decodePackbits(data, bytesInThisTile, byteArray);
                            interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                        } else if (compression == COMP_LZW) {
                            stream.readFully(data, 0, byteCount);
                            byte byteArray[] = new byte[unitsInThisTile * 2];
                            lzwDecoder.decode(data, byteArray, newRect.height);
                            interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                        } else if (compression == COMP_DEFLATE) {
                            stream.readFully(data, 0, byteCount);
                            byte byteArray[] = new byte[unitsInThisTile * 2];
                            inflate(data, byteArray);
                            interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                        } else if (compression == COMP_NONE) {
                            readShorts(byteCount / 2, sdata);
                        }
                        stream.seek(save_offset);
                    } catch (IOException ioe) {
                        throw new RuntimeException("TIFFImage13");
                    }
                }
            } else if (sampleSize == 8) {
                if (decodePaletteAsShorts) {
                    byte tempData[] = null;
                    int unitsBeforeLookup = unitsInThisTile / 3;
                    try {
                        if (compression == COMP_PACKBITS) {
                            stream.readFully(data, 0, byteCount);
                            tempData = new byte[unitsBeforeLookup];
                            decodePackbits(data, unitsBeforeLookup, tempData);
                        } else if (compression == COMP_LZW) {
                            stream.readFully(data, 0, byteCount);
                            tempData = new byte[unitsBeforeLookup];
                            lzwDecoder.decode(data, tempData, newRect.height);
                        } else if (compression == COMP_JPEG_TTN2) {
                            stream.readFully(data, 0, byteCount);
                            Raster tempTile = decodeJPEG(data, decodeParam, colorConvertJPEG, tile.getMinX(), tile.getMinY());
                            int[] tempPixels = new int[unitsBeforeLookup];
                            tempTile.getPixels(tile.getMinX(), tile.getMinY(), tile.getWidth(), tile.getHeight(), tempPixels);
                            tempData = new byte[unitsBeforeLookup];
                            for (int i = 0; i < unitsBeforeLookup; i++) {
                                tempData[i] = (byte) tempPixels[i];
                            }
                        } else if (compression == COMP_DEFLATE) {
                            stream.readFully(data, 0, byteCount);
                            tempData = new byte[unitsBeforeLookup];
                            inflate(data, tempData);
                        } else if (compression == COMP_NONE) {
                            tempData = new byte[byteCount];
                            stream.readFully(tempData, 0, byteCount);
                        }
                        stream.seek(save_offset);
                    } catch (IOException ioe) {
                        throw new RuntimeException("TIFFImage13");
                    }
                    int cmapValue;
                    int count = 0, lookup, len = colormap.length / 3;
                    int len2 = len * 2;
                    for (int i = 0; i < unitsBeforeLookup; i++) {
                        lookup = tempData[i] & 0xff;
                        cmapValue = colormap[lookup + len2];
                        sdata[count++] = (short) (cmapValue & 0xffff);
                        cmapValue = colormap[lookup + len];
                        sdata[count++] = (short) (cmapValue & 0xffff);
                        cmapValue = colormap[lookup];
                        sdata[count++] = (short) (cmapValue & 0xffff);
                    }
                } else {
                    try {
                        if (compression == COMP_PACKBITS) {
                            stream.readFully(data, 0, byteCount);
                            decodePackbits(data, unitsInThisTile, bdata);
                        } else if (compression == COMP_LZW) {
                            stream.readFully(data, 0, byteCount);
                            lzwDecoder.decode(data, bdata, newRect.height);
                        } else if (compression == COMP_JPEG_TTN2) {
                            stream.readFully(data, 0, byteCount);
                            tile.setRect(decodeJPEG(data, decodeParam, colorConvertJPEG, tile.getMinX(), tile.getMinY()));
                        } else if (compression == COMP_DEFLATE) {
                            stream.readFully(data, 0, byteCount);
                            inflate(data, bdata);
                        } else if (compression == COMP_NONE) {
                            stream.readFully(bdata, 0, byteCount);
                        }
                        stream.seek(save_offset);
                    } catch (IOException ioe) {
                        throw new RuntimeException("TIFFImage13");
                    }
                }
            } else if (sampleSize == 4) {
                int padding = (newRect.width % 2 == 0) ? 0 : 1;
                int bytesPostDecoding = ((newRect.width / 2 + padding) * newRect.height);
                if (decodePaletteAsShorts) {
                    byte tempData[] = null;
                    try {
                        stream.readFully(data, 0, byteCount);
                        stream.seek(save_offset);
                    } catch (IOException ioe) {
                        throw new RuntimeException("TIFFImage13");
                    }
                    if (compression == COMP_PACKBITS) {
                        tempData = new byte[bytesPostDecoding];
                        decodePackbits(data, bytesPostDecoding, tempData);
                    } else if (compression == COMP_LZW) {
                        tempData = new byte[bytesPostDecoding];
                        lzwDecoder.decode(data, tempData, newRect.height);
                    } else if (compression == COMP_DEFLATE) {
                        tempData = new byte[bytesPostDecoding];
                        inflate(data, tempData);
                    } else if (compression == COMP_NONE) {
                        tempData = data;
                    }
                    int bytes = unitsInThisTile / 3;
                    data = new byte[bytes];
                    int srcCount = 0, dstCount = 0;
                    for (int j = 0; j < newRect.height; j++) {
                        for (int i = 0; i < newRect.width / 2; i++) {
                            data[dstCount++] = (byte) ((tempData[srcCount] & 0xf0) >> 4);
                            data[dstCount++] = (byte) (tempData[srcCount++] & 0x0f);
                        }
                        if (padding == 1) {
                            data[dstCount++] = (byte) ((tempData[srcCount++] & 0xf0) >> 4);
                        }
                    }
                    int len = colormap.length / 3;
                    int len2 = len * 2;
                    int cmapValue, lookup;
                    int count = 0;
                    for (int i = 0; i < bytes; i++) {
                        lookup = data[i] & 0xff;
                        cmapValue = colormap[lookup + len2];
                        sdata[count++] = (short) (cmapValue & 0xffff);
                        cmapValue = colormap[lookup + len];
                        sdata[count++] = (short) (cmapValue & 0xffff);
                        cmapValue = colormap[lookup];
                        sdata[count++] = (short) (cmapValue & 0xffff);
                    }
                } else {
                    try {
                        if (compression == COMP_PACKBITS) {
                            stream.readFully(data, 0, byteCount);
                            decodePackbits(data, bytesPostDecoding, bdata);
                        } else if (compression == COMP_LZW) {
                            stream.readFully(data, 0, byteCount);
                            lzwDecoder.decode(data, bdata, newRect.height);
                        } else if (compression == COMP_DEFLATE) {
                            stream.readFully(data, 0, byteCount);
                            inflate(data, bdata);
                        } else if (compression == COMP_NONE) {
                            stream.readFully(bdata, 0, byteCount);
                        }
                        stream.seek(save_offset);
                    } catch (IOException ioe) {
                        throw new RuntimeException("TIFFImage13");
                    }
                }
            }
        } else if (imageType == TYPE_GRAY_4BIT) {
            try {
                if (compression == COMP_PACKBITS) {
                    stream.readFully(data, 0, byteCount);
                    int bytesInThisTile;
                    if ((newRect.width % 8) == 0) {
                        bytesInThisTile = (newRect.width / 2) * newRect.height;
                    } else {
                        bytesInThisTile = (newRect.width / 2 + 1) * newRect.height;
                    }
                    decodePackbits(data, bytesInThisTile, bdata);
                } else if (compression == COMP_LZW) {
                    stream.readFully(data, 0, byteCount);
                    lzwDecoder.decode(data, bdata, newRect.height);
                } else if (compression == COMP_DEFLATE) {
                    stream.readFully(data, 0, byteCount);
                    inflate(data, bdata);
                } else {
                    stream.readFully(bdata, 0, byteCount);
                }
                stream.seek(save_offset);
            } catch (IOException ioe) {
                throw new RuntimeException("TIFFImage13");
            }
        } else {
            try {
                if (sampleSize == 8) {
                    if (compression == COMP_NONE) {
                        stream.readFully(bdata, 0, byteCount);
                    } else if (compression == COMP_LZW) {
                        stream.readFully(data, 0, byteCount);
                        lzwDecoder.decode(data, bdata, newRect.height);
                    } else if (compression == COMP_PACKBITS) {
                        stream.readFully(data, 0, byteCount);
                        decodePackbits(data, unitsInThisTile, bdata);
                    } else if (compression == COMP_JPEG_TTN2) {
                        stream.readFully(data, 0, byteCount);
                        tile.setRect(decodeJPEG(data, decodeParam, colorConvertJPEG, tile.getMinX(), tile.getMinY()));
                    } else if (compression == COMP_DEFLATE) {
                        stream.readFully(data, 0, byteCount);
                        inflate(data, bdata);
                    }
                } else if (sampleSize == 16) {
                    if (compression == COMP_NONE) {
                        readShorts(byteCount / 2, sdata);
                    } else if (compression == COMP_LZW) {
                        stream.readFully(data, 0, byteCount);
                        byte byteArray[] = new byte[unitsInThisTile * 2];
                        lzwDecoder.decode(data, byteArray, newRect.height);
                        interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                    } else if (compression == COMP_PACKBITS) {
                        stream.readFully(data, 0, byteCount);
                        int bytesInThisTile = unitsInThisTile * 2;
                        byte byteArray[] = new byte[bytesInThisTile];
                        decodePackbits(data, bytesInThisTile, byteArray);
                        interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                    } else if (compression == COMP_DEFLATE) {
                        stream.readFully(data, 0, byteCount);
                        byte byteArray[] = new byte[unitsInThisTile * 2];
                        inflate(data, byteArray);
                        interpretBytesAsShorts(byteArray, sdata, unitsInThisTile);
                    }
                } else if (sampleSize == 32 && dataType == DataBuffer.TYPE_INT) {
                    if (compression == COMP_NONE) {
                        readInts(byteCount / 4, idata);
                    } else if (compression == COMP_LZW) {
                        stream.readFully(data, 0, byteCount);
                        byte byteArray[] = new byte[unitsInThisTile * 4];
                        lzwDecoder.decode(data, byteArray, newRect.height);
                        interpretBytesAsInts(byteArray, idata, unitsInThisTile);
                    } else if (compression == COMP_PACKBITS) {
                        stream.readFully(data, 0, byteCount);
                        int bytesInThisTile = unitsInThisTile * 4;
                        byte byteArray[] = new byte[bytesInThisTile];
                        decodePackbits(data, bytesInThisTile, byteArray);
                        interpretBytesAsInts(byteArray, idata, unitsInThisTile);
                    } else if (compression == COMP_DEFLATE) {
                        stream.readFully(data, 0, byteCount);
                        byte byteArray[] = new byte[unitsInThisTile * 4];
                        inflate(data, byteArray);
                        interpretBytesAsInts(byteArray, idata, unitsInThisTile);
                    }
                }
                stream.seek(save_offset);
            } catch (IOException ioe) {
                throw new RuntimeException("TIFFImage13");
            }
            switch(imageType) {
                case TYPE_GRAY:
                case TYPE_GRAY_ALPHA:
                    if (isWhiteZero) {
                        if (dataType == DataBuffer.TYPE_BYTE && !(getColorModel() instanceof IndexColorModel)) {
                            for (int l = 0; l < bdata.length; l += numBands) {
                                bdata[l] = (byte) (255 - bdata[l]);
                            }
                        } else if (dataType == DataBuffer.TYPE_USHORT) {
                            int ushortMax = Short.MAX_VALUE - Short.MIN_VALUE;
                            for (int l = 0; l < sdata.length; l += numBands) {
                                sdata[l] = (short) (ushortMax - sdata[l]);
                            }
                        } else if (dataType == DataBuffer.TYPE_SHORT) {
                            for (int l = 0; l < sdata.length; l += numBands) {
                                sdata[l] = (short) (~sdata[l]);
                            }
                        } else if (dataType == DataBuffer.TYPE_INT) {
                            long uintMax = ((long) Integer.MAX_VALUE - (long) Integer.MIN_VALUE);
                            for (int l = 0; l < idata.length; l += numBands) {
                                idata[l] = (int) (uintMax - idata[l]);
                            }
                        }
                    }
                    break;
                case TYPE_RGB:
                    if (sampleSize == 8 && compression != COMP_JPEG_TTN2) {
                        for (int i = 0; i < unitsInThisTile; i += 3) {
                            bswap = bdata[i];
                            bdata[i] = bdata[i + 2];
                            bdata[i + 2] = bswap;
                        }
                    } else if (sampleSize == 16) {
                        for (int i = 0; i < unitsInThisTile; i += 3) {
                            sswap = sdata[i];
                            sdata[i] = sdata[i + 2];
                            sdata[i + 2] = sswap;
                        }
                    } else if (sampleSize == 32) {
                        if (dataType == DataBuffer.TYPE_INT) {
                            for (int i = 0; i < unitsInThisTile; i += 3) {
                                iswap = idata[i];
                                idata[i] = idata[i + 2];
                                idata[i + 2] = iswap;
                            }
                        }
                    }
                    break;
                case TYPE_RGB_ALPHA:
                    if (sampleSize == 8) {
                        for (int i = 0; i < unitsInThisTile; i += 4) {
                            bswap = bdata[i];
                            bdata[i] = bdata[i + 3];
                            bdata[i + 3] = bswap;
                            bswap = bdata[i + 1];
                            bdata[i + 1] = bdata[i + 2];
                            bdata[i + 2] = bswap;
                        }
                    } else if (sampleSize == 16) {
                        for (int i = 0; i < unitsInThisTile; i += 4) {
                            sswap = sdata[i];
                            sdata[i] = sdata[i + 3];
                            sdata[i + 3] = sswap;
                            sswap = sdata[i + 1];
                            sdata[i + 1] = sdata[i + 2];
                            sdata[i + 2] = sswap;
                        }
                    } else if (sampleSize == 32) {
                        if (dataType == DataBuffer.TYPE_INT) {
                            for (int i = 0; i < unitsInThisTile; i += 4) {
                                iswap = idata[i];
                                idata[i] = idata[i + 3];
                                idata[i + 3] = iswap;
                                iswap = idata[i + 1];
                                idata[i + 1] = idata[i + 2];
                                idata[i + 2] = iswap;
                            }
                        }
                    }
                    break;
                case TYPE_YCBCR_SUB:
                    int pixelsPerDataUnit = chromaSubH * chromaSubV;
                    int numH = newRect.width / chromaSubH;
                    int numV = newRect.height / chromaSubV;
                    byte[] tempData = new byte[numH * numV * (pixelsPerDataUnit + 2)];
                    System.arraycopy(bdata, 0, tempData, 0, tempData.length);
                    int samplesPerDataUnit = pixelsPerDataUnit * 3;
                    int[] pixels = new int[samplesPerDataUnit];
                    int bOffset = 0;
                    int offsetCb = pixelsPerDataUnit;
                    int offsetCr = offsetCb + 1;
                    int y = newRect.y;
                    for (int j = 0; j < numV; j++) {
                        int x = newRect.x;
                        for (int i = 0; i < numH; i++) {
                            int Cb = tempData[bOffset + offsetCb];
                            int Cr = tempData[bOffset + offsetCr];
                            int k = 0;
                            while (k < samplesPerDataUnit) {
                                pixels[k++] = tempData[bOffset++];
                                pixels[k++] = Cb;
                                pixels[k++] = Cr;
                            }
                            bOffset += 2;
                            tile.setPixels(x, y, chromaSubH, chromaSubV, pixels);
                            x += chromaSubH;
                        }
                        y += chromaSubV;
                    }
                    break;
            }
        }
        return tile;
    }
