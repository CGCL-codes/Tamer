    public void write() throws IOException {
        if (trace) {
            say((numObjects / 1024) + "k objects");
            say((numAddresses / 1024) + "k non-null object references");
            say(numNulledReferences + " references nulled because they are " + "non-jdk fields or point to non-bootimage objects");
            say(((Statics.getNumberOfReferenceSlots() + Statics.getNumberOfNumericSlots()) / 1024) + "k jtoc slots");
            say((getDataSize() / 1024) + "k data in image");
            say((getCodeSize() / 1024) + "k code in image");
            say("writing " + imageDataFileName);
        }
        if (!mapByteBuffers) {
            dataOut.write(bootImageData.array(), 0, getDataSize());
        } else {
            dataOut.getChannel().truncate(getDataSize());
        }
        dataOut.close();
        if (trace) {
            say("writing " + imageCodeFileName);
        }
        if (!mapByteBuffers) {
            codeOut.write(bootImageCode.array(), 0, getCodeSize());
        } else {
            codeOut.getChannel().truncate(getCodeSize());
        }
        codeOut.close();
        if (trace) {
            say("writing " + imageRMapFileName);
        }
        bootImageRMap = new byte[referenceMapReferences << LOG_BYTES_IN_WORD];
        rMapSize = ScanBootImage.encodeRMap(bootImageRMap, referenceMap, referenceMapLimit);
        FileOutputStream rmapOut = new FileOutputStream(imageRMapFileName);
        rmapOut.write(bootImageRMap, 0, rMapSize);
        rmapOut.flush();
        rmapOut.close();
        if (trace) {
            say("total refs: " + referenceMapReferences);
        }
        ScanBootImage.encodingStats();
    }
