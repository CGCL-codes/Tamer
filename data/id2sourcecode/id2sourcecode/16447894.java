    public void writeTracks(OutputStream out, List tracks) throws IOException {
        URL template_url = getTemplateUrl();
        InputStream template_in = template_url.openStream();
        VelocityContext context = new VelocityContext();
        context.put("printtracks", Boolean.TRUE);
        context.put("printwaypoints", Boolean.FALSE);
        context.put("printroutes", Boolean.FALSE);
        context.put("tracks", tracks);
        addDefaultValuesToContext(context);
        Reader template_reader = new InputStreamReader(template_in);
        Writer out_writer = new OutputStreamWriter(out);
        printTemplate(template_url.getFile(), context, template_reader, out_writer);
        out_writer.flush();
        out_writer.close();
    }
