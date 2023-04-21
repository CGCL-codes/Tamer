    protected void createBuildXmlFile(Project project) throws IOException {
        FileObject dest = project.getProjectDirectory().createData(GeneratedFilesHelper.BUILD_XML_PATH);
        final URL url = ProjectType.class.getResource("/org/netbeans/modules/flexbean/project/module/ant/build-impl.xml");
        InputStream in = url.openStream();
        OutputStream out = dest.getOutputStream();
        FileUtil.copy(in, out);
        in.close();
        out.close();
    }
