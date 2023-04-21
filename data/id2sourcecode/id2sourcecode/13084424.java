    public void saveIndexHtml(File targetDirectory) throws Exception {
        URL url;
        if (templateType == THREE_FRAME_TEMPLATE) {
            url = ExportPlugin.getDefault().getBundle().getEntry(templateDirectory + File.separator + "templates" + File.separator + "index.html");
        } else {
            url = ExportPlugin.getDefault().getBundle().getEntry(templateDirectory + File.separator + "templates" + File.separator + "index2.html");
        }
        VelocityContext context = new VelocityContext();
        context.put("modelname", modelExport.getModel().getName());
        context.put("schemas", getSchemaList());
        context.put("diagrams", getDiagramList(targetDirectory));
        File targetFile = new File(targetDirectory, "index.html");
        if (!targetFile.exists()) targetFile.createNewFile();
        FileWriter writer = new FileWriter(targetFile, false);
        InputStreamReader reader = new InputStreamReader(url.openStream());
        Velocity.evaluate(context, writer, "RMBench HTML EXport", reader);
        writer.close();
        reader.close();
    }
