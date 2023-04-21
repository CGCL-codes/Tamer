    private ZipFile getZipFile() throws IOException {
        String className = getClass().getName() + ".class";
        String jarURLName = getClass().getClassLoader().getSystemResource(className).toString();
        int toIndex = jarURLName.lastIndexOf("!/");
        URL url = new URL(jarURLName.substring(0, toIndex) + "!/");
        JarURLConnection urlCon = (JarURLConnection) url.openConnection();
        return urlCon.getJarFile();
    }
