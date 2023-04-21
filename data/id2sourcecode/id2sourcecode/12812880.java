    public void initWriter(OutputStream os) throws IOException {
        zipOutputFileSream = new ZipOutputStream(os);
        String outputFile = "zipped.sdf";
        ZipEntry zipEntry = new ZipEntry(outputFile);
        IOType outType = SimpleWriter.checkGetOutputType(outputFile);
        zipOutputFileSream.putNextEntry(zipEntry);
        writer = new SimpleWriter(zipOutputFileSream, outType);
    }
