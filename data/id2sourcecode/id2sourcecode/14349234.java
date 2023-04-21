    private void JWScorrectClassPath() {
        URL jasperreportsAbsoluteURL = Thread.currentThread().getContextClassLoader().getResource("net/sf/jasperreports/engine");
        String jasperreportsAbsolutePath = "";
        if (jasperreportsAbsoluteURL.toString().startsWith("jar:http:") || jasperreportsAbsoluteURL.toString().startsWith("jar:https:")) {
            jasperreportsAbsolutePath = jasperreportsAbsoluteURL.toString().split("!")[0].split("jar:")[1];
            File reqLib = new File(System.getProperty("java.io.tmpdir"), "CompiereJasperReqs.jar");
            if (!reqLib.exists() && !(reqLib.length() > 0)) {
                try {
                    URL reqLibURL = new URL(jasperreportsAbsolutePath);
                    InputStream in = reqLibURL.openStream();
                    FileOutputStream fout = new FileOutputStream(reqLib);
                    byte buf[] = new byte[1024];
                    int s = 0;
                    while ((s = in.read(buf, 0, 1024)) > 0) fout.write(buf, 0, s);
                    in.close();
                    fout.flush();
                    fout.close();
                } catch (FileNotFoundException e) {
                    log.warning("Required library not found " + e.getMessage());
                    reqLib.delete();
                    reqLib = null;
                } catch (IOException e) {
                    log.severe("I/O error downloading required library from server " + e.getMessage());
                    reqLib.delete();
                    reqLib = null;
                }
            }
            jasperreportsAbsolutePath = reqLib.getAbsolutePath();
        } else {
            jasperreportsAbsolutePath = jasperreportsAbsoluteURL.toString().split("!")[0].split("file:")[1];
        }
        if (jasperreportsAbsolutePath != null && !jasperreportsAbsolutePath.trim().equals("")) {
            if (System.getProperty("java.class.path").indexOf(jasperreportsAbsolutePath) < 0) {
                System.setProperty("java.class.path", System.getProperty("java.class.path") + System.getProperty("path.separator") + jasperreportsAbsolutePath);
                log.info("Classpath has been corrected to " + System.getProperty("java.class.path"));
            }
        }
    }
