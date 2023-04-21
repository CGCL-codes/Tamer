    @Override
    protected void deploy() {
        final ConfigurationFactory configurationFactory = new DefaultConfigurationFactory();
        final LocalConfiguration configuration = (LocalConfiguration) configurationFactory.createConfiguration("glassfish2", ContainerType.INSTALLED, ConfigurationType.STANDALONE, containerHome + "cargo-conf/");
        final StringBuilder args = new StringBuilder();
        for (final String arg : jvmArguments) {
            args.append(arg);
            args.append(" ");
            if (log.isInfoEnabled()) {
                log.info("Added JVM argument: " + arg);
            }
        }
        configuration.setProperty(GeneralPropertySet.JVMARGS, args.toString());
        configuration.setProperty(ServletPropertySet.PORT, containerPort.toString());
        final Set<Entry<String, String>> entrySet = deployableLocations.entrySet();
        final Iterator<Entry<String, String>> iterator = entrySet.iterator();
        while (iterator.hasNext()) {
            final Entry<String, String> entry = iterator.next();
            final String key = entry.getKey();
            final String value = entry.getValue();
            DeployableType deployableType = null;
            deployableType = determineDeployableType(value);
            addDeployable(configuration, key, deployableType);
        }
        for (final DeployableLocationConfiguration config : deployableLocationConfigurations) {
            final String contextName = config.getContextName();
            final String type = config.getType();
            String path = config.getPath();
            DeployableType deployableType = null;
            if (contextName != null && contextName.length() > 0) {
                deployableType = determineDeployableType(type);
                if (DeployableType.WAR.equals(deployableType)) {
                    final File srcFile = new File(path);
                    final File destFile = new File("target/" + contextName + ".war");
                    try {
                        FileUtils.copyFile(srcFile, destFile);
                    } catch (final IOException e) {
                        throw new DeployException("Failed to copy WAR file: " + path, e);
                    }
                    path = destFile.getPath();
                }
            } else {
                deployableType = determineDeployableType(type);
            }
            addDeployable(configuration, path, deployableType);
        }
        installedLocalContainer = (InstalledLocalContainer) new DefaultContainerFactory().createContainer("glassfish2", ContainerType.INSTALLED, configuration);
        installedLocalContainer.setHome(containerHome);
        final Logger fileLogger = new FileLogger(new File(cargoLogFilePath + "cargo.log"), true);
        fileLogger.setLevel(LogLevel.DEBUG);
        installedLocalContainer.setLogger(fileLogger);
        installedLocalContainer.setOutput(cargoLogFilePath + "output.log");
        installedLocalContainer.setSystemProperties(systemProperties);
        try {
            completeGlassfishConfiguration();
        } catch (final IOException e) {
            throw new DeployException("Failed to complete the Glassfish configuration while setting the env files", e);
        }
        if (log.isInfoEnabled()) {
            log.info("Starting Glassfish [" + configurationName + "]...");
        }
        installedLocalContainer.start();
        if (log.isInfoEnabled()) {
            log.info("Glassfish up and running!");
        }
    }
