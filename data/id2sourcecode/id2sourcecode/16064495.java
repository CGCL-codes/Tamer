    private void createXchangeElementContainer(File creationLocation, File sourceFile) {
        String orignalContainerUuid = ConversionUtil.stripXPX(sourceFile);
        String newContainerUuid = getUuid(orignalContainerUuid);
        sendProgressMessage("Processing XchangeElementContainer - " + newContainerUuid);
        File dir = new File(creationLocation.getAbsoluteFile() + File.separator + newContainerUuid);
        dir.mkdirs();
        try {
            File destinationFile = new File(openDestDir + File.separator + dir.getName() + ".xpx");
            FileUtils.copyFile(sourceFile, destinationFile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Problem creating the container xpx file for: " + orignalContainerUuid, e);
        }
        containerContents.put(newContainerUuid, new HashSet<String>());
    }
