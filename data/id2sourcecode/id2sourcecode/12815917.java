    public void write(ZipOutputStream out, DataOutputStream dos, boolean attemptToSaveAsShort) {
        boolean useShort = false;
        if (attemptToSaveAsShort) {
            int bp = sortedPositionTexts[0].position;
            useShort = true;
            for (int i = 1; i < sortedPositionTexts.length; i++) {
                int currentStart = sortedPositionTexts[i].position;
                int diff = currentStart - bp;
                if (diff > 65536) {
                    useShort = false;
                    break;
                }
                bp = currentStart;
            }
        }
        String fileType;
        if (useShort) fileType = USeqUtilities.SHORT + USeqUtilities.TEXT; else fileType = USeqUtilities.INT + USeqUtilities.TEXT;
        sliceInfo.setBinaryType(fileType);
        binaryFile = null;
        try {
            out.putNextEntry(new ZipEntry(sliceInfo.getSliceName()));
            dos.writeUTF(header);
            dos.writeInt(sortedPositionTexts[0].position);
            dos.writeUTF(sortedPositionTexts[0].text);
            if (useShort) {
                int bp = sortedPositionTexts[0].position;
                for (int i = 1; i < sortedPositionTexts.length; i++) {
                    int currentStart = sortedPositionTexts[i].position;
                    int diff = currentStart - bp - 32768;
                    dos.writeShort((short) (diff));
                    dos.writeUTF(sortedPositionTexts[i].text);
                    bp = currentStart;
                }
            } else {
                int bp = sortedPositionTexts[0].position;
                for (int i = 1; i < sortedPositionTexts.length; i++) {
                    int currentStart = sortedPositionTexts[i].position;
                    int diff = currentStart - bp;
                    dos.writeInt(diff);
                    dos.writeUTF(sortedPositionTexts[i].text);
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
