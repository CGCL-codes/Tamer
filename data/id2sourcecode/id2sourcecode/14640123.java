    public static void writeReadHeader(SFFReadHeader readHeader, OutputStream out) throws IOException {
        String name = readHeader.getName();
        final int nameLength = name.length();
        int unpaddedHeaderLength = 16 + nameLength;
        int padding = SFFUtil.caclulatePaddedBytes(unpaddedHeaderLength);
        out.write(IOUtil.convertUnsignedShortToByteArray(unpaddedHeaderLength + padding));
        out.write(IOUtil.convertUnsignedShortToByteArray(nameLength));
        out.write(IOUtil.convertUnsignedIntToByteArray(readHeader.getNumberOfBases()));
        writeClip(readHeader.getQualityClip(), out);
        writeClip(readHeader.getAdapterClip(), out);
        out.write(name.getBytes());
        out.write(new byte[padding]);
        out.flush();
    }
