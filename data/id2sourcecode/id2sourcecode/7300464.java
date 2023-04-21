    public static TemplateTree applyArroba(String sequences, InputStream template) throws ingenias.exception.NotWellFormed, Exception {
        String readFile = readFile(template);
        TemplateTree tags = new TemplateTree(new Tag("root", 0, 0));
        readFile = ingenias.generator.util.Conversor.convertArrobaFormat(readFile);
        File output = File.createTempFile("ingenias", "_tmp");
        FileOutputStream fos = new FileOutputStream(output);
        fos.write(readFile.getBytes());
        Vector templateData = obtainTemplateData(sequences);
        File tempFile = File.createTempFile("idk", "cgen");
        PrintWriter pw = new PrintWriter(new FileOutputStream(tempFile));
        try {
            TemplateHandler.process(output.getAbsolutePath(), templateData, pw, tags);
        } catch (org.xml.sax.SAXParseException spe) {
            ingenias.editor.Log.getInstance().logERROR("Parser error at " + ((SAXParseException) spe).getLineNumber() + ":" + ((SAXParseException) spe).getColumnNumber() + " " + spe.getMessage());
            throw new ingenias.exception.NotWellFormed();
        }
        pw.close();
        ingenias.editor.Log.getInstance().logSYS("Processing " + template);
        Codegen.decompose(tempFile.getPath());
        fos.close();
        tempFile.delete();
        return tags;
    }
