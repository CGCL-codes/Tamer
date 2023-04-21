    public static InputStream generateCode(File flowFile, String packagePath, File scriptRootFolder) throws IOException {
        String fileName = flowFile.getName();
        String className = fileName.substring(0, fileName.lastIndexOf("."));
        AgentModelBind agentModelBind = BindingHandler.getInstance().loadFlowModel(flowFile, new String().getClass().getClassLoader());
        JavaSrcModelDigester digester = new JavaSrcModelDigester(className, packagePath, scriptRootFolder, agentModelBind.getComment(), agentModelBind.getImports(), agentModelBind.getInterfaces(), agentModelBind.getParentClassName());
        digester.digest(agentModelBind);
        digester.generateClassSrc(agentModelBind);
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);
        digester.serialize(writer);
        writer.close();
        String source = stringWriter.toString();
        try {
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(source.getBytes());
    }
