    public void initWriter(OutputStream os) throws IOException {
        zipOutputFileSream = new ZipOutputStream(os);
        String outputFile = "zipped.sdf";
        ZipEntry zipEntry = new ZipEntry(outputFile);
        BasicIOType outType = BasicMoleculeWriter.checkGetOutputType(outputFile);
        zipOutputFileSream.putNextEntry(zipEntry);
        writer = new BasicMoleculeWriter(zipOutputFileSream, outType);
    }
