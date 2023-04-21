    private int encode(RenderedImage im, TIFFEncodeParam encodeParam, int ifdOffset, boolean isLast) throws IOException {
        int compression = encodeParam.getCompression();
        boolean isTiled = encodeParam.getWriteTiled();
        int minX = im.getMinX();
        int minY = im.getMinY();
        int width = im.getWidth();
        int height = im.getHeight();
        SampleModel sampleModel = im.getSampleModel();
        int sampleSize[] = sampleModel.getSampleSize();
        for (int i = 1; i < sampleSize.length; i++) {
            if (sampleSize[i] != sampleSize[0]) {
                throw new Error(JaiI18N.getString("TIFFImageEncoder0"));
            }
        }
        int numBands = sampleModel.getNumBands();
        if ((sampleSize[0] == 1 || sampleSize[0] == 4) && numBands != 1) {
            throw new Error(JaiI18N.getString("TIFFImageEncoder1"));
        }
        int dataType = sampleModel.getDataType();
        switch(dataType) {
            case DataBuffer.TYPE_BYTE:
                if (sampleSize[0] != 1 && sampleSize[0] == 4 && sampleSize[0] != 8) {
                    throw new Error(JaiI18N.getString("TIFFImageEncoder2"));
                }
                break;
            case DataBuffer.TYPE_SHORT:
            case DataBuffer.TYPE_USHORT:
                if (sampleSize[0] != 16) {
                    throw new Error(JaiI18N.getString("TIFFImageEncoder3"));
                }
                break;
            case DataBuffer.TYPE_INT:
            case DataBuffer.TYPE_FLOAT:
                if (sampleSize[0] != 32) {
                    throw new Error(JaiI18N.getString("TIFFImageEncoder4"));
                }
                break;
            default:
                throw new Error(JaiI18N.getString("TIFFImageEncoder5"));
        }
        boolean dataTypeIsShort = dataType == DataBuffer.TYPE_SHORT || dataType == DataBuffer.TYPE_USHORT;
        ColorModel colorModel = im.getColorModel();
        if (colorModel != null && colorModel instanceof IndexColorModel && dataType != DataBuffer.TYPE_BYTE) {
            throw new Error(JaiI18N.getString("TIFFImageEncoder6"));
        }
        IndexColorModel icm = null;
        int sizeOfColormap = 0;
        int colormap[] = null;
        int imageType = TIFF_UNSUPPORTED;
        int numExtraSamples = 0;
        int extraSampleType = EXTRA_SAMPLE_UNSPECIFIED;
        if (colorModel instanceof IndexColorModel) {
            icm = (IndexColorModel) colorModel;
            int mapSize = icm.getMapSize();
            if (sampleSize[0] == 1 && numBands == 1) {
                if (mapSize != 2) {
                    throw new IllegalArgumentException(JaiI18N.getString("TIFFImageEncoder7"));
                }
                byte r[] = new byte[mapSize];
                icm.getReds(r);
                byte g[] = new byte[mapSize];
                icm.getGreens(g);
                byte b[] = new byte[mapSize];
                icm.getBlues(b);
                if ((r[0] & 0xff) == 0 && (r[1] & 0xff) == 255 && (g[0] & 0xff) == 0 && (g[1] & 0xff) == 255 && (b[0] & 0xff) == 0 && (b[1] & 0xff) == 255) {
                    imageType = TIFF_BILEVEL_BLACK_IS_ZERO;
                } else if ((r[0] & 0xff) == 255 && (r[1] & 0xff) == 0 && (g[0] & 0xff) == 255 && (g[1] & 0xff) == 0 && (b[0] & 0xff) == 255 && (b[1] & 0xff) == 0) {
                    imageType = TIFF_BILEVEL_WHITE_IS_ZERO;
                } else {
                    imageType = TIFF_PALETTE;
                }
            } else if (numBands == 1) {
                imageType = TIFF_PALETTE;
            }
        } else if (colorModel == null) {
            if (sampleSize[0] == 1 && numBands == 1) {
                imageType = TIFF_BILEVEL_BLACK_IS_ZERO;
            } else {
                imageType = TIFF_GENERIC;
                if (numBands > 1) {
                    numExtraSamples = numBands - 1;
                }
            }
        } else {
            ColorSpace colorSpace = colorModel.getColorSpace();
            switch(colorSpace.getType()) {
                case ColorSpace.TYPE_CMYK:
                    imageType = TIFF_CMYK;
                    break;
                case ColorSpace.TYPE_GRAY:
                    imageType = TIFF_GRAY;
                    break;
                case ColorSpace.TYPE_Lab:
                    imageType = TIFF_CIELAB;
                    break;
                case ColorSpace.TYPE_RGB:
                    if (compression == COMP_JPEG_TTN2 && encodeParam.getJPEGCompressRGBToYCbCr()) {
                        imageType = TIFF_YCBCR;
                    } else {
                        imageType = TIFF_RGB;
                    }
                    break;
                case ColorSpace.TYPE_YCbCr:
                    imageType = TIFF_YCBCR;
                    break;
                default:
                    imageType = TIFF_GENERIC;
                    break;
            }
            if (imageType == TIFF_GENERIC) {
                numExtraSamples = numBands - 1;
            } else if (numBands > 1) {
                numExtraSamples = numBands - colorSpace.getNumComponents();
            }
            if (numExtraSamples == 1 && colorModel.hasAlpha()) {
                extraSampleType = colorModel.isAlphaPremultiplied() ? EXTRA_SAMPLE_ASSOCIATED_ALPHA : EXTRA_SAMPLE_UNASSOCIATED_ALPHA;
            }
        }
        if (imageType == TIFF_UNSUPPORTED) {
            throw new Error(JaiI18N.getString("TIFFImageEncoder8"));
        }
        if (compression == COMP_JPEG_TTN2) {
            if (imageType == TIFF_PALETTE) {
                throw new Error(JaiI18N.getString("TIFFImageEncoder11"));
            } else if (!(sampleSize[0] == 8 && (imageType == TIFF_GRAY || imageType == TIFF_RGB || imageType == TIFF_YCBCR))) {
                throw new Error(JaiI18N.getString("TIFFImageEncoder9"));
            }
        }
        int photometricInterpretation = -1;
        switch(imageType) {
            case TIFF_BILEVEL_WHITE_IS_ZERO:
                photometricInterpretation = 0;
                break;
            case TIFF_BILEVEL_BLACK_IS_ZERO:
                photometricInterpretation = 1;
                break;
            case TIFF_GRAY:
            case TIFF_GENERIC:
                photometricInterpretation = 1;
                break;
            case TIFF_PALETTE:
                photometricInterpretation = 3;
                icm = (IndexColorModel) colorModel;
                sizeOfColormap = icm.getMapSize();
                byte r[] = new byte[sizeOfColormap];
                icm.getReds(r);
                byte g[] = new byte[sizeOfColormap];
                icm.getGreens(g);
                byte b[] = new byte[sizeOfColormap];
                icm.getBlues(b);
                int redIndex = 0, greenIndex = sizeOfColormap;
                int blueIndex = 2 * sizeOfColormap;
                colormap = new int[sizeOfColormap * 3];
                for (int i = 0; i < sizeOfColormap; i++) {
                    colormap[redIndex++] = (r[i] << 8) & 0xffff;
                    colormap[greenIndex++] = (g[i] << 8) & 0xffff;
                    colormap[blueIndex++] = (b[i] << 8) & 0xffff;
                }
                sizeOfColormap *= 3;
                break;
            case TIFF_RGB:
                photometricInterpretation = 2;
                break;
            case TIFF_CMYK:
                photometricInterpretation = 5;
                break;
            case TIFF_YCBCR:
                photometricInterpretation = 6;
                break;
            case TIFF_CIELAB:
                photometricInterpretation = 8;
                break;
            default:
                throw new Error(JaiI18N.getString("TIFFImageEncoder8"));
        }
        int tileWidth;
        int tileHeight;
        if (isTiled) {
            tileWidth = encodeParam.getTileWidth() > 0 ? encodeParam.getTileWidth() : im.getTileWidth();
            tileHeight = encodeParam.getTileHeight() > 0 ? encodeParam.getTileHeight() : im.getTileHeight();
        } else {
            tileWidth = width;
            tileHeight = encodeParam.getTileHeight() > 0 ? encodeParam.getTileHeight() : DEFAULT_ROWS_PER_STRIP;
        }
        JPEGEncodeParam jep = null;
        if (compression == COMP_JPEG_TTN2) {
            jep = encodeParam.getJPEGEncodeParam();
            int maxSubH = jep.getHorizontalSubsampling(0);
            int maxSubV = jep.getVerticalSubsampling(0);
            for (int i = 1; i < numBands; i++) {
                int subH = jep.getHorizontalSubsampling(i);
                if (subH > maxSubH) {
                    maxSubH = subH;
                }
                int subV = jep.getVerticalSubsampling(i);
                if (subV > maxSubV) {
                    maxSubV = subV;
                }
            }
            int factorV = 8 * maxSubV;
            tileHeight = (int) ((float) tileHeight / (float) factorV + 0.5F) * factorV;
            if (tileHeight < factorV) {
                tileHeight = factorV;
            }
            if (isTiled) {
                int factorH = 8 * maxSubH;
                tileWidth = (int) ((float) tileWidth / (float) factorH + 0.5F) * factorH;
                if (tileWidth < factorH) {
                    tileWidth = factorH;
                }
            }
        }
        int numTiles;
        if (isTiled) {
            numTiles = ((width + tileWidth - 1) / tileWidth) * ((height + tileHeight - 1) / tileHeight);
        } else {
            numTiles = (int) Math.ceil((double) height / (double) tileHeight);
        }
        long tileByteCounts[] = new long[numTiles];
        long bytesPerRow = (long) Math.ceil((sampleSize[0] / 8.0) * tileWidth * numBands);
        long bytesPerTile = bytesPerRow * tileHeight;
        for (int i = 0; i < numTiles; i++) {
            tileByteCounts[i] = bytesPerTile;
        }
        if (!isTiled) {
            long lastStripRows = height - (tileHeight * (numTiles - 1));
            tileByteCounts[numTiles - 1] = lastStripRows * bytesPerRow;
        }
        long totalBytesOfData = bytesPerTile * (numTiles - 1) + tileByteCounts[numTiles - 1];
        long tileOffsets[] = new long[numTiles];
        SortedSet fields = new TreeSet();
        fields.add(new TIFFField(TIFFImageDecoder.TIFF_IMAGE_WIDTH, TIFFField.TIFF_LONG, 1, (Object) (new long[] { (long) width })));
        fields.add(new TIFFField(TIFFImageDecoder.TIFF_IMAGE_LENGTH, TIFFField.TIFF_LONG, 1, new long[] { (long) height }));
        fields.add(new TIFFField(TIFFImageDecoder.TIFF_BITS_PER_SAMPLE, TIFFField.TIFF_SHORT, numBands, intsToChars(sampleSize)));
        fields.add(new TIFFField(TIFFImageDecoder.TIFF_COMPRESSION, TIFFField.TIFF_SHORT, 1, new char[] { (char) compression }));
        fields.add(new TIFFField(TIFFImageDecoder.TIFF_PHOTOMETRIC_INTERPRETATION, TIFFField.TIFF_SHORT, 1, new char[] { (char) photometricInterpretation }));
        if (!isTiled) {
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_STRIP_OFFSETS, TIFFField.TIFF_LONG, numTiles, (long[]) tileOffsets));
        }
        fields.add(new TIFFField(TIFFImageDecoder.TIFF_SAMPLES_PER_PIXEL, TIFFField.TIFF_SHORT, 1, new char[] { (char) numBands }));
        if (!isTiled) {
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_ROWS_PER_STRIP, TIFFField.TIFF_LONG, 1, new long[] { (long) tileHeight }));
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_STRIP_BYTE_COUNTS, TIFFField.TIFF_LONG, numTiles, (long[]) tileByteCounts));
        }
        if (colormap != null) {
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_COLORMAP, TIFFField.TIFF_SHORT, sizeOfColormap, intsToChars(colormap)));
        }
        if (isTiled) {
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_TILE_WIDTH, TIFFField.TIFF_LONG, 1, new long[] { (long) tileWidth }));
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_TILE_LENGTH, TIFFField.TIFF_LONG, 1, new long[] { (long) tileHeight }));
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_TILE_OFFSETS, TIFFField.TIFF_LONG, numTiles, (long[]) tileOffsets));
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_TILE_BYTE_COUNTS, TIFFField.TIFF_LONG, numTiles, (long[]) tileByteCounts));
        }
        if (numExtraSamples > 0) {
            int[] extraSamples = new int[numExtraSamples];
            for (int i = 0; i < numExtraSamples; i++) {
                extraSamples[i] = extraSampleType;
            }
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_EXTRA_SAMPLES, TIFFField.TIFF_SHORT, numExtraSamples, intsToChars(extraSamples)));
        }
        if (dataType != DataBuffer.TYPE_BYTE) {
            int[] sampleFormat = new int[numBands];
            if (dataType == DataBuffer.TYPE_FLOAT) {
                sampleFormat[0] = 3;
            } else if (dataType == DataBuffer.TYPE_USHORT) {
                sampleFormat[0] = 1;
            } else {
                sampleFormat[0] = 2;
            }
            for (int b = 1; b < numBands; b++) {
                sampleFormat[b] = sampleFormat[0];
            }
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_SAMPLE_FORMAT, TIFFField.TIFF_SHORT, numBands, intsToChars(sampleFormat)));
        }
        boolean inverseFill = encodeParam.getReverseFillOrder();
        boolean T4encode2D = encodeParam.getT4Encode2D();
        boolean T4PadEOLs = encodeParam.getT4PadEOLs();
        TIFFFaxEncoder faxEncoder = null;
        if ((imageType == TIFF_BILEVEL_BLACK_IS_ZERO || imageType == TIFF_BILEVEL_WHITE_IS_ZERO) && (compression == COMP_GROUP3_1D || compression == COMP_GROUP3_2D || compression == COMP_GROUP4)) {
            faxEncoder = new TIFFFaxEncoder(inverseFill);
            fields.add(new TIFFField(TIFFImageDecoder.TIFF_FILL_ORDER, TIFFField.TIFF_SHORT, 1, new char[] { inverseFill ? (char) 2 : (char) 1 }));
            if (compression == COMP_GROUP3_2D) {
                long T4Options = 0x00000000;
                if (T4encode2D) {
                    T4Options |= 0x00000001;
                }
                if (T4PadEOLs) {
                    T4Options |= 0x00000004;
                }
                fields.add(new TIFFField(TIFFImageDecoder.TIFF_T4_OPTIONS, TIFFField.TIFF_LONG, 1, new long[] { T4Options }));
            } else if (compression == COMP_GROUP4) {
                fields.add(new TIFFField(TIFFImageDecoder.TIFF_T6_OPTIONS, TIFFField.TIFF_LONG, 1, new long[] { (long) 0x00000000 }));
            }
        }
        com.sun.image.codec.jpeg.JPEGEncodeParam jpegEncodeParam = null;
        com.sun.image.codec.jpeg.JPEGImageEncoder jpegEncoder = null;
        int jpegColorID = 0;
        if (compression == COMP_JPEG_TTN2) {
            jpegColorID = com.sun.image.codec.jpeg.JPEGDecodeParam.COLOR_ID_UNKNOWN;
            switch(imageType) {
                case TIFF_GRAY:
                case TIFF_PALETTE:
                    jpegColorID = com.sun.image.codec.jpeg.JPEGDecodeParam.COLOR_ID_GRAY;
                    break;
                case TIFF_RGB:
                    jpegColorID = com.sun.image.codec.jpeg.JPEGDecodeParam.COLOR_ID_RGB;
                    break;
                case TIFF_YCBCR:
                    jpegColorID = com.sun.image.codec.jpeg.JPEGDecodeParam.COLOR_ID_YCbCr;
                    break;
            }
            Raster tile00 = im.getTile(0, 0);
            jpegEncodeParam = com.sun.image.codec.jpeg.JPEGCodec.getDefaultJPEGEncodeParam(tile00, jpegColorID);
            JPEGImageEncoder.modifyEncodeParam(jep, jpegEncodeParam, numBands);
            if (jep.getWriteImageOnly()) {
                jpegEncodeParam.setImageInfoValid(false);
                jpegEncodeParam.setTableInfoValid(true);
                ByteArrayOutputStream tableStream = new ByteArrayOutputStream();
                jpegEncoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(tableStream, jpegEncodeParam);
                jpegEncoder.encode(tile00);
                byte[] tableData = tableStream.toByteArray();
                fields.add(new TIFFField(TIFF_JPEG_TABLES, TIFFField.TIFF_UNDEFINED, tableData.length, tableData));
                jpegEncoder = null;
            }
        }
        if (imageType == TIFF_YCBCR) {
            int subsampleH = 1;
            int subsampleV = 1;
            if (compression == COMP_JPEG_TTN2) {
                subsampleH = jep.getHorizontalSubsampling(0);
                subsampleV = jep.getVerticalSubsampling(0);
                for (int i = 1; i < numBands; i++) {
                    int subH = jep.getHorizontalSubsampling(i);
                    if (subH > subsampleH) {
                        subsampleH = subH;
                    }
                    int subV = jep.getVerticalSubsampling(i);
                    if (subV > subsampleV) {
                        subsampleV = subV;
                    }
                }
            }
            fields.add(new TIFFField(TIFF_YCBCR_SUBSAMPLING, TIFFField.TIFF_SHORT, 2, new char[] { (char) subsampleH, (char) subsampleV }));
            fields.add(new TIFFField(TIFF_YCBCR_POSITIONING, TIFFField.TIFF_SHORT, 1, new char[] { compression == COMP_JPEG_TTN2 ? (char) 1 : (char) 2 }));
            long[][] refbw;
            if (compression == COMP_JPEG_TTN2) {
                refbw = new long[][] { { 0, 1 }, { 255, 1 }, { 128, 1 }, { 255, 1 }, { 128, 1 }, { 255, 1 } };
            } else {
                refbw = new long[][] { { 15, 1 }, { 235, 1 }, { 128, 1 }, { 240, 1 }, { 128, 1 }, { 240, 1 } };
            }
            fields.add(new TIFFField(TIFF_REF_BLACK_WHITE, TIFFField.TIFF_RATIONAL, 6, refbw));
        }
        TIFFField[] extraFields = encodeParam.getExtraFields();
        if (extraFields != null) {
            ArrayList extantTags = new ArrayList(fields.size());
            Iterator fieldIter = fields.iterator();
            while (fieldIter.hasNext()) {
                TIFFField fld = (TIFFField) fieldIter.next();
                extantTags.add(new Integer(fld.getTag()));
            }
            int numExtraFields = extraFields.length;
            for (int i = 0; i < numExtraFields; i++) {
                TIFFField fld = extraFields[i];
                Integer tagValue = new Integer(fld.getTag());
                if (!extantTags.contains(tagValue)) {
                    fields.add(fld);
                    extantTags.add(tagValue);
                }
            }
        }
        int dirSize = getDirectorySize(fields);
        tileOffsets[0] = ifdOffset + dirSize;
        OutputStream outCache = null;
        byte[] compressBuf = null;
        File tempFile = null;
        int nextIFDOffset = 0;
        boolean skipByte = false;
        Deflater deflater = null;
        int deflateLevel = Deflater.DEFAULT_COMPRESSION;
        boolean jpegRGBToYCbCr = false;
        if (compression == COMP_NONE) {
            int numBytesPadding = 0;
            if (sampleSize[0] == 16 && tileOffsets[0] % 2 != 0) {
                numBytesPadding = 1;
                tileOffsets[0]++;
            } else if (sampleSize[0] == 32 && tileOffsets[0] % 4 != 0) {
                numBytesPadding = (int) (4 - tileOffsets[0] % 4);
                tileOffsets[0] += numBytesPadding;
            }
            for (int i = 1; i < numTiles; i++) {
                tileOffsets[i] = tileOffsets[i - 1] + tileByteCounts[i - 1];
            }
            if (!isLast) {
                nextIFDOffset = (int) (tileOffsets[0] + totalBytesOfData);
                if (nextIFDOffset % 2 != 0) {
                    nextIFDOffset++;
                    skipByte = true;
                }
            }
            writeDirectory(ifdOffset, fields, nextIFDOffset);
            if (numBytesPadding != 0) {
                for (int padding = 0; padding < numBytesPadding; padding++) {
                    output.write((byte) 0);
                }
            }
        } else {
            if ((output instanceof SeekableOutputStream)) {
                ((SeekableOutputStream) output).seek(tileOffsets[0]);
            } else {
                outCache = output;
                try {
                    tempFile = File.createTempFile("jai-SOS-", ".tmp");
                    tempFile.deleteOnExit();
                    RandomAccessFile raFile = new RandomAccessFile(tempFile, "rw");
                    output = new SeekableOutputStream(raFile);
                } catch (Exception e) {
                    output = new ByteArrayOutputStream((int) totalBytesOfData);
                }
            }
            int bufSize = 0;
            switch(compression) {
                case COMP_GROUP3_1D:
                    bufSize = (int) Math.ceil((((tileWidth + 1) / 2) * 9 + 2) / 8.0);
                    break;
                case COMP_GROUP3_2D:
                case COMP_GROUP4:
                    bufSize = (int) Math.ceil((((tileWidth + 1) / 2) * 9 + 2) / 8.0);
                    bufSize = tileHeight * (bufSize + 2) + 12;
                    break;
                case COMP_PACKBITS:
                    bufSize = (int) (bytesPerTile + ((bytesPerRow + 127) / 128) * tileHeight);
                    break;
                case COMP_JPEG_TTN2:
                    bufSize = 0;
                    if (imageType == TIFF_YCBCR && colorModel != null && colorModel.getColorSpace().getType() == ColorSpace.TYPE_RGB) {
                        jpegRGBToYCbCr = true;
                    }
                    break;
                case COMP_DEFLATE:
                    bufSize = (int) bytesPerTile;
                    deflater = new Deflater(encodeParam.getDeflateLevel());
                    break;
                default:
                    bufSize = 0;
            }
            if (bufSize != 0) {
                compressBuf = new byte[bufSize];
            }
        }
        int[] pixels = null;
        float[] fpixels = null;
        boolean checkContiguous = ((sampleSize[0] == 1 && sampleModel instanceof MultiPixelPackedSampleModel && dataType == DataBuffer.TYPE_BYTE) || (sampleSize[0] == 8 && sampleModel instanceof ComponentSampleModel));
        byte[] bpixels = null;
        if (compression != COMP_JPEG_TTN2) {
            if (dataType == DataBuffer.TYPE_BYTE) {
                bpixels = new byte[tileHeight * tileWidth * numBands];
            } else if (dataTypeIsShort) {
                bpixels = new byte[2 * tileHeight * tileWidth * numBands];
            } else if (dataType == DataBuffer.TYPE_INT || dataType == DataBuffer.TYPE_FLOAT) {
                bpixels = new byte[4 * tileHeight * tileWidth * numBands];
            }
        }
        int lastRow = minY + height;
        int lastCol = minX + width;
        int tileNum = 0;
        for (int row = minY; row < lastRow; row += tileHeight) {
            int rows = isTiled ? tileHeight : Math.min(tileHeight, lastRow - row);
            int size = rows * tileWidth * numBands;
            for (int col = minX; col < lastCol; col += tileWidth) {
                Raster src = im.getData(new Rectangle(col, row, tileWidth, rows));
                boolean useDataBuffer = false;
                if (compression != COMP_JPEG_TTN2) {
                    if (checkContiguous) {
                        if (sampleSize[0] == 8) {
                            ComponentSampleModel csm = (ComponentSampleModel) src.getSampleModel();
                            int[] bankIndices = csm.getBankIndices();
                            int[] bandOffsets = csm.getBandOffsets();
                            int pixelStride = csm.getPixelStride();
                            int lineStride = csm.getScanlineStride();
                            if (pixelStride != numBands || lineStride != bytesPerRow) {
                                useDataBuffer = false;
                            } else {
                                useDataBuffer = true;
                                for (int i = 0; useDataBuffer && i < numBands; i++) {
                                    if (bankIndices[i] != 0 || bandOffsets[i] != i) {
                                        useDataBuffer = false;
                                    }
                                }
                            }
                        } else {
                            MultiPixelPackedSampleModel mpp = (MultiPixelPackedSampleModel) src.getSampleModel();
                            if (mpp.getNumBands() == 1 && mpp.getDataBitOffset() == 0 && mpp.getPixelBitStride() == 1) {
                                useDataBuffer = true;
                            }
                        }
                    }
                    if (!useDataBuffer) {
                        if (dataType == DataBuffer.TYPE_FLOAT) {
                            fpixels = src.getPixels(col, row, tileWidth, rows, fpixels);
                        } else {
                            pixels = src.getPixels(col, row, tileWidth, rows, pixels);
                        }
                    }
                }
                int index;
                int pixel = 0;
                ;
                int k = 0;
                switch(sampleSize[0]) {
                    case 1:
                        if (useDataBuffer) {
                            byte[] btmp = ((DataBufferByte) src.getDataBuffer()).getData();
                            MultiPixelPackedSampleModel mpp = (MultiPixelPackedSampleModel) src.getSampleModel();
                            int lineStride = mpp.getScanlineStride();
                            int inOffset = mpp.getOffset(col - src.getSampleModelTranslateX(), row - src.getSampleModelTranslateY());
                            if (lineStride == (int) bytesPerRow) {
                                System.arraycopy(btmp, inOffset, bpixels, 0, (int) bytesPerRow * rows);
                            } else {
                                int outOffset = 0;
                                for (int j = 0; j < rows; j++) {
                                    System.arraycopy(btmp, inOffset, bpixels, outOffset, (int) bytesPerRow);
                                    inOffset += lineStride;
                                    outOffset += (int) bytesPerRow;
                                }
                            }
                        } else {
                            index = 0;
                            for (int i = 0; i < rows; i++) {
                                for (int j = 0; j < tileWidth / 8; j++) {
                                    pixel = (pixels[index++] << 7) | (pixels[index++] << 6) | (pixels[index++] << 5) | (pixels[index++] << 4) | (pixels[index++] << 3) | (pixels[index++] << 2) | (pixels[index++] << 1) | pixels[index++];
                                    bpixels[k++] = (byte) pixel;
                                }
                                if (tileWidth % 8 > 0) {
                                    pixel = 0;
                                    for (int j = 0; j < tileWidth % 8; j++) {
                                        pixel |= (pixels[index++] << (7 - j));
                                    }
                                    bpixels[k++] = (byte) pixel;
                                }
                            }
                        }
                        if (compression == COMP_NONE) {
                            output.write(bpixels, 0, rows * ((tileWidth + 7) / 8));
                        } else if (compression == COMP_GROUP3_1D) {
                            int rowStride = (tileWidth + 7) / 8;
                            int rowOffset = 0;
                            int numCompressedBytes = 0;
                            for (int tileRow = 0; tileRow < rows; tileRow++) {
                                int numCompressedBytesInRow = faxEncoder.encodeRLE(bpixels, rowOffset, 0, tileWidth, compressBuf);
                                output.write(compressBuf, 0, numCompressedBytesInRow);
                                rowOffset += rowStride;
                                numCompressedBytes += numCompressedBytesInRow;
                            }
                            tileByteCounts[tileNum++] = numCompressedBytes;
                        } else if (compression == COMP_GROUP3_2D) {
                            int numCompressedBytes = faxEncoder.encodeT4(!T4encode2D, T4PadEOLs, bpixels, (tileWidth + 7) / 8, 0, tileWidth, rows, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_GROUP4) {
                            int numCompressedBytes = faxEncoder.encodeT6(bpixels, (tileWidth + 7) / 8, 0, tileWidth, rows, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_PACKBITS) {
                            int numCompressedBytes = compressPackBits(bpixels, rows, (int) bytesPerRow, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_DEFLATE) {
                            int numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        }
                        break;
                    case 4:
                        index = 0;
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < tileWidth / 2; j++) {
                                pixel = (pixels[index++] << 4) | pixels[index++];
                                bpixels[k++] = (byte) pixel;
                            }
                            if ((tileWidth % 2) == 1) {
                                pixel = pixels[index++] << 4;
                                bpixels[k++] = (byte) pixel;
                            }
                        }
                        if (compression == COMP_NONE) {
                            output.write(bpixels, 0, rows * ((tileWidth + 1) / 2));
                        } else if (compression == COMP_PACKBITS) {
                            int numCompressedBytes = compressPackBits(bpixels, rows, (int) bytesPerRow, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_DEFLATE) {
                            int numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        }
                        break;
                    case 8:
                        if (compression != COMP_JPEG_TTN2) {
                            if (useDataBuffer) {
                                byte[] btmp = ((DataBufferByte) src.getDataBuffer()).getData();
                                ComponentSampleModel csm = (ComponentSampleModel) src.getSampleModel();
                                int inOffset = csm.getOffset(col - src.getSampleModelTranslateX(), row - src.getSampleModelTranslateY());
                                int lineStride = csm.getScanlineStride();
                                if (lineStride == (int) bytesPerRow) {
                                    System.arraycopy(btmp, inOffset, bpixels, 0, (int) bytesPerRow * rows);
                                } else {
                                    int outOffset = 0;
                                    for (int j = 0; j < rows; j++) {
                                        System.arraycopy(btmp, inOffset, bpixels, outOffset, (int) bytesPerRow);
                                        inOffset += lineStride;
                                        outOffset += (int) bytesPerRow;
                                    }
                                }
                            } else {
                                for (int i = 0; i < size; i++) {
                                    bpixels[i] = (byte) pixels[i];
                                }
                            }
                        }
                        if (compression == COMP_NONE) {
                            output.write(bpixels, 0, size);
                        } else if (compression == COMP_PACKBITS) {
                            int numCompressedBytes = compressPackBits(bpixels, rows, (int) bytesPerRow, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_JPEG_TTN2) {
                            long startPos = getOffset(output);
                            if (jpegEncoder == null || jpegEncodeParam.getWidth() != src.getWidth() || jpegEncodeParam.getHeight() != src.getHeight()) {
                                jpegEncodeParam = com.sun.image.codec.jpeg.JPEGCodec.getDefaultJPEGEncodeParam(src, jpegColorID);
                                JPEGImageEncoder.modifyEncodeParam(jep, jpegEncodeParam, numBands);
                                jpegEncoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGEncoder(output, jpegEncodeParam);
                            }
                            if (jpegRGBToYCbCr) {
                                WritableRaster wRas = null;
                                if (src instanceof WritableRaster) {
                                    wRas = (WritableRaster) src;
                                } else {
                                    wRas = src.createCompatibleWritableRaster();
                                    wRas.setRect(src);
                                }
                                if (wRas.getMinX() != 0 || wRas.getMinY() != 0) {
                                    wRas = wRas.createWritableTranslatedChild(0, 0);
                                }
                                BufferedImage bi = new BufferedImage(colorModel, wRas, false, null);
                                jpegEncoder.encode(bi);
                            } else {
                                jpegEncoder.encode(src.createTranslatedChild(0, 0));
                            }
                            long endPos = getOffset(output);
                            tileByteCounts[tileNum++] = (int) (endPos - startPos);
                        } else if (compression == COMP_DEFLATE) {
                            int numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        }
                        break;
                    case 16:
                        int ls = 0;
                        for (int i = 0; i < size; i++) {
                            short value = (short) pixels[i];
                            bpixels[ls++] = (byte) ((value & 0xff00) >> 8);
                            bpixels[ls++] = (byte) (value & 0x00ff);
                        }
                        if (compression == COMP_NONE) {
                            output.write(bpixels, 0, size * 2);
                        } else if (compression == COMP_PACKBITS) {
                            int numCompressedBytes = compressPackBits(bpixels, rows, (int) bytesPerRow, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_DEFLATE) {
                            int numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        }
                        break;
                    case 32:
                        if (dataType == DataBuffer.TYPE_INT) {
                            int li = 0;
                            for (int i = 0; i < size; i++) {
                                int value = pixels[i];
                                bpixels[li++] = (byte) ((value & 0xff000000) >> 24);
                                bpixels[li++] = (byte) ((value & 0x00ff0000) >> 16);
                                bpixels[li++] = (byte) ((value & 0x0000ff00) >> 8);
                                bpixels[li++] = (byte) (value & 0x000000ff);
                            }
                        } else {
                            int lf = 0;
                            for (int i = 0; i < size; i++) {
                                int value = Float.floatToIntBits(fpixels[i]);
                                bpixels[lf++] = (byte) ((value & 0xff000000) >> 24);
                                bpixels[lf++] = (byte) ((value & 0x00ff0000) >> 16);
                                bpixels[lf++] = (byte) ((value & 0x0000ff00) >> 8);
                                bpixels[lf++] = (byte) (value & 0x000000ff);
                            }
                        }
                        if (compression == COMP_NONE) {
                            output.write(bpixels, 0, size * 4);
                        } else if (compression == COMP_PACKBITS) {
                            int numCompressedBytes = compressPackBits(bpixels, rows, (int) bytesPerRow, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        } else if (compression == COMP_DEFLATE) {
                            int numCompressedBytes = deflate(deflater, bpixels, compressBuf);
                            tileByteCounts[tileNum++] = numCompressedBytes;
                            output.write(compressBuf, 0, numCompressedBytes);
                        }
                        break;
                }
            }
        }
        if (compression == COMP_NONE) {
            if (skipByte) {
                output.write((byte) 0);
            }
        } else {
            int totalBytes = 0;
            for (int i = 1; i < numTiles; i++) {
                int numBytes = (int) tileByteCounts[i - 1];
                totalBytes += numBytes;
                tileOffsets[i] = tileOffsets[i - 1] + numBytes;
            }
            totalBytes += (int) tileByteCounts[numTiles - 1];
            nextIFDOffset = isLast ? 0 : ifdOffset + dirSize + totalBytes;
            skipByte = nextIFDOffset % 2 != 0;
            if (outCache == null) {
                if (skipByte) {
                    output.write((byte) 0);
                }
                SeekableOutputStream sos = (SeekableOutputStream) output;
                long savePos = sos.getFilePointer();
                sos.seek(ifdOffset);
                writeDirectory(ifdOffset, fields, nextIFDOffset);
                sos.seek(savePos);
            } else if (tempFile != null) {
                FileInputStream fileStream = new FileInputStream(tempFile);
                output.close();
                output = outCache;
                writeDirectory(ifdOffset, fields, nextIFDOffset);
                byte[] copyBuffer = new byte[8192];
                int bytesCopied = 0;
                while (bytesCopied < totalBytes) {
                    int bytesRead = fileStream.read(copyBuffer);
                    if (bytesRead == -1) {
                        break;
                    }
                    output.write(copyBuffer, 0, bytesRead);
                    bytesCopied += bytesRead;
                }
                fileStream.close();
                tempFile.delete();
                if (skipByte) {
                    output.write((byte) 0);
                }
            } else if (output instanceof ByteArrayOutputStream) {
                ByteArrayOutputStream memoryStream = (ByteArrayOutputStream) output;
                output = outCache;
                writeDirectory(ifdOffset, fields, nextIFDOffset);
                memoryStream.writeTo(output);
                if (skipByte) {
                    output.write((byte) 0);
                }
            } else {
                throw new IllegalStateException();
            }
        }
        return nextIFDOffset;
    }
