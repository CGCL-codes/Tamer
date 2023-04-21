    public static void main(String args[]) throws Exception {
        URL url = new URL("jar:file:/D://work//study.jar!/");
        JarURLConnection con = (JarURLConnection) url.openConnection();
        System.out.println(con.getEntryName());
        JarFile jarFile = con.getJarFile();
        System.out.println(jarFile.getName());
    }
