    public static String replaceTokens(String template, Map<String, Object> params) throws ParseErrorException, ResourceNotFoundException, MethodInvocationException, IOException {
        VelocityContext context = new VelocityContext(params);
        StringWriter writer = new StringWriter();
        StringReader reader = new StringReader(template);
        boolean result = Velocity.evaluate(context, writer, "rule-" + System.currentTimeMillis(), reader);
        String strResult = null;
        if (result) {
            strResult = writer.getBuffer().toString();
        }
        return strResult;
    }
