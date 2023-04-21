    public static void main(String[] args) {
        File srcDir = null;
        if (DEBUG) {
            srcDir = new File("D:\\SoftwareTests\\TestImages\\Test_Images\\Test_Images\\Enhancement");
        } else {
            if (args.length == 1) {
                srcDir = new File(args[0]);
            } else {
                exitWithError();
            }
        }
        if (!srcDir.isDirectory()) {
            exitWithError();
        }
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            LOGGER.info("Processing File " + files[i].getName());
            try {
                FileImageWriter.write(FileImageReader.read(files[i].getAbsolutePath()), ((files[i].getAbsolutePath().split(ImageAPIConstants.DOT_CHARACTER_REGEX)[0]) + "_converted"));
            } catch (IOException ex) {
                LOGGER.error("Exception occurred", ex);
            }
        }
    }
