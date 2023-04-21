    private void refreshJOGL(final File installDir) {
        try {
            Class subAppletClass = Class.forName(subAppletClassName);
        } catch (ClassNotFoundException cnfe) {
            displayError("Start failed : class not found : " + subAppletClassName);
            return;
        }
        if (!installDir.exists()) {
            if (!installDir.mkdirs()) {
                displayError("Unable to create directories for target: " + installDir);
                return;
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
            String path = getCodeBase().toExternalForm() + nativeJarName;
            try {
                nativeLibURL = new URL(path);
                urlConnection = nativeLibURL.openConnection();
            } catch (Exception e) {
                e.printStackTrace();
                displayError("Couldn't access the native lib URL : " + path);
                return;
            }
            long lastModified = getTimestamp(installDir, nativeJarName, urlConnection.getLastModified());
            if (lastModified != urlConnection.getLastModified()) {
                displayMessage("Updating local version of the native libraries");
                File localJarFile = new File(installDir, nativeJarName);
                try {
                    saveNativesJarLocally(localJarFile, urlConnection);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    displayError("Unable to install the native file locally");
                    return;
                }
                try {
                    JarFile jf = new JarFile(localJarFile);
                    if (!findNativeEntries(jf)) {
                        displayError("native libraries not found in jar file");
                        return;
                    }
                    byte[] buf = new byte[8192];
                    for (int i = 0; i < nativeLibNames.length; i++) {
                        JarEntry entry = jf.getJarEntry(nativeLibNames[i]);
                        if (entry == null) {
                            displayError("error looking up jar entry " + nativeLibNames[i]);
                            return;
                        }
                        if (!checkNativeCertificates(jf, entry, buf)) {
                            displayError("Native library " + nativeLibNames[i] + " isn't properly signed or has other errors");
                            return;
                        }
                    }
                    setProgress(0);
                    for (int i = 0; i < nativeLibNames.length; i++) {
                        displayMessage("Installing native files from " + nativeJarName);
                        if (!installFile(installDir, jf, nativeLibNames[i], buf)) {
                            return;
                        }
                        int percent = (100 * (i + 1) / nativeLibNames.length);
                        setProgress(percent);
                    }
                    jf.close();
                    localJarFile.delete();
                    try {
                        File timestampFile = new File(installDir, getTimestampFileName(nativeJarName));
                        timestampFile.delete();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(timestampFile));
                        writer.write("" + urlConnection.getLastModified());
                        writer.flush();
                        writer.close();
                    } catch (Exception e) {
                        displayError("Error writing time stamp for native libraries");
                        return;
                    }
                } catch (Exception e) {
                    displayError("Error opening jar file " + localJarFile.getName() + " for reading");
                    return;
                }
            }
        }
        loadNativesAndStart(installDir);
    }
