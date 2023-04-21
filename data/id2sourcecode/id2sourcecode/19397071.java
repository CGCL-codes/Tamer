    public void testDistributionAntScriptGeneration() throws Exception {
        Xsd2CobModel model = new Xsd2CobModel();
        model.setProductLocation(".");
        model.setProbeFile(new File("probe.file.tmp"));
        model.setInputXsdUri(new URI("schema"));
        model.setTargetXsdFile(new File("cobolschema"));
        model.setTargetCobolFile(new File("cobol"));
        File resultFile = genAntScriptAsFile(model);
        FileUtils.copyFileToDirectory(resultFile, new File("target/gen-distro"));
    }
