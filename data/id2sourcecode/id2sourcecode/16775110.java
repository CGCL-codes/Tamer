    public static KnownTemplates importFromText(java.io.BufferedReader buff) throws java.io.IOException {
        MergeTemplateWriter writer = new MergeTemplateWriter();
        KnownTemplates workingTemplates = new KnownTemplates();
        while (true) {
            MergeTemplate t = writer.readFrom(buff);
            System.out.println("Template Read " + t);
            if (t == null) {
                break;
            }
            workingTemplates.addTemplate(t);
        }
        writer.realizeTemplateCache(workingTemplates);
        return workingTemplates;
    }
