    public static final String deployWSAR(String wsarName) {
        String serviceName = wsarName.substring(0, wsarName.length() - "wsar.".length());
        Service service = ServiceManager.getInstance().getService(serviceName);
        if (service != null) {
            String error = service.undeploy();
            if (error != null) {
                return error;
            }
        }
        File serviceDirectory = new File(Uploader.directory, serviceName);
        File wsar = new File(Uploader.directory, wsarName);
        try {
            JarFile jar = new JarFile(wsar);
            Enumeration enumeration = jar.entries();
            serviceDirectory.mkdirs();
            while (enumeration.hasMoreElements()) {
                JarEntry file = (JarEntry) enumeration.nextElement();
                File f = new File(serviceDirectory, file.getName());
                InputStream is = jar.getInputStream(file);
                FileOutputStream fos = new FileOutputStream(f);
                while (is.available() > 0) {
                    fos.write(is.read());
                }
                fos.flush();
                fos.close();
                is.close();
            }
        } catch (Throwable t) {
            Log.get().log(Level.WARNING, "Error trying to deploy WSAR", t);
            return "Error attempting to deploy WSAR: " + t.getMessage();
        }
        Uploader.loadService(serviceDirectory);
        return null;
    }
