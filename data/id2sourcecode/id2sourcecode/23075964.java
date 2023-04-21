    private void checkPropertiesPath() {
        Map<String, String> env = EnvGetter.getenv();
        System.out.println("================= check  PROPERTIES_PATH and QA_REPOSITORY ====================== ");
        if (!env.containsKey("PROPERTIES_PATH") || !env.containsKey("QA_REPOSITORY")) {
            MessageDialog.openError(null, "PROPERTIES_PATH NOT EXIST OR QA_REPOSITORY NOT EXIST!", "PROPERTIES_PATH or QA_REPOSITORY environment variable not exist, please set!");
        } else {
            System.out.println("================= PROPERTIES_PATH or QA_REPOSITORY is exists ====================== ");
            File configurationTemplate = new File(FilenameUtils.concat(EnvGetter.getenv("QA_REPOSITORY"), "configuration_template"));
            File configuration = new File(FilenameUtils.concat(EnvGetter.getenv("QA_REPOSITORY"), "configuration"));
            if (!configuration.exists()) {
                configuration.mkdir();
            }
            FileNameFilter fileNameFilter = new FileNameFilter();
            File[] configurationFiles = configuration.listFiles(fileNameFilter);
            if (configurationFiles.length == 0) {
                File[] configurations = configurationTemplate.listFiles(fileNameFilter);
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
            StringBuilder sb = new StringBuilder(EnvGetter.getenv("PROPERTIES_PATH"));
            sb.append("/properties/local.properties");
            File propFile = new File(sb.toString());
            System.out.println("================= make file $PROPERTIES_PATH/properties/local.properties ====================== ");
            if (!propFile.exists()) {
                MessageDialog.openError(null, "PROPERTIES_PATH  ERROR!", "PROPERTIES_PATH environment variable error, please set it again!");
            } else {
                boolean importCubrid = false;
                String localPath = PropertiesUtil.getValue("local.path");
                System.out.println("================= localPath=" + localPath + " ====================== ");
                if (null == localPath || "".equals(localPath)) {
                    importCubrid = true;
                } else {
                    File dir = new File(localPath);
                    if (!dir.isDirectory() || !dir.exists()) {
                        importCubrid = true;
                    }
                }
                if (importCubrid) {
                    String qa_repository_path = EnvGetter.getenv("QA_REPOSITORY");
                    System.out.println("================= make file $QA_REPOSITORY ====================== ");
                    if (new File(qa_repository_path).exists() && new File(qa_repository_path).isDirectory()) {
                        qa_repository_path = qa_repository_path.replaceAll("\\\\", "/");
                        if (!qa_repository_path.endsWith("/")) {
                            qa_repository_path = qa_repository_path + "/";
                        }
                        PropertiesUtil.setValue("local.path", qa_repository_path);
                    }
                } else {
                    System.out.println("--- all ok! ---");
                }
            }
        }
    }
