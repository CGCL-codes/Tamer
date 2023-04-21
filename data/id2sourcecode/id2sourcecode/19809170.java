    private static void downloadExtensions(String extDir) {
        java.util.Date fechaActual = null;
        try {
            if (System.getProperty("javawebstart.version") != null) {
                BasicService bs = (BasicService) ServiceManager.lookup("javax.jnlp.BasicService");
                URL baseURL = bs.getCodeBase();
                SplashWindow.process(5, "Descargando las extensiones desde " + baseURL + " a " + extDir);
                URL url = new URL(baseURL + "extensiones.zip");
                URLConnection connection = url.openConnection();
                System.out.println(url.toExternalForm() + ":");
                System.out.println("  Content Type: " + connection.getContentType());
                System.out.println("  Content Length: " + connection.getContentLength());
                System.out.println("  Last Modified: " + new Date(connection.getLastModified()));
                System.out.println("  Expiration: " + connection.getExpiration());
                System.out.println("  Content Encoding: " + connection.getContentEncoding());
                Long miliSecondsInWeb = new Long(connection.getLastModified());
                File destDir = new File(extDir);
                if (!destDir.exists()) {
                    destDir.getParentFile().mkdir();
                    if (!destDir.mkdir()) {
                        System.err.println("Imposible crear el directorio " + destDir.getAbsolutePath());
                    }
                }
                File timeFile = new File(destDir.getParent() + File.separator + "timeStamp.properties");
                if (!timeFile.exists()) {
                    timeFile.createNewFile();
                }
                FileInputStream inAux = new FileInputStream(timeFile);
                Properties prop = new Properties();
                prop.load(inAux);
                inAux.close();
                if (prop.getProperty("timestamp") != null) {
                    Long lastMiliSeconds = (Long) new Long(prop.getProperty("timestamp"));
                    if (lastMiliSeconds.longValue() == miliSecondsInWeb.longValue()) {
                        System.out.println("No hay nueva actualizaci�n");
                        return;
                    }
                    System.out.println("timeStampWeb= " + miliSecondsInWeb);
                    System.out.println("timeStampLocal= " + lastMiliSeconds);
                } else {
                    System.out.println("El timeStamp no est� escrito en " + timeFile.getAbsolutePath());
                }
                InputStream stream = connection.getInputStream();
                File temp = File.createTempFile("gvsig", ".zip");
                temp.deleteOnExit();
                FileOutputStream file = new FileOutputStream(temp);
                BufferedInputStream in = new BufferedInputStream(stream);
                BufferedOutputStream out = new BufferedOutputStream(file);
                int i;
                int pct;
                int desde;
                int hasta;
                hasta = connection.getContentLength() / 1024;
                desde = 0;
                while ((i = in.read()) != -1) {
                    pct = ((desde / 1024) * 100) / hasta;
                    if (((desde % 10240) == 0) && (pct > 10) && ((pct % 10) == 0)) {
                        SplashWindow.process(pct, (desde / 1024) + "Kb de " + hasta + "Kb descargados...");
                    }
                    out.write(i);
                    desde++;
                }
                out.flush();
                out.close();
                in.close();
                SplashWindow.process(5, "Extensiones descargadas.");
                System.out.println("Extrayendo a " + destDir.getAbsolutePath());
                Date fechaDir = new Date(destDir.lastModified());
                System.out.println("Fecha del directorio " + extDir + " = " + fechaDir.toString());
                Utilities.extractTo(temp, new File(extDir), splashWindow);
                fechaActual = new java.util.Date();
                FileOutputStream outAux = new FileOutputStream(timeFile);
                prop.setProperty("timestamp", miliSecondsInWeb.toString());
                prop.store(outAux, "last download");
                outAux.close();
                System.out.println("Fecha actual guardada: " + fechaActual.toGMTString());
            }
        } catch (IOException e) {
            NotificationManager.addError("", e);
        } catch (UnavailableServiceException e) {
            NotificationManager.addError("", e);
        } catch (SecurityException e) {
            System.err.println("No se puede escribir el timeStamp " + fechaActual.toGMTString());
            NotificationManager.addError("", e);
        }
    }
