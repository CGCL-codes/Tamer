    private static void writePlacedRead(AcePlacedRead read, Phd phd, OutputStream out) throws IOException {
        writeString(AceFileUtil.createAcePlacedReadRecord(read.getId(), read.getEncodedGlyphs(), read.getValidRange(), read.getSequenceDirection(), phd, read.getPhdInfo()), out);
    }
