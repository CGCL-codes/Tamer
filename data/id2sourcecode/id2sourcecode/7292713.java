    private void addIncrDataCopySpecificScript(BufferedWriter incrDataCopyBatWriter, File file) throws IOException {
        if (file.isDirectory() && file.listFiles() != null) {
            for (int i = 0; i < file.listFiles().length; i++) {
                File fullDataLoadFile = file.listFiles()[i];
                if (fullDataLoadFile.getName().indexOf(".") == -1) {
                    incrDataCopyBatWriter.write("load data local infile '" + fullDataLoadFile.getAbsolutePath().replaceAll("\\\\+", "/") + "' into table " + fullDataLoadFile.getName() + ";");
                    incrDataCopyBatWriter.newLine();
                }
            }
        }
    }
