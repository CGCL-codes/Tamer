    public String getStringInfo() {
        StringWriter writer = new StringWriter();
        String andamiPath;
        String extensionsPath;
        String jaiVersion;
        Properties props = System.getProperties();
        try {
            try {
                andamiPath = (new File(Launcher.class.getResource(".").getFile() + File.separator + ".." + File.separator + ".." + File.separator + "..")).getCanonicalPath();
            } catch (IOException e) {
                andamiPath = (new File(Launcher.class.getResource(".").getFile() + File.separator + ".." + File.separator + ".." + File.separator + "..")).getAbsolutePath();
            }
        } catch (Exception e1) {
            andamiPath = (String) props.get("user.dir");
        }
        try {
            try {
                extensionsPath = (new File(Launcher.getAndamiConfig().getPluginsDirectory())).getCanonicalPath();
            } catch (IOException e) {
                extensionsPath = (new File(Launcher.getAndamiConfig().getPluginsDirectory())).getAbsolutePath();
            }
        } catch (Exception e1) {
            extensionsPath = "???";
        }
        writer.write("gvSIG version: " + Version.longFormat() + "\n");
        writer.write("    gvSIG app exec path: " + andamiPath + "\n");
        writer.write("    gvSIG user app home: " + Launcher.getAppHomeDir() + "\n");
        writer.write("    gvSIG extension path: " + extensionsPath + "\n");
        writer.write("    gvSIG locale language: " + Launcher.getAndamiConfig().getLocaleLanguage() + "\n");
        String osName = props.getProperty("os.name");
        writer.write("OS name: " + osName + "\n");
        writer.write("    arch:" + props.get("os.arch") + "\n");
        writer.write("    version:" + props.get("os.version") + "\n");
        if (osName.startsWith("Linux")) {
            try {
                String[] command = { "lsb_release", "-a" };
                Process p = Runtime.getRuntime().exec(command);
                InputStream is = p.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) writer.write("    " + line + "\n");
            } catch (Exception ex) {
            }
        }
        writer.write("JAVA vendor: " + props.get("java.vendor") + "\n");
        writer.write("    version:" + props.get("java.version") + "\n");
        writer.write("    home: " + props.get("java.home") + "\n");
        return writer.toString();
    }
