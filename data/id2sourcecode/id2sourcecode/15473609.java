    private String fillTemplate(VelocityContext ctx, String templateResourceName) {
        Reader reader;
        try {
            reader = new InputStreamReader(getClass().getResourceAsStream(templateResourceName), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw createRuntimeException(e);
        }
        StringWriter writer = new StringWriter();
        try {
            boolean isOk = Velocity.evaluate(ctx, writer, VELOCITY_LOG4J_APPENDER_NAME, reader);
            isTrue(isOk);
            writer.close();
            return writer.toString();
        } catch (IOException e) {
            throw createRuntimeException(e);
        }
    }
