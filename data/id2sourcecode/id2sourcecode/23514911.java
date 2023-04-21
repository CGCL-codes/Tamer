    public void load(String baseURL, String localDirectory, IProgressListener pl, IExtensionLoader loader) throws Exception {
        pl.setProgress(0);
        pl.setFinished(false);
        pl.setError(false);
        URL downloadURL = null;
        String installDirectory = null;
        try {
            downloadURL = new URL(baseURL);
            installDirectory = System.getProperty("user.home") + File.separator + localDirectory;
        } catch (Exception e) {
            pl.setProgress(100);
            pl.setFinished(true);
            pl.setError(true);
            throw new Exception("JOGLLoader: Invalid parameter: " + e.toString());
        }
        loader.loadJar(localDirectory, baseURL, "jogl.jar", pl);
        loader.loadJar(localDirectory, baseURL, "gluegen-rt.jar", pl);
        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");
        if (!checkOSAndArch(osName, osArch)) {
            pl.setProgress(100);
            pl.setFinished(true);
            pl.setError(true);
            throw new Exception("Init JOGL failed : Unsupported os / arch ( " + osName + " / " + osArch + " )");
        }
        File installDir = new File(installDirectory);
        if (!installDir.exists()) {
            if (!installDir.mkdirs()) {
                pl.setProgress(100);
                pl.setFinished(true);
                pl.setError(true);
                throw new Exception("Cannot create install directory: " + installDirectory);
            }
        }
        try {
            Class alClass = Class.forName("net.java.games.joal.AL", false, this.getClass().getClassLoader());
            haveJOAL = true;
        } catch (Exception e) {
        }
        String[] nativeJarNames = new String[] { nativeLibInfo.formatNativeJarName("jogl-natives-{0}.jar"), nativeLibInfo.formatNativeJarName("gluegen-rt-natives-{0}.jar"), (haveJOAL ? nativeLibInfo.formatNativeJarName("joal-natives-{0}.jar") : null) };
        for (int n = 0; n < nativeJarNames.length; n++) {
            String nativeJarName = nativeJarNames[n];
            if (nativeJarName == null) continue;
            URL nativeLibURL;
            URLConnection urlConnection;
            String path = downloadURL.toExternalForm() + nativeJarName;
            nativeLibURL = new URL(path);
            urlConnection = nativeLibURL.openConnection();
            File localJarFile = new File(installDir, nativeJarName);
            saveNativesJarLocally(localJarFile, urlConnection);
            JarFile jf = new JarFile(localJarFile);
            if (!findNativeEntries(jf)) {
                pl.setProgress(100);
                pl.setFinished(true);
                pl.setError(true);
                throw new Exception("Native libraries not found in jar file");
            }
            byte[] buf = new byte[8192];
            for (int i = 0; i < nativeLibNames.length; i++) {
                if (!installFile(installDir, jf, nativeLibNames[i], buf)) {
                    pl.setProgress(100);
                    pl.setFinished(true);
                    pl.setError(true);
                    throw new Exception("Cannot install file " + jf.toString() + " in: " + installDir);
                }
            }
            jf.close();
            localJarFile.delete();
        }
        loadNatives(installDir);
        joglLoaded = true;
        pl.setProgress(100);
        pl.setFinished(true);
        pl.setError(false);
    }
