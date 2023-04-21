    public void write(List<MessageHelper> messages) throws CoreException {
        if (hasEnums(messages, SEVERITY_ERROR)) {
            ClassHelper clazzError = generateClassMessage(CLASS_NAME_ERRORMESSAGE, SEVERITY_ERROR, messages);
            FileUtil.writeClassFile(path, clazzError, false, false);
        }
        if (hasEnums(messages, SEVERITY_FATAL)) {
            ClassHelper clazzFatal = generateClassMessage(CLASS_NAME_FATALMESSAGE, SEVERITY_FATAL, messages);
            FileUtil.writeClassFile(path, clazzFatal, false, false);
        }
        if (hasEnums(messages, SEVERITY_INFO)) {
            ClassHelper clazzInfo = generateClassMessage(CLASS_NAME_INFOMESSAGE, SEVERITY_INFO, messages);
            FileUtil.writeClassFile(path, clazzInfo, false, false);
        }
        Configurator reader = new Configurator();
        reader.writeMessagePackage(packageName, xml, false);
        EclipseUtil.updateProject();
    }
