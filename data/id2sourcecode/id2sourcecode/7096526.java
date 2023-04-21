    public String applyTemplate(String templateResourceName, VelocityContext context) throws ParseErrorException, MethodInvocationException, ResourceNotFoundException, IOException {
        StringWriter writer = new StringWriter();
        StringWriter template = new StringWriter();
        InputStream input = load(templateResourceName);
        InputStreamReader reader = new InputStreamReader(input);
        int c;
        try {
            while ((c = reader.read()) != -1) template.write(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Velocity.evaluate(context, writer, "LOG", template.toString());
        return writer.toString();
    }
