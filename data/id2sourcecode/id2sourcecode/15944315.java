    private static URL getLocalJarURL(URL url, Collection<String> systemLibs) {
        String urlStrJar = SystemUtils.getJarPath(url);
        if (systemLibs.contains(urlStrJar)) return null;
        InputStream inputStreamJar = null;
        File tempJar;
        try {
            if (url.getPath().startsWith("file:")) return new File(urlStrJar).toURI().toURL();
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            inputStreamJar = new FileInputStream(jar.getName());
            String strippedName = urlStrJar;
            int dotIndex = strippedName.lastIndexOf('.');
            if (dotIndex >= 0) {
                strippedName = strippedName.substring(0, dotIndex);
                strippedName = strippedName.replace("/", File.separator);
                strippedName = strippedName.replace("\\", File.separator);
                int slashIndex = strippedName.lastIndexOf(File.separator);
                if (slashIndex >= 0) {
                    strippedName = strippedName.substring(slashIndex + 1);
                }
            }
            tempJar = File.createTempFile(strippedName, ".jar");
            tempJar.deleteOnExit();
            SystemUtils.copyToFile(inputStreamJar, tempJar);
            return tempJar.toURI().toURL();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (inputStreamJar != null) {
                    inputStreamJar.close();
                }
            } catch (IOException ioe) {
            }
        }
        return null;
    }
