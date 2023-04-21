    private static String doCompress(File source, File destination, MessageDigest md, Set<String> fileNamesToSkipInCheckSumCalculation) throws Exception {
        if (!destination.exists()) {
            File destinationDir = destination.getParentFile();
            if (!destinationDir.exists()) {
                destinationDir.mkdirs();
            }
        } else if (destination.isDirectory()) {
            throw new IOException("Compression can be done into file only!");
        }
        if (destination.exists()) {
            destination.delete();
        }
        destination.createNewFile();
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destination));
        compress(source, source, out, md, fileNamesToSkipInCheckSumCalculation);
        out.close();
        return md != null ? FormatUtils.formatBytesAsHexString(md.digest()) : null;
    }
