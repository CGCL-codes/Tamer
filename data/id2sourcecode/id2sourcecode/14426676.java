    public void writePackage() throws CoreException {
        Configurator reader = new Configurator();
        reader.writeMessagePackage(packageName, xml, false);
        EclipseUtil.updateProject();
    }
