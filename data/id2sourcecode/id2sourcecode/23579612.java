    private static AbstractPVRFileSystem primaryDetection(final File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        FileChannel fc = fis.getChannel();
        ImageIdStruct img = parsePartitionTable(fc);
        fis.close();
        if (img != null) {
            switch(img.version) {
                case TSD_V1:
                    return new PVRV1FileSystem(file);
                case TSD_V2:
                    return new PVRV2FileSystem(file, img.startSector);
                default:
                    LOGGER.severe("Illegal Image type detected");
            }
        }
        return null;
    }
