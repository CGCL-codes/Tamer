    public static String render(String template, Object data) throws Exception {
        Reader reader = new InputStreamReader(App.class.getClassLoader().getResourceAsStream(template));
        VelocityContext context = new VelocityContext();
        context.put("data", data);
        StringWriter writer = new StringWriter();
        Velocity.evaluate(context, writer, "", reader);
        return writer.toString();
    }
