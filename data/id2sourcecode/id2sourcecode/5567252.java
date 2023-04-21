    public static String getResourceAsString(ClassLoader loader, String path) throws AgentBuilderRuntimeException {
        java.io.InputStream in = null;
        java.io.Reader reader = null;
        java.io.StringWriter writer = null;
        try {
            in = loader.getResourceAsStream(path);
            reader = new java.io.InputStreamReader(in);
            writer = new java.io.StringWriter();
            repast.simphony.agents.base.IOUtils.copyChars(reader, writer);
            reader.close();
            writer.close();
        } catch (java.io.IOException e) {
            throw new AgentBuilderRuntimeException("Error while loading resource (" + path + ")", e);
        } finally {
            if (in != null) try {
                reader.close();
            } catch (java.io.IOException e) {
            }
            if (reader != null) try {
                reader.close();
            } catch (java.io.IOException e) {
            }
            if (writer != null) try {
                reader.close();
            } catch (java.io.IOException e) {
            }
        }
        return writer.toString();
    }
