    public static OutputStream wrapInCompressorStream(OutputStream l_outStream, String l_fileName) throws IOException {
        java.util.zip.ZipOutputStream result = new java.util.zip.ZipOutputStream(l_outStream);
        result.putNextEntry(new java.util.zip.ZipEntry(l_fileName));
        return result;
    }
