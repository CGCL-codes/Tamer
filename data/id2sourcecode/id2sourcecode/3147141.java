    public void process(File xmlFile) {
        try {
            Project project = parseProject(xmlFile);
            if (project.getProcessInfo() == null) {
                throw new ProcessException("No process specified.");
            }
            if (project.getProcessInfo() instanceof GenerationProcessInfo) {
                generateData(project);
            } else if (project.getProcessInfo() instanceof ReadMetadataProcessInfo) {
                ReadMetadataProcessInfo readMetadataProcessInfo = (ReadMetadataProcessInfo) project.getProcessInfo();
                readMetadata(project);
                writeProject(project, readMetadataProcessInfo.getOutputXmlFile());
            }
        } catch (Exception ex) {
            throw new ProcessException(ex);
        }
    }
