    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = sortedRegionTexts[0].start;
            useShortBeginning = true;
            for (int i = 1; i < sortedRegionTexts.length; i++) {
                int currentStart = sortedRegionTexts[i].start;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < sortedRegionTexts.length; i++) {
                int diff = sortedRegionTexts[i].stop - sortedRegionTexts[i].start;
                if (diff > 65536) {
                    useShortLength = false;
                    break;
                }
            }
        }
        String fileType;
        if (useShortBeginning) fileType = USeqUtilities.SHORT; else fileType = USeqUtilities.INT;
        if (useShortLength) fileType = fileType + USeqUtilities.SHORT; else fileType = fileType + USeqUtilities.INT;
        fileType = fileType + USeqUtilities.TEXT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedRegionTexts[0].start);
            int bp = sortedRegionTexts[0].start;
            if (useShortBeginning) {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegionTexts[0].stop - sortedRegionTexts[0].start);
                    dos.writeUTF(sortedRegionTexts[0].text);
                    for (int i = 1; i < sortedRegionTexts.length; i++) {
                        int currentStart = sortedRegionTexts[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeInt(sortedRegionTexts[i].stop - sortedRegionTexts[i].start);
                        dos.writeUTF(sortedRegionTexts[i].text);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegionTexts[0].stop - sortedRegionTexts[0].start - 32768));
                    dos.writeUTF(sortedRegionTexts[0].text);
                    for (int i = 1; i < sortedRegionTexts.length; i++) {
                        int currentStart = sortedRegionTexts[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeShort((short) (sortedRegionTexts[i].stop - sortedRegionTexts[i].start - 32768));
                        dos.writeUTF(sortedRegionTexts[i].text);
                        bp = currentStart;
                    }
                }
            } else {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegionTexts[0].stop - sortedRegionTexts[0].start);
                    dos.writeUTF(sortedRegionTexts[0].text);
                    for (int i = 1; i < sortedRegionTexts.length; i++) {
                        int currentStart = sortedRegionTexts[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeInt(sortedRegionTexts[i].stop - sortedRegionTexts[i].start);
                        dos.writeUTF(sortedRegionTexts[i].text);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegionTexts[0].stop - sortedRegionTexts[0].start - 32768));
                    dos.writeUTF(sortedRegionTexts[0].text);
                    for (int i = 1; i < sortedRegionTexts.length; i++) {
                        int currentStart = sortedRegionTexts[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeShort((short) (sortedRegionTexts[i].stop - sortedRegionTexts[i].start - 32768));
                        dos.writeUTF(sortedRegionTexts[i].text);
                        bp = currentStart;
                    }
                }
            }
            out.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(dos);
        }
    }
