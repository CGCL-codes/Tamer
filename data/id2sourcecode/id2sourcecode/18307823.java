    public static void parse(Reader reader, Writer writer, Map contextMap) throws Exception {
        VelocityContext customContext = new VelocityContext();
        Iterator keys = contextMap.keySet().iterator();
        while (keys.hasNext()) {
            Object key = keys.next();
            customContext.put(key.toString(), contextMap.get(key));
        }
        Velocity.evaluate(customContext, writer, VelocityTool.class.getName(), reader);
    }
