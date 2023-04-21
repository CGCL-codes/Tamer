    public static void main(String argv[]) throws Throwable {
        RecordImporter ri = new RecordImporter(RecordInfo.getRecordInfo(argv[0]));
        File dir = new File(argv[1]);
        String[] lst = dir.list();
        char buffer[] = new char[8196];
        for (int i = 0; i < lst.length; i++) {
            MakumbaSystem.getMakumbaLogger("import").finest(lst[i]);
            Reader r = new FileReader(new File(dir, lst[i]));
            StringWriter sw = new StringWriter();
            int n;
            while ((n = r.read(buffer)) != -1) sw.write(buffer, 0, n);
            String content = sw.toString().toString();
            MakumbaSystem.getMakumbaLogger("import").finest(ri.importFrom(content, null, null).toString());
        }
    }
