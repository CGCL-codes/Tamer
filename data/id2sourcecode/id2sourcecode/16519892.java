    private void checkRequiresFiles(IWorkspace mIWorkspace) {
        File workSpaceLocation = m_iWorkspace.getRoot().getLocation().toFile();
        File commoreSettingDir = new File(workSpaceLocation, ".commore_plugin");
        commoreSettingDir.mkdirs();
        m_javaGeneratorHelperUsed = new File(commoreSettingDir, "java_generator_helper.jar");
        if (!m_javaGeneratorHelperUsed.exists()) {
            InputStream javaGeneratorHelperInJar = GenerateFromIdl.class.getResourceAsStream("/ressources/java_generator_helper.jar");
            FileOutputStream dest;
            try {
                dest = new FileOutputStream(m_javaGeneratorHelperUsed);
                FileUtils.copyFile(javaGeneratorHelperInJar, dest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        m_javaGeneratorExeUsed = new File(commoreSettingDir, "java_generator.exe");
        if (!m_javaGeneratorExeUsed.exists()) {
            InputStream javaGeneratorExeInJar = GenerateFromIdl.class.getResourceAsStream("/ressources/java_generator.exe");
            FileOutputStream dest;
            try {
                dest = new FileOutputStream(m_javaGeneratorExeUsed);
                FileUtils.copyFile(javaGeneratorExeInJar, dest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        m_commoreJarUsed = new File(commoreSettingDir, "commore.jar");
        if (!m_commoreJarUsed.exists()) {
            InputStream commoreJarUsedInJar = GenerateFromIdl.class.getResourceAsStream("/ressources/commore.jar");
            FileOutputStream dest;
            try {
                dest = new FileOutputStream(m_commoreJarUsed);
                FileUtils.copyFile(commoreJarUsedInJar, dest);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
