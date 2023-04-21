    @Deprecated
    private void updateComa(String dialogURL) {
        File jarFile = null;
        ProtectionDomain pDomain = coma.getClass().getProtectionDomain();
        CodeSource codeSource = pDomain.getCodeSource();
        if (codeSource == null) System.out.println("ASDF!");
        URL loc = codeSource.getLocation();
        File jarDir = new File(loc.getFile()).getParentFile();
        try {
            URL jarURL = new URL(dialogURL);
            URLConnection connection = jarURL.openConnection();
            jarFile = new File(jarDir + "/ComaUpdate.jar");
            jarFile.createNewFile();
            FileOutputStream jarWriter = new FileOutputStream(jarFile);
            InputStream is = connection.getInputStream();
            byte[] buf = new byte[1028];
            int i = 0;
            while ((i = is.read(buf)) != -1) jarWriter.write(buf, 0, i);
            is.close();
            jarWriter.close();
            jarURL = new URL("http://www1.uni-hamburg.de/exmaralda/Daten/2D-Download/Zubehoer/updater.jar");
            connection = jarURL.openConnection();
            jarFile = new File(jarDir + "/updater.jar");
            jarFile.createNewFile();
            jarWriter = new FileOutputStream(jarFile);
            is = connection.getInputStream();
            buf = new byte[1028];
            i = 0;
            while ((i = is.read(buf)) != -1) jarWriter.write(buf, 0, i);
            is.close();
            jarWriter.close();
        } catch (IOException ioe) {
            System.out.println("Error Reading from connection!!!");
            ioe.printStackTrace();
        }
        try {
            URL pathToUpdater = jarFile.toURI().toURL();
            JarClassLoader cl = new JarClassLoader(pathToUpdater);
            String name = null;
            try {
                name = cl.getMainClassName();
            } catch (IOException e) {
                System.err.println("I/O error while loading JAR file:");
                e.printStackTrace();
            }
            if (name == null) {
                coma.status("Specified jar file does not contain a 'Main-Class'" + " manifest attribute");
            }
            String[] newArgs = { loc.toString(), jarDir + "/ComaUpdate.jar", jarDir + "/updater.jar" };
            try {
                cl.invokeClass(name, newArgs);
            } catch (ClassNotFoundException e) {
                coma.status("Class not found: " + name);
            } catch (NoSuchMethodException e) {
                coma.status("Class does not define a 'main' method: " + name);
            } catch (InvocationTargetException e) {
                e.getTargetException().printStackTrace();
                System.exit(1);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
