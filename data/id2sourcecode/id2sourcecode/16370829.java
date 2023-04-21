    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShortBeginning = false;
        boolean useShortLength = false;
        if (attemptToSaveAsShort) {
            int bp = sortedRegionScores[0].start;
            useShortBeginning = true;
            for (int i = 1; i < sortedRegionScores.length; i++) {
                int currentStart = sortedRegionScores[i].start;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShortBeginning = false;
                    break;
                }
                bp = currentStart;
            }
            useShortLength = true;
            for (int i = 0; i < sortedRegionScores.length; i++) {
                int diff = sortedRegionScores[i].stop - sortedRegionScores[i].start;
                if (diff > 65536) {
                    useShortLength = false;
                    break;
                }
            }
        }
        String fileType;
        if (useShortBeginning) fileType = USeqUtilities.SHORT; else fileType = USeqUtilities.INT;
        if (useShortLength) fileType = fileType + USeqUtilities.SHORT; else fileType = fileType + USeqUtilities.INT;
        fileType = fileType + USeqUtilities.FLOAT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedRegionScores[0].start);
            int bp = sortedRegionScores[0].start;
            if (useShortBeginning) {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegionScores[0].stop - sortedRegionScores[0].start);
                    dos.writeFloat(sortedRegionScores[0].score);
                    for (int i = 1; i < sortedRegionScores.length; i++) {
                        int currentStart = sortedRegionScores[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeInt(sortedRegionScores[i].stop - sortedRegionScores[i].start);
                        dos.writeFloat(sortedRegionScores[i].score);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegionScores[0].stop - sortedRegionScores[0].start - 32768));
                    dos.writeFloat(sortedRegionScores[0].score);
                    for (int i = 1; i < sortedRegionScores.length; i++) {
                        int currentStart = sortedRegionScores[i].start;
                        int diff = currentStart - bp - 32768;
                        dos.writeShort((short) (diff));
                        dos.writeShort((short) (sortedRegionScores[i].stop - sortedRegionScores[i].start - 32768));
                        dos.writeFloat(sortedRegionScores[i].score);
                        bp = currentStart;
                    }
                }
            } else {
                if (useShortLength == false) {
                    dos.writeInt(sortedRegionScores[0].stop - sortedRegionScores[0].start);
                    dos.writeFloat(sortedRegionScores[0].score);
                    for (int i = 1; i < sortedRegionScores.length; i++) {
                        int currentStart = sortedRegionScores[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeInt(sortedRegionScores[i].stop - sortedRegionScores[i].start);
                        dos.writeFloat(sortedRegionScores[i].score);
                        bp = currentStart;
                    }
                } else {
                    dos.writeShort((short) (sortedRegionScores[0].stop - sortedRegionScores[0].start - 32768));
                    dos.writeFloat(sortedRegionScores[0].score);
                    for (int i = 1; i < sortedRegionScores.length; i++) {
                        int currentStart = sortedRegionScores[i].start;
                        int diff = currentStart - bp;
                        dos.writeInt(diff);
                        dos.writeShort((short) (sortedRegionScores[i].stop - sortedRegionScores[i].start - 32768));
                        dos.writeFloat(sortedRegionScores[i].score);
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
