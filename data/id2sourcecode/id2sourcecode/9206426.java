    private boolean createFiles(File dataSet, File destDir, List<String> files) {
        String corrFilename = "";
        ZipFile in;
        try {
            in = new ZipFile(dataSet);
        } catch (IOException e) {
            Logging.errorPrint("Failed to read data set " + dataSet + " due to ", e);
            ShowMessageDelegate.showMessageDialog(PropertyFactory.getFormattedString("in_diBadDataSet", dataSet), TITLE, MessageType.ERROR);
            return false;
        }
        try {
            for (String filename : files) {
                ZipEntry entry = in.getEntry(filename);
                corrFilename = correctFileName(destDir, filename);
                Logging.debugPrint("Extracting file: " + filename + " to " + corrFilename);
                copyInputStream(in.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(corrFilename)));
            }
            in.close();
        } catch (IOException e) {
            Logging.errorPrint("Failed to read data set " + dataSet + " or write file " + corrFilename + " due to ", e);
            ShowMessageDelegate.showMessageDialog(PropertyFactory.getFormattedString("in_diWriteFail", corrFilename), TITLE, MessageType.ERROR);
            return false;
        }
        return true;
    }
