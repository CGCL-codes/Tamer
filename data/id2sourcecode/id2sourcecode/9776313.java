    public static int writeReadHeader(SffReadHeader readHeader, OutputStream out) throws IOException {
        String name = readHeader.getId();
        final int nameLength = name.length();
        int unpaddedHeaderLength = 16 + nameLength;
        int padding = SffUtil.caclulatePaddedBytes(unpaddedHeaderLength);
        int paddedHeaderLength = unpaddedHeaderLength + padding;
        out.write(IOUtil.convertUnsignedShortToByteArray(paddedHeaderLength));
        out.write(IOUtil.convertUnsignedShortToByteArray(nameLength));
        out.write(IOUtil.convertUnsignedIntToByteArray(readHeader.getNumberOfBases()));
        writeClip(readHeader.getQualityClip(), out);
        writeClip(readHeader.getAdapterClip(), out);
        out.write(name.getBytes(IOUtil.UTF_8));
        out.write(new byte[padding]);
        out.flush();
        return paddedHeaderLength;
    }
