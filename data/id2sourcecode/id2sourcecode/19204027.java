    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: SourceCodeGenerator <templateName> <templateFile> <sourceFile>");
            return;
        }
        String templateName = args[0];
        try {
            java.net.URL url = new FileFinder().getUrl(com.objectwave.tools.GenerateTemplateInto.class, args[1]);
            KnownTemplates temp = KnownTemplates.readStream(url.openStream());
            if (temp == null) {
                throw new RuntimeException("Failed create Knowntemplates from XML file");
            }
            KnownTemplates.setDefaultInstance(temp);
            FileReader fileRdr = new FileReader(args[2]);
            com.objectwave.sourceParser.SourceCodeReader src = new com.objectwave.sourceParser.SourceCodeReader(fileRdr);
            com.objectwave.sourceModel.JavaClassDef def = new com.objectwave.sourceModel.JavaClassDef(src.parseSource());
            ClassInformation ci = new ClassInformation();
            ci.setClassElement(def);
            MergeTemplate template = KnownTemplates.getDefaultInstance().getTemplate(templateName);
            template.generateForOn(ci, System.out);
            System.out.println("");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
