            public void run() throws IOException {
                antProjectHelper[0] = createProject(name, projectFolderFO, "src", "build", flexSdk, mainClass);
                final FlexProject flexProject = (FlexProject) ProjectManager.getDefault().findProject(projectFolderFO);
                ProjectManager.getDefault().saveProject(flexProject);
                final FileObject srcFolderFO = projectFolderFO.createFolder("src");
                FileObject dest = projectFolderFO.createData(GeneratedFilesHelper.BUILD_XML_PATH);
                final URL url = FlexProjectGenerator.class.getResource("resources/build-impl.xml");
                InputStream in = url.openStream();
                OutputStream out = dest.getOutputStream();
                FileUtil.copy(in, out);
                in.close();
                out.close();
                if (mainClass != null) {
                    createMainClass(srcFolderFO, mainClass);
                }
                return;
            }
