    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = sortedPositionScoreTexts[0].position;
            useShort = true;
            for (int i = 1; i < sortedPositionScoreTexts.length; i++) {
                int currentStart = sortedPositionScoreTexts[i].position;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShort = false;
                    break;
                }
                bp = currentStart;
            }
        }
        String fileType;
        if (useShort) fileType = USeqUtilities.SHORT + USeqUtilities.FLOAT; else fileType = USeqUtilities.INT + USeqUtilities.FLOAT;
        fileType = fileType + USeqUtilities.TEXT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedPositionScoreTexts[0].position);
            dos.writeFloat(sortedPositionScoreTexts[0].score);
            dos.writeUTF(sortedPositionScoreTexts[0].text);
            if (useShort) {
                int bp = sortedPositionScoreTexts[0].position;
                for (int i = 1; i < sortedPositionScoreTexts.length; i++) {
                    int currentStart = sortedPositionScoreTexts[i].position;
                    int diff = currentStart - bp - 32768;
                    dos.writeShort((short) (diff));
                    dos.writeFloat(sortedPositionScoreTexts[i].score);
                    dos.writeUTF(sortedPositionScoreTexts[i].text);
                    bp = currentStart;
                }
            } else {
                int bp = sortedPositionScoreTexts[0].position;
                for (int i = 1; i < sortedPositionScoreTexts.length; i++) {
                    int currentStart = sortedPositionScoreTexts[i].position;
                    int diff = currentStart - bp;
                    dos.writeInt(diff);
                    dos.writeFloat(sortedPositionScoreTexts[i].score);
                    dos.writeUTF(sortedPositionScoreTexts[i].text);
                    bp = currentStart;
                }
            }
            out.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
            USeqUtilities.safeClose(out);
            USeqUtilities.safeClose(dos);
        }
    }
