    private void copyWsdl() throws EnhancedException {
        FileUtils.createDirectory(outputDir + "//" + copyWsdlDir);
        FileUtils.copyFile(inputFile, new File(outputDir + "//" + copyWsdlDir + "//" + inputFile.getName()));
        context.put("wsdl_url", copyWsdlDir + "/" + inputFile.getName());
    }
