    protected CodeWriter createWriter() throws ConfigurationException {
        String classname = getConfiguration().getString("generator.writer[@class]");
        try {
            Object obj = ClassUtils.executeConstructor(classname);
            if (!(obj instanceof CodeWriter)) throw new ConfigurationException("Invalid writer class, It's not a CodeWriter");
            CodeWriter writer = (CodeWriter) obj;
            Properties properties = ConfigurationHelper.readProperties(getConfiguration().subset("generator.writer.properties"));
            writer.setProperties(properties);
            return writer;
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }
