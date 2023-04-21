    private void _createBuildXml() throws IOException {
        final FileObject projectFolderFO = _antProjectHelper.getProjectDirectory();
        projectFolderFO.getFileSystem().runAtomicAction(new FileSystem.AtomicAction() {

            public void run() throws IOException {
                FileObject dest = projectFolderFO.getFileObject("build.xml");
                final URL url = AntProjectHelperBuilder.class.getResource("resources/build-impl.xml");
                InputStream in = url.openStream();
                OutputStream out = dest.getOutputStream();
                FileUtil.copy(in, out);
                in.close();
                out.close();
            }
        });
    }
