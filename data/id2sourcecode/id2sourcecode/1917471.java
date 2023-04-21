    public File convert(File inputFile, File outputFile) throws IOException {
        try {
            if (inputFile.getName().endsWith(".txt")) {
                Charset fileCharset = FileUtils.getFileEncoding(inputFile);
                if (fileCharset != null) {
                    Charset systemCharset = Charset.defaultCharset();
                    if (!fileCharset.equals(systemCharset) && !(systemCharset.equals(Charset.forName("GBK")) && fileCharset.name().toLowerCase().equals("gb2312"))) {
                        String encodedFileName = FileUtils.getFilePrefix(inputFile.getPath()) + "_encoded." + (this.isWindows() ? "odt" : "txt");
                        File encodedFile = new File(encodedFileName);
                        try {
                            FileUtils.convertFileEncodingToSys(inputFile, encodedFile);
                        } catch (Exception e) {
                            org.apache.commons.io.FileUtils.copyFile(inputFile, encodedFile);
                        }
                        inputFile = encodedFile;
                    } else if (isWindows()) {
                        String encodedFileName = FileUtils.getFilePrefix(inputFile.getPath()) + "_encoded.odt";
                        File encodedFile = new File(encodedFileName);
                        org.apache.commons.io.FileUtils.copyFile(inputFile, encodedFile);
                        inputFile = encodedFile;
                    }
                }
            }
            LOGGER.debug("进行文档转换转换:" + inputFile.getPath() + " --> " + outputFile.getPath());
            OfficeDocumentConverter converter = new OfficeDocumentConverter(officeManager);
            converter.convert(inputFile, outputFile);
            LOGGER.debug("文档转换完成:" + inputFile.getPath() + " --> " + outputFile.getPath());
            return outputFile;
        } catch (Exception e) {
            LOGGER.error(e);
        } finally {
        }
        return null;
    }
