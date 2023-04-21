    protected void deployPluginJARs() throws IOException {
        File devLibDir = getDevLib();
        if (!devLibDir.exists()) {
            devLibDir.mkdirs();
        }
        File tmpDevLibDir = new File(getPluginTmpDir(), "tmpDevLib");
        if (tmpDevLibDir.exists()) {
            FileUtils.deleteDirectory(tmpDevLibDir);
        }
        tmpDevLibDir.mkdirs();
        Set<File> newSourceJars = new HashSet<File>();
        for (final PluginDescriptor pluginDescriptor : getManager().getRegistry().getPluginDescriptors()) {
            ExtensionPoint devLibExtensionPoint = pluginDescriptor.getExtensionPoint(EXTENSIONPOINT_DEVLIB);
            if (devLibExtensionPoint != null) {
                Set<File> sourceJars = new HashSet<File>();
                for (ParameterDefinition parameterDefinition : devLibExtensionPoint.getParameterDefinitions()) {
                    if ("jarDir".equals(parameterDefinition.getId())) {
                        String defaultJARDirValue = parameterDefinition.getDefaultValue();
                        File jarDir = getFilePath(pluginDescriptor, defaultJARDirValue);
                        if (!jarDir.exists()) {
                            throw new RuntimeException(pluginDescriptor.getUniqueId() + " does not contain the directory called: " + defaultJARDirValue);
                        }
                        for (String sourceJarname : jarDir.list(new SuffixFileFilter("jar"))) {
                            File sourceJar = new File(jarDir.getPath(), sourceJarname);
                            sourceJars.add(sourceJar);
                        }
                    } else {
                        String defaultJARValue = parameterDefinition.getDefaultValue();
                        File sourceJar = getFilePath(pluginDescriptor, defaultJARValue);
                        if (!sourceJar.exists()) {
                            throw new RuntimeException("Could not locate: " + sourceJar.getPath() + " for plugin: " + pluginDescriptor.getId());
                        }
                        sourceJars.add(sourceJar);
                    }
                }
                for (File sourceJar : sourceJars) {
                    String extendedSourceJarname = getExtendedJarname(sourceJar.getName(), pluginDescriptor);
                    File extendSourceJar = new File(tmpDevLibDir, extendedSourceJarname);
                    FileUtils.copyFile(sourceJar, extendSourceJar);
                    newSourceJars.add(extendSourceJar);
                }
            }
        }
        if (devLibDir.exists()) {
            FileUtils.deleteDirectory(devLibDir);
        }
        for (File newSourceJar : newSourceJars) {
            logger.debug("Add to development library " + newSourceJar.getPath());
            FileUtils.moveFileToDirectory(newSourceJar, devLibDir, true);
        }
    }
