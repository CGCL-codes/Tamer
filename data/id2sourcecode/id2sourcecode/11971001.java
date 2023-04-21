    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = sortedRegions[0].start;
            useShortBeginning = true;
            for (int i = 1; i < sortedRegions.length; i++) {
                int currentStart = sortedRegions[i].start;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < sortedRegions.length; i++) {
                int diff = sortedRegions[i].stop - sortedRegions[i].start;
                if (diff > 65536) {
                    useShortLength = false;
                    break;
                }
            }
        }
        String fileType;
        if (useShortBeginning) fileType = USeqUtilities.SHORT; else fileType = USeqUtilities.INT;
        if (useShortLength) fileType = fileType + USeqUtilities.SHORT; else fileType = fileType + USeqUtilities.INT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedRegions[0].start);
            int bp = sortedRegions[0].start;
            if (useShortBeginning) {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegions[0].stop - sortedRegions[0].start);
                    for (int i = 1; i < sortedRegions.length; i++) {
                        int currentStart = sortedRegions[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeInt(sortedRegions[i].stop - sortedRegions[i].start);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegions[0].stop - sortedRegions[0].start - 32768));
                    for (int i = 1; i < sortedRegions.length; i++) {
                        int currentStart = sortedRegions[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeShort((short) (sortedRegions[i].stop - sortedRegions[i].start - 32768));
                        bp = currentStart;
                    }
                }
            } else {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegions[0].stop - sortedRegions[0].start);
                    for (int i = 1; i < sortedRegions.length; i++) {
                        int currentStart = sortedRegions[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeInt(sortedRegions[i].stop - sortedRegions[i].start);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegions[0].stop - sortedRegions[0].start - 32768));
                    for (int i = 1; i < sortedRegions.length; i++) {
                        int currentStart = sortedRegions[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeShort((short) (sortedRegions[i].stop - sortedRegions[i].start - 32768));
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
