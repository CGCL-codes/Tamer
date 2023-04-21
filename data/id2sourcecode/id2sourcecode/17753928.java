    public void applyTemplate(String templateFilePath, VelocityContext context, Writer writer) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
        StringWriter template = new StringWriter();
        InputStream input = null;
        if (input == null) {
            input = getClass().getResourceAsStream(templateFilePath);
        }
        InputStreamReader reader = new InputStreamReader(input);
        context.put("FormatterUtil", new FormatterUtil());
        int c;
        try {
            while ((c = reader.read()) != -1) template.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Velocity.evaluate(context, writer, "LOG", template.toString());
    }
