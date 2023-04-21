            public void run() throws IOException {
                antProjectHelper[0] = _createAntProjectHelper(_projectName, projectFolderFO, "src", "build", _flexSdk, _mainClass);
                final Project flexProject = ProjectManager.getDefault().findProject(projectFolderFO);
                ProjectManager.getDefault().saveProject(flexProject);
                final FileObject srcFolderFO = projectFolderFO.createFolder("src");
                FileObject dest = projectFolderFO.createData(GeneratedFilesHelper.BUILD_XML_PATH);
                final URL url = AntProjectHelperBuilder.class.getResource("resources/build-impl.xml");
                InputStream in = url.openStream();
                OutputStream out = dest.getOutputStream();
                FileUtil.copy(in, out);
                in.close();
                out.close();
                if (_mainClass != null) {
                    _createMainClass(srcFolderFO, _mainClass);
                }
                return;
            }
