    private static void _updateBuildXml(final Project project) throws IOException {
        final FileObject projectFileFolder = project.getProjectDirectory();
        projectFileFolder.getFileSystem().runAtomicAction(new FileSystem.AtomicAction() {

            public void run() throws IOException {
                FileObject dest = projectFileFolder.getFileObject(GeneratedFilesHelper.BUILD_XML_PATH);
                if (dest != null) {
                    dest.delete();
                }
                dest = projectFileFolder.createData(GeneratedFilesHelper.BUILD_XML_PATH);
                final URL url = FlexProjectGenerator.class.getResource("resources/build-impl.xml");
                InputStream in = url.openStream();
                OutputStream out = dest.getOutputStream();
                FileUtil.copy(in, out);
                in.close();
                out.close();
                ProjectManager.getDefault().saveProject(project);
            }
        });
    }
