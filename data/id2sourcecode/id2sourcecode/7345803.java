    public void testDistributionAntScriptGeneration() throws Exception {
        Java2CobModel model = new Java2CobModel();
        model.setProductLocation(".");
        model.setProbeFile(new File("probe.file.tmp"));
        List<String> classNames = Arrays.asList(new String[] { "com.legstar.xsdc.test.cases.jvmquery.JVMQueryRequest", "com.legstar.xsdc.test.cases.jvmquery.JVMQueryReply" });
        model.setClassNames(classNames);
        model.setTargetXsdFile(new File("cobolschema"));
        model.setTargetCobolFile(new File("cobol"));
        List<String> pathElementLocations = Arrays.asList(new String[] { "${basedir}/java/legstar-test-jvmquery-classes.jar" });
        model.setPathElementLocations(pathElementLocations);
        File resultFile = genAntScriptAsFile(model);
        FileUtils.copyFileToDirectory(resultFile, new File("target/gen-distro"));
    }
