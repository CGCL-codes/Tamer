    public static void main(String[] args) {
        TemplateGuiModel model = new TemplateGuiModel();
        if (args.length != 3) {
            System.out.println("Usage java com.objectwave.templateMerge.gui.TemplateGuiModel <TokenProvider> <InputFile> <OutputFile>");
        } else {
            try {
                Class c = Class.forName(args[0]);
                model.setTokenProvider((com.objectwave.templateMerge.TokenProvider) c.newInstance());
                URL url = new FileFinder().getUrl(model.getClass(), args[1]);
                InputStreamReader fr = new InputStreamReader(url.openStream());
                model.importFromXML(fr);
                model.workingTemplates.fileName = args[2];
                model.saveCurrentModel();
            } catch (Exception e) {
                MessageLog.error(TemplateGuiModel.class, "Error with token provider " + e, e);
                System.exit(1);
            }
        }
    }
