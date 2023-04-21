    private void saveTableFiles(File targetDirectory) throws Exception {
        URL url = ExportPlugin.getDefault().getBundle().getEntry(templateDirectory + File.separator + "templates" + File.separator + "table.html");
        int tableCounter = 1;
        for (Iterator<ITable> it = tablesList.iterator(); it.hasNext(); ) {
            VelocityContext context = new VelocityContext();
            File targetFile = new File(targetDirectory, "tables" + File.separator + "table_" + tableCounter + ".html");
            if (!targetFile.exists()) targetFile.createNewFile();
            context.put("columns", getColumnList((ITable) it.next()));
            FileWriter writer = new FileWriter(targetFile, false);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            Velocity.evaluate(context, writer, "RMBench HTML EXport", reader);
            writer.close();
            reader.close();
            tableCounter++;
        }
    }
