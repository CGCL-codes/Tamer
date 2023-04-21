    protected void initFile(String id) throws FormatException, IOException {
        if (debug) debug("OpenlabReader.initFile(" + id + ")");
        super.initFile(id);
        in = new RandomAccessStream(id);
        status("Verifying Openlab LIFF format");
        in.order(false);
        in.skipBytes(4);
        if (!in.readString(4).equals("impr")) {
            throw new FormatException("Invalid LIFF file.");
        }
        version = in.readInt();
        if (version != 2 && version != 5) {
            throw new FormatException("Invalid version : " + version);
        }
        in.skipBytes(4);
        int offset = in.readInt();
        in.seek(offset);
        status("Finding image offsets");
        layerInfoList = new Vector[2];
        for (int i = 0; i < layerInfoList.length; i++) layerInfoList[i] = new Vector();
        xCal = yCal = zCal = (float) 0.0;
        while (in.getFilePointer() < in.length()) {
            long nextTag, startPos;
            subTag = tag = 0;
            try {
                startPos = in.getFilePointer();
                nextTag = readTagHeader();
            } catch (IOException exc) {
                if (debug) trace(exc);
                if (in.getFilePointer() >= in.length()) break; else throw new FormatException(exc.getMessage());
            }
            try {
                if (tag == 67 || tag == 68 || fmt.equals("PICT") || fmt.equals("RAWi")) {
                    LayerInfo info = new LayerInfo();
                    info.layerStart = (int) startPos;
                    info.zPosition = -1;
                    info.wavelength = -1;
                    in.skipBytes(24);
                    int volumeType = in.readShort();
                    if (volumeType == MAC_1_BIT || volumeType == MAC_256_GREYS || volumeType == MAC_256_COLORS || (volumeType >= MAC_24_BIT && volumeType <= GREY_16_BIT)) {
                        in.skipBytes(16);
                        info.layerName = in.readString(128);
                        if (!info.layerName.trim().equals("Original Image")) {
                            info.timestamp = in.readLong();
                            layerInfoList[0].add(info);
                        }
                    }
                } else if (tag == 69) {
                    in.skipBytes(18);
                    xCal = in.readFloat();
                    yCal = in.readFloat();
                } else if (tag == 72 || fmt.equals("USER")) {
                    char aChar = (char) in.read();
                    StringBuffer sb = new StringBuffer();
                    while (aChar != 0) {
                        sb = sb.append(aChar);
                        aChar = (char) in.read();
                    }
                    String className = sb.toString();
                    if (className.equals("CVariableList")) {
                        aChar = (char) in.read();
                        if (aChar == 1) {
                            int numVars = in.readShort();
                            while (numVars > 0) {
                                aChar = (char) in.read();
                                sb = new StringBuffer();
                                while (aChar != 0) {
                                    sb = sb.append(aChar);
                                    aChar = (char) in.read();
                                }
                                String varName = "";
                                String varStringValue = "";
                                double varNumValue = 0.0;
                                className = sb.toString();
                                int derivedClassVersion = in.read();
                                if (derivedClassVersion != 1) {
                                    throw new FormatException("Invalid revision.");
                                }
                                if (className.equals("CStringVariable")) {
                                    int strSize = in.readInt();
                                    varStringValue = in.readString(strSize);
                                    varNumValue = Float.parseFloat(varStringValue);
                                    in.skipBytes(1);
                                } else if (className.equals("CFloatVariable")) {
                                    varNumValue = in.readDouble();
                                    varStringValue = "" + varNumValue;
                                }
                                int baseClassVersion = in.read();
                                if (baseClassVersion == 1 || baseClassVersion == 2) {
                                    int strSize = in.readInt();
                                    varName = in.readString(strSize);
                                    in.skipBytes(baseClassVersion == 1 ? 3 : 2);
                                } else {
                                    throw new FormatException("Invalid revision.");
                                }
                                addMeta(varName, varStringValue);
                                numVars--;
                            }
                        }
                    }
                }
                in.seek(nextTag);
            } catch (Exception exc) {
                if (debug) trace(exc);
                in.seek(nextTag);
            }
        }
        Vector tmp = new Vector();
        for (int i = 0; i < layerInfoList[0].size(); i++) {
            tmp.add(layerInfoList[0].get(i));
        }
        core = new CoreMetadata(2);
        core.imageCount[0] = tmp.size();
        status("Determining series count");
        int oldChannels = openBytes(0).length / (core.sizeX[0] * core.sizeY[0] * 3);
        int oldWidth = core.sizeX[0];
        for (int i = 0; i < tmp.size(); i++) {
            LayerInfo layer = (LayerInfo) tmp.get(i);
            in.seek(layer.layerStart);
            long nextTag = readTagHeader();
            if (fmt.equals("PICT")) {
                in.skipBytes(298);
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
                in.seek(layer.layerStart);
                if (version == 2) {
                    nextTag = readTagHeader();
                    if ((tag != 67 && tag != 68) || !fmt.equals("PICT")) {
                        throw new FormatException("Corrupt LIFF file.");
                    }
                    in.skipBytes(298);
                    try {
                        byte[] b = new byte[(int) (nextTag - in.getFilePointer())];
                        in.read(b);
                        BufferedImage img = pict.open(b);
                        if (img.getRaster().getNumBands() != oldChannels || img.getWidth() != oldWidth) {
                            layerInfoList[1].add(tmp.get(i));
                            layerInfoList[0].remove(tmp.get(i));
                        }
                    } catch (FormatException e) {
                    }
                }
            } else {
                in.skipBytes(24);
                int type = DataTools.read2SignedBytes(in, false);
                if (type == MAC_24_BIT) {
                    layerInfoList[1].add(tmp.get(i));
                    layerInfoList[0].remove(tmp.get(i));
                }
            }
        }
        if (layerInfoList[1].size() == 0 || layerInfoList[0].size() == 0) {
            core.sizeC = new int[1];
            core.sizeC[0] = layerInfoList[1].size() == 0 ? 1 : 3;
            if (core.sizeC[0] == 1 && oldChannels == 1) core.sizeC[0] = 3;
            int oldImages = core.imageCount[0];
            core.imageCount = new int[1];
            core.imageCount[0] = oldImages;
            if (layerInfoList[0].size() == 0) layerInfoList[0] = layerInfoList[1];
            int x = core.sizeX[0];
            core.sizeX = new int[1];
            core.sizeX[0] = x;
        } else {
            core.imageCount[0] = layerInfoList[0].size();
            core.imageCount[1] = layerInfoList[1].size();
            core.sizeC[0] = 1;
            core.sizeC[1] = 3;
            int oldW = core.sizeX[0];
            int oldH = core.sizeY[0];
            core.sizeX = new int[2];
            core.sizeY = new int[2];
            core.sizeX[0] = oldW;
            core.sizeX[1] = oldW;
            core.sizeY[0] = oldH;
            core.sizeY[1] = oldH;
        }
        Arrays.fill(core.metadataComplete, true);
        status("Populating metadata");
        numSeries = core.imageCount.length;
        int[] bpp = new int[numSeries];
        Arrays.fill(core.orderCertain, true);
        int oldSeries = getSeries();
        for (int i = 0; i < bpp.length; i++) {
            setSeries(i);
            if (core.sizeC[i] == 0) core.sizeC[i] = 1;
            bpp[i] = openBytes(0).length / (core.sizeX[i] * core.sizeY[i]);
        }
        setSeries(oldSeries);
        if (bytesPerPixel == 3) bytesPerPixel = 1;
        if (bytesPerPixel == 0) bytesPerPixel++;
        addMeta("Version", new Integer(version));
        addMeta("Number of Series", new Integer(numSeries));
        for (int i = 0; i < numSeries; i++) {
            addMeta("Width (Series " + i + ")", new Integer(core.sizeX[i]));
            addMeta("Height (Series " + i + ")", new Integer(core.sizeY[i]));
            addMeta("Bit depth (Series " + i + ")", new Integer(bpp[i] * 8));
            addMeta("Number of channels (Series " + i + ")", new Integer(core.sizeC[i]));
            addMeta("Number of images (Series " + i + ")", new Integer(core.imageCount[i]));
        }
        MetadataStore store = getMetadataStore();
        for (int i = 0; i < numSeries; i++) {
            core.sizeT[i] += 1;
            core.sizeZ[i] = core.imageCount[i] / core.sizeT[i];
            core.currentOrder[i] = isRGB() ? "XYCZT" : "XYZCT";
            core.rgb[i] = core.sizeC[i] > 1;
            core.interleaved[i] = true;
            core.littleEndian[i] = false;
            try {
                if (i != 0) {
                    if (bpp[i] == bpp[0]) bpp[i] = bpp[i + 1];
                }
            } catch (ArrayIndexOutOfBoundsException a) {
            }
            switch(bpp[i]) {
                case 1:
                case 3:
                    core.pixelType[i] = FormatTools.UINT8;
                    break;
                case 2:
                case 6:
                    core.pixelType[i] = FormatTools.UINT16;
                    break;
                case 4:
                    core.pixelType[i] = FormatTools.UINT32;
                    break;
            }
            store.setImage("Series " + i, null, null, new Integer(i));
            store.setDimensions(new Float(xCal), new Float(yCal), new Float(zCal), null, null, new Integer(i));
        }
        FormatTools.populatePixels(store, this);
        for (int i = 0; i < numSeries; i++) {
            for (int j = 0; j < core.sizeC[i]; j++) {
                store.setLogicalChannel(j, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, new Integer(i));
            }
        }
    }
