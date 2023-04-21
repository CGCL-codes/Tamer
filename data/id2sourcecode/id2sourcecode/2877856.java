    public static void main(String argv[]) throws Throwable {
        ObjectImporter ri = new ObjectImporter(DataDefinitionProvider.getInstance().getDataDefinition(argv[0]));
        File dir = new File(argv[1]);
        String[] lst = dir.list();
        char buffer[] = new char[8196];
        for (int i = 0; i < lst.length; i++) {
            java.util.logging.Logger.getLogger("org.makumba." + "import").finest(lst[i]);
            Reader r = new FileReader(new File(dir, lst[i]));
            StringWriter sw = new StringWriter();
            int n;
            while ((n = r.read(buffer)) != -1) sw.write(buffer, 0, n);
            String content = sw.toString().toString();
            java.util.logging.Logger.getLogger("org.makumba." + "import").finest(ri.importFrom(content, null, null).toString());
        }
    }
