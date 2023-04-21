    private void writeSecurityFile(Application application, String appDir, String appLogDir, String appDiskDir, String domainDiskDir) throws IOException {
        Writer out = null;
        InputStream template = null;
        try {
            out = new FileWriter(appDir + System.getProperty("file.separator") + "jriaffe.policy");
            template = ApplicationInstaller.class.getResourceAsStream("/jriaffe/core/jriaffe.policy");
            int i = -1;
            while ((i = template.read()) != -1) {
                out.write(i);
            }
            out.write("permission java.io.FilePermission \"" + fixPath(appLogDir) + "/*\", \"read,write\";");
            out.write("\r");
            out.write("permission java.io.FilePermission \"" + fixPath(appDir) + "\", \"read\";");
            out.write("\r");
            out.write("permission java.io.FilePermission \"" + fixPath(appDir) + "/*\", \"read\";");
            out.write("\r");
            out.write("permission java.io.FilePermission \"" + fixPath(domainDiskDir) + "/*\", \"read,write\";");
            out.write("\r");
            out.write("permission java.io.FilePermission \"" + fixPath(appDiskDir) + "/*\", \"read,write\";");
            out.write("\r");
            List<String> hosts = application.getHosts();
            if (hosts != null) {
                for (String host : hosts) {
                    out.write("permission java.net.SocketPermission \"" + host + "\", \"connect,resolve\";");
                    out.write("\r");
                }
            }
            out.write("};");
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
            if (template != null) {
                template.close();
            }
        }
    }
