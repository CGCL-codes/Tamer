    public void addOptionsPropertiesFile(ZipOutputStream zos) throws IOException {
        Properties optionsProperties = new Properties();
        ResourceBundle bundle = ResourceBundle.getBundle(edu.rice.cs.drjava.DrJava.RESOURCE_BUNDLE_NAME);
        String customDrJavaJarVersionSuffix = "";
        Enumeration<String> keyEn = bundle.getKeys();
        while (keyEn.hasMoreElements()) {
            String key = keyEn.nextElement();
            String value = bundle.getString(key);
            if (key.equals(OptionConstants.CUSTOM_DRJAVA_JAR_VERSION_SUFFIX.getName())) {
                customDrJavaJarVersionSuffix = value;
            } else if (key.equals(OptionConstants.NEW_VERSION_NOTIFICATION.getName())) {
            } else if (key.equals(OptionConstants.NEW_VERSION_ALLOWED.getName())) {
            } else {
                optionsProperties.setProperty(key, value);
            }
        }
        StringBuilder sb = new StringBuilder(customDrJavaJarVersionSuffix);
        for (File f : _sourcesList.getValue()) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(f.getName());
        }
        optionsProperties.setProperty(OptionConstants.CUSTOM_DRJAVA_JAR_VERSION_SUFFIX.getName(), sb.toString());
        optionsProperties.setProperty(OptionConstants.NEW_VERSION_ALLOWED.getName(), "false");
        optionsProperties.setProperty(OptionConstants.NEW_VERSION_NOTIFICATION.getName(), OptionConstants.VersionNotificationChoices.DISABLED);
        zos.putNextEntry(new ZipEntry(OPTIONS_PROPERTIES_FILENAME));
        optionsProperties.store(zos, "Custom drjava.jar file generated " + new Date());
    }
