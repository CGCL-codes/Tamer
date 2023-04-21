    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = sortedPositionScores[0].position;
            useShort = true;
            for (int i = 1; i < sortedPositionScores.length; i++) {
                int currentStart = sortedPositionScores[i].position;
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
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedPositionScores[0].position);
            dos.writeFloat(sortedPositionScores[0].score);
            if (useShort) {
                int bp = sortedPositionScores[0].position;
                for (int i = 1; i < sortedPositionScores.length; i++) {
                    int currentStart = sortedPositionScores[i].position;
                    int diff = currentStart - bp - 32768;
                    dos.writeShort((short) (diff));
                    dos.writeFloat(sortedPositionScores[i].score);
                    bp = currentStart;
                }
            } else {
                int bp = sortedPositionScores[0].position;
                for (int i = 1; i < sortedPositionScores.length; i++) {
                    int currentStart = sortedPositionScores[i].position;
                    int diff = currentStart - bp;
                    dos.writeInt(diff);
                    dos.writeFloat(sortedPositionScores[i].score);
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
