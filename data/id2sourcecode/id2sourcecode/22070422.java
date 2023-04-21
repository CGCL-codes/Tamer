    private static void upgradeConfigDir(File configDir, File installDir, String templatePluginsXML, File bootPropertiesTemplate, File repositoryLocalTemplate) throws IOException {
        File pluginsXML = new File(configDir, "plugins.xml");
        if (pluginsXML.exists()) {
            System.out.println(pluginsXML + " exists, and will NOT be overwritten");
        } else {
            templatePluginsXML = templatePluginsXML.replace("your-installationDir", installDir.getPath().replace("\\", "/"));
            templatePluginsXML = templatePluginsXML.replace("your-tolven-configDir", configDir.getPath().replace("\\", "/"));
            System.out.println("Writing template plugins.xml to: " + pluginsXML);
            FileUtils.writeStringToFile(pluginsXML, templatePluginsXML);
        }
        File bootProperties = new File(configDir, bootPropertiesTemplate.getName());
        if (bootProperties.exists()) {
            System.out.println(bootProperties + " exists, and will NOT be replaced by: " + bootPropertiesTemplate);
        } else {
            System.out.println("copy: " + bootPropertiesTemplate.getPath() + " to: " + bootProperties.getPath());
            FileUtils.copyFile(bootPropertiesTemplate, bootProperties);
        }
        File repositoryLocal = new File(configDir, "repositoryLocal");
        if (repositoryLocal.exists()) {
            System.out.println(repositoryLocal + " exists, and will NOT be replaced by: " + repositoryLocalTemplate);
        } else {
            System.out.println("copy: " + repositoryLocalTemplate.getPath() + " to: " + repositoryLocal.getPath());
            FileUtils.copyDirectory(repositoryLocalTemplate, repositoryLocal);
            File repositoryPluginsDir = new File(repositoryLocal, "plugins");
            repositoryPluginsDir.mkdirs();
        }
    }
