    public void createDistJar(DistJarConfig distJarConfig, boolean includeUserCredentials) throws IOException, IncorrectKeyException, IllegalAccessException, NoSuchAlgorithmException, KeyStoreException, CertificateException, VisitPCException, ClassNotFoundException, InstantiationException {
        Properties configProperties = new Properties();
        if (distJarConfig.serverName != null && distJarConfig.serverName.trim().length() > 0 && distJarConfig.serverPort > 0) {
            checkServerReachable(distJarConfig.serverName, distJarConfig.serverPort, 30000);
            checkServerNotOnLocalSubnet(distJarConfig.serverName);
        }
        if (!new File(distJarConfig.distJarDialogConfig.outputPath).isDirectory()) {
            throw new IOException(distJarConfig.distJarDialogConfig.outputPath + " jar file output directory not found.");
        }
        File configPropertiesFile = new File(distJarConfig.configDir, SimpleConfigHelper.JAR_CONFIG_FILENAME);
        if (distJarConfig.pm != null) {
            distJarConfig.pm.setMinimum(0);
            distJarConfig.pm.setMaximum(6);
            distJarConfig.pm.setProgress(1);
        }
        if (distJarConfig.tmpDir.isDirectory()) {
            removeDirectory(distJarConfig.tmpDir);
        }
        if (!distJarConfig.tmpDir.mkdir()) {
            throw new IOException("Unable to create the " + distJarConfig.tmpDir + " directory.");
        }
        JarIt jarIt = new JarIt(distJarConfig.visitPCJarFile.getAbsolutePath(), distJarConfig.tmpDir.getAbsolutePath());
        jarIt.decompress();
        if (distJarConfig.pm != null) {
            distJarConfig.pm.setProgress(2);
        }
        removeDirectory(distJarConfig.srvMgrDir);
        if (!distJarConfig.distJarDialogConfig.jarTypeList.equals(DistJarDialogConfig.VNC_DEST_CLIENT)) {
            removeDirectory(distJarConfig.vncServerDir);
        }
        if (!distJarConfig.configDir.mkdir()) {
            throw new IOException("Unable to create the " + distJarConfig.configDir + " directory.");
        }
        addGenericConfig(distJarConfig.distJarDialogConfig, configProperties, distJarConfig.configDir, SimpleConfigHelper.OBFUSRACTION_KEY);
        if (distJarConfig.trustStoreFile != null && distJarConfig.trustStoreFile.trim().length() > 0) {
            String trustStoreFilename = copyTrustStoreFile(distJarConfig.trustStoreFile, distJarConfig.configDir.getAbsolutePath());
            configProperties.put(SimpleConfigHelper.CLIENT_TRUSTSTORE_FILE_KEY, SimpleConfigHelper.JAR_CONFIG_PATH + trustStoreFilename);
        }
        copyEmbeddedConfigFile(distJarConfig.embeddedConfigFile, distJarConfig.configDir, distJarConfig.defaultEncryptKey, SimpleConfigHelper.OBFUSRACTION_KEY, includeUserCredentials, distJarConfig.distJarDialogConfig.clearPCName);
        File manifestDir = new File(distJarConfig.tmpDir, "META-INF");
        File manifestFile = new File(manifestDir, "MANIFEST.MF");
        if (!manifestFile.isFile()) {
            throw new IOException(manifestFile + " file not found.");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(manifestFile));
        bw.write("Manifest-Version: 1.0" + System.getProperty("line.separator"));
        bw.write("Main-Class: " + distJarConfig.startupClass + System.getProperty("line.separator"));
        bw.close();
        if (distJarConfig.pm != null) {
            distJarConfig.pm.setProgress(3);
        }
        Vector<String> configFileList = new Vector<String>();
        File destFile = new File(distJarConfig.configDir, "." + distJarConfig.embeddedConfigFile);
        configFileList.add(SimpleConfigHelper.JAR_CONFIG_PATH + destFile.getName());
        if (distJarConfig.distJarDialogConfig.logoFile != null && distJarConfig.distJarDialogConfig.logoFile.length() > 0) {
            File lcf = new File(distJarConfig.distJarDialogConfig.logoFile);
            configProperties.put(SimpleConfigHelper.LOGO_IMAGE_FILE_KEY, SimpleConfigHelper.JAR_CONFIG_PATH + lcf.getName());
        }
        if (distJarConfig.distJarDialogConfig.smallLogoFile != null && distJarConfig.distJarDialogConfig.smallLogoFile.length() > 0) {
            File slcf = new File(distJarConfig.distJarDialogConfig.smallLogoFile);
            configProperties.put(SimpleConfigHelper.SMALL_LOGO_IMAGE_FILE_KEY, SimpleConfigHelper.JAR_CONFIG_PATH + slcf.getName());
        }
        bw = new BufferedWriter(new FileWriter(configPropertiesFile));
        int index = 0;
        StringBuffer strBuffer = new StringBuffer();
        for (String configFile : configFileList) {
            if (index == 0) {
                strBuffer.append(configFile);
            } else {
                strBuffer.append("\t" + configFile);
            }
            index++;
        }
        configProperties.put(SimpleConfigHelper.FILES_KEY, strBuffer.toString());
        configProperties.store(bw, distJarConfig.embeddedConfigFile);
        bw.close();
        if (distJarConfig.distJarDialogConfig.jarTypeList.equals(DistJarDialogConfig.VNC_DEST_CLIENT)) {
            if (PlatformHandler.GetOSName().startsWith("Windows")) {
                File srcFile = VNCDestClient.GetVNCServerINI();
                if (!srcFile.isFile()) {
                    throw new IOException(srcFile + " file not found.");
                }
                destFile = new File(distJarConfig.tmpDir.toString() + "\\visitpc\\destclient\\vncserver\\win\\ultravnc.ini");
                if (!destFile.isFile()) {
                    throw new IOException(destFile + " file not found.");
                }
                if (!destFile.delete()) {
                    throw new IOException("Failed to delete " + destFile);
                }
                FileIO.CopyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            }
        }
        try {
            if (distJarConfig.pm != null) {
                distJarConfig.pm.setProgress(4);
            }
            jarIt.createJar(distJarConfig.tmpDir, distJarConfig.jarFile.toString());
            if (distJarConfig.pm != null) {
                distJarConfig.pm.setProgress(5);
            }
            if (distJarConfig.webServerConfig != null) {
                copyToWebServer(distJarConfig);
            }
        } finally {
            if (distJarConfig.pm != null) {
                distJarConfig.pm.setProgress(6);
            }
        }
    }
