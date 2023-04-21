    public static void main(String[] args) throws Exception {
        File dir = new File(".");
        TestCompiler tc = new TestCompiler(dir, null, dir);
        tc.addErrorListener(new ConsoleErrorReporter(System.out));
        tc.setForceCompile(true);
        String[] names = tc.compile(args[0]);
        System.out.println("Compiled " + names.length + " sources");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i]);
        }
        int errorCount = tc.getErrorCount();
        if (errorCount > 0) {
            String msg = String.valueOf(errorCount) + " error";
            if (errorCount != 1) {
                msg += 's';
            }
            System.out.println(msg);
            return;
        }
        TemplateLoader loader = new TemplateLoader();
        TemplateLoader.Template template = loader.getTemplate(args[0]);
        int length = args.length - 1;
        Object[] params = new Object[length];
        for (int i = 0; i < length; i++) {
            params[i] = args[i + 1];
        }
        System.out.println("Executing " + template);
        template.execute(new Context(System.out), params);
    }
