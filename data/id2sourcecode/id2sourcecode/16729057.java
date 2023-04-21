    public void process(Schema prevSchema, Schema currSchema, String outDir) throws Exception {
        logger.info("Generating delta report...");
        String path = new File("templates/delta").getAbsolutePath();
        StringTemplateGroup templates = new StringTemplateGroup("tg", path);
        StringTemplate template = templates.getInstanceOf("delta");
        populateSequences(template, prevSchema, currSchema);
        populateTables(template, prevSchema, currSchema);
        String delta = template.toString();
        try {
            FileUtils.writeStringToFile(new File(outDir, "delta_report.html"), delta);
            FileUtils.copyFileToDirectory(new File("templates/delta/main.css"), new File(outDir));
        } catch (IOException ex) {
            throw new DbxFormatterException(ex);
        }
        logger.info("Delta report has been generated.");
    }
