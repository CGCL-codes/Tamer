    protected void assembleTolvenCommonJar() throws IOException {
        ExtensionPoint tolvenCommonJarExtensionPoint = getDescriptor().getExtensionPoint(EXTENSIONPOINT_TOLVENCOMMON_JAR);
        ExtensionPoint tolvenCommonJARParentExtensionPoint = getParentExtensionPoint(tolvenCommonJarExtensionPoint);
        PluginDescriptor tolvenCommonJarPluginDescritor = tolvenCommonJARParentExtensionPoint.getDeclaringPluginDescriptor();
        String tolvenCommonJarname = tolvenCommonJARParentExtensionPoint.getParameterDefinition("tolvenCommon").getDefaultValue();
        File sourceTolvenCommonJar = getFilePath(tolvenCommonJarPluginDescritor, tolvenCommonJarname);
        String sourceDigest = TolvenMessageDigest.checksum(sourceTolvenCommonJar.toURI().toURL(), MESSAGE_DIGEST_ALGORITHM);
        String appserverHome = (String) evaluate("#{globalProperty['appserver.home']}", getDescriptor());
        File appserverHomeDir = new File(appserverHome);
        if (!appserverHomeDir.exists()) {
            throw new RuntimeException("Could not find appserver home directory: " + appserverHomeDir.getPath());
        }
        File stageDirAppserverHomeDir = new File(getStageDir(), appserverHomeDir.getName());
        File stageAppserverLibDir = new File(stageDirAppserverHomeDir, LIBDIR);
        File destTolvenCommonJar = new File(stageAppserverLibDir, sourceTolvenCommonJar.getName());
        String destDigest = null;
        if (destTolvenCommonJar.exists()) {
            destDigest = TolvenMessageDigest.checksum(destTolvenCommonJar.toURI().toURL(), MESSAGE_DIGEST_ALGORITHM);
        }
        if (destDigest == null || !destDigest.equals(sourceDigest)) {
            logger.debug("Copy " + sourceTolvenCommonJar.getPath() + " to " + destTolvenCommonJar.getPath());
            FileUtils.copyFile(sourceTolvenCommonJar, destTolvenCommonJar);
        }
    }
