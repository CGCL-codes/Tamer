    protected void copyJarFileToDynamic(File file) {
        String fileName = file.getName();
        String dstFilePath = System.getProperty(XA_Designer_Plugin.xahomeStr);
        if (!dstFilePath.endsWith(File.separator)) {
            dstFilePath += File.separator;
        }
        dstFilePath += XAwareConstants.DYNAMIC_JARS_PATH + File.separator + fileName;
        try {
            FileUtils.copyFile(file.getAbsolutePath(), dstFilePath);
            summaryPage.addLineToEntry(this, JAR_INFORMATION, translator.getString("Copied jar ") + file.getAbsolutePath() + translator.getString(" to ") + dstFilePath);
        } catch (FileNotFoundException e) {
            ControlFactory.showStackTrace(translator.getString("Error copying jar file ") + bizDriverRef, e);
        } catch (IOException e) {
            ControlFactory.showStackTrace(translator.getString("Error copying jar file ") + bizDriverRef, e);
        }
    }
