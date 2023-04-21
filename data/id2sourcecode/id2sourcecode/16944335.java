    public void importFunction(boolean importVar) {
        String shellPath = path + File.separator + "scenario" + File.separator + "shell";
        File shellFile = new File(shellPath);
        if (shellFile.exists() && shellFile.isDirectory()) {
            if (FileUtil.isLinux()) {
                shellFile.listFiles(new FileFilter() {

                    public boolean accept(File pathname) {
                        if (pathname.isDirectory() && pathname.getName().endsWith("_win")) {
                            FileDeleteUtil.deleteFileAndFolder(pathname.getAbsolutePath());
                        }
                        return false;
                    }
                });
            } else {
                shellFile.listFiles(new FileFilter() {

                    public boolean accept(File pathname) {
                        if (pathname.isDirectory() && !pathname.getName().endsWith("_win")) {
                            FileDeleteUtil.deleteFileAndFolder(pathname.getAbsolutePath());
                        }
                        return false;
                    }
                });
            }
        }
        File f = new File(path);
        Element e = FileUtil.createDesc(f);
        Document d = DocumentHelper.createDocument();
        d.add(e);
        FileUtil.addProcedure(d);
        File descFile = new File(FilenameUtils.concat(path, "scenario.xml"));
        xmlFilePath = descFile.getPath();
        OutputStream os = null;
        try {
            os = new FileOutputStream(descFile);
            String str = d.asXML();
            os.write(str.getBytes("utf8"));
        } catch (Exception e1) {
            System.out.println(e1.getStackTrace());
        } finally {
            Closer.close(os);
        }
        if (importVar) {
            if (true == CubridqaEnvImport.importVar(path)) {
                String proppath = "";
                if (path.endsWith("/")) {
                    proppath = path + "qatool_bin/qamanager/properties/local.properties";
                } else {
                    proppath = path + "/qatool_bin/qamanager/properties/local.properties";
                }
                if (path.endsWith("/")) {
                    PropertiesUtil.setValue("local.path", path.replaceAll("\\\\", "/").replaceAll("\\:", ":"), proppath);
                } else {
                    PropertiesUtil.setValue("local.path", path.replaceAll("\\\\", "/").replaceAll("\\:", ":") + "/", proppath);
                }
                String[] strings = path.replaceAll("\\\\", "/").split("/");
                PropertiesUtil.setValue("rootnode.name", strings[strings.length - 1], proppath);
                String message = "Import Environment Variables OK!\r\n";
                message += "Please restart your qatool";
                MessageDialog.openInformation(null, "Import Env Var OK!", message);
            }
            ;
        } else {
            buildTree();
            if (tree.getItemCount() > 0) {
                newMenuItem.setEnabled(true);
            }
        }
        File configurationTemplate = new File(FilenameUtils.concat(path, "configuration_template"));
        File configuration = new File(FilenameUtils.concat(path, "configuration"));
        if (!configuration.exists()) {
            configuration.mkdir();
        }
        File[] configurationFiles = configuration.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return !".svn".equalsIgnoreCase(name);
            }
        });
        if (configurationFiles.length == 0) {
            File[] configurations = configurationTemplate.listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return !".svn".equalsIgnoreCase(name);
                }
            });
            for (File file : configurations) {
                try {
                    if (file.isFile()) {
                        FileUtils.copyFileToDirectory(file, configuration);
                    } else {
                        FileUtils.copyDirectoryToDirectory(file, configuration);
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        String propertiesPath = path;
        if (!propertiesPath.endsWith("/")) {
            propertiesPath += "/";
        }
        propertiesPath += "qatool_bin/qamanager/properties/local.properties";
        File propFile = new File(propertiesPath);
        if (!propFile.exists()) {
            MessageDialog.openError(null, "PROPERTIES_PATH  ERROR!", "PROPERTIES_PATH environment variable error, not found local.properties, please set it again!");
        }
    }
