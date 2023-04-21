    private File writeContentFile(ARCRecord arcRec) throws Exception {
        byte[] buffer = new byte[4000];
        File filePath = new File(arcLoc, EXT_PREFIX + (++fileCount));
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(filePath);
            int read = 0;
            while ((read = arcRec.read(buffer)) != -1) {
                fileOut.write(buffer, 0, read);
            }
        } finally {
            if (fileOut != null) {
                fileOut.close();
            }
        }
        return filePath;
    }
