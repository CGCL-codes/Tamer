    private void processTemplate(String templateName, File output, VelocityContext context) throws Exception {
        StringWriter writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader(HTMLGenerator.class.getResourceAsStream(templateName), "UTF-8");
        Velocity.evaluate(context, writer, null, reader);
        FileOutputStream out = new FileOutputStream(output);
        out.write(writer.getBuffer().toString().getBytes("UTF-8"));
        IOUtils.close(out);
        IOUtils.close(reader);
        IOUtils.close(writer);
    }
