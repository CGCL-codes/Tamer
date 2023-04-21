    private void copyXpxElements(File xpxFile, String containerUuid) {
        String xchangeElementUuid = getUuid(ConversionUtil.stripXPX(xpxFile));
        File destinationFile = new File(openDestDir + File.separator + containerUuid + File.separator + xchangeElementUuid + ".xpx");
        try {
            FileUtils.copyFile(xpxFile, destinationFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problem copying element file: " + xpxFile, e);
        }
    }
