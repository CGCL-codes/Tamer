    public void convertImage(String svgFilePath, List<ReplacementValue> replacementValues, String destination, String fileType) throws Exception {
        File svgFile = new File(svgFilePath);
        File tempSvgFile = new File(System.getProperty("java.io.tmpdir") + "/" + svgFile.getName());
        FileUtils.copyFile(svgFile, tempSvgFile);
        Document svgDocument = loadSVGDocument(tempSvgFile.getAbsolutePath());
        processReplacing(svgDocument, replacementValues);
        saveSVGDocument(svgDocument, tempSvgFile.getAbsolutePath());
        SVG2PNG(tempSvgFile.getAbsolutePath(), destination, fileType);
    }
