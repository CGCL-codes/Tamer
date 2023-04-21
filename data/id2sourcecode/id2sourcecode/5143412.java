    private String prepareUserFeedbackRequestBody(String userEMail, String userMessage) {
        VelocityContext ctx = new VelocityContext();
        ctx.put("appFullName", appManager.getFullAppName());
        ctx.put("appInstallationUid", appManager.getAppInstallationUid());
        ctx.put("userEMail", userEMail);
        try {
            ctx.put("base64EncodedUserMessage", encodeBase64String(userMessage.getBytes("UTF-8")).trim());
        } catch (UnsupportedEncodingException e) {
            throw createRuntimeException(e);
        }
        Reader reader;
        try {
            reader = new InputStreamReader(getClass().getResourceAsStream("user_feedback_request.xml.vm"), "UTF-8");
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
