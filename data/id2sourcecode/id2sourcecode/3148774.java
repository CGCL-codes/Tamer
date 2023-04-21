    public static void load(AbstractComponent component) throws Exception {
        if (component.isBasicComponent()) return;
        String fileName = component.getClass().getName();
        fileName = fileName.replace('.', '/') + ".ftl";
        Template tpl = configuration.getTemplate(fileName);
        StringWriter swriter = new StringWriter();
        tpl.process(Execution.getCurrent().getModel(), swriter);
        Reader sreader = new StringReader(swriter.toString());
        InputSource is = new InputSource(sreader);
        if (is == null) return;
        ComponentLoader handler = new ComponentLoader(component);
        handler.sp.parse(is, handler);
    }
