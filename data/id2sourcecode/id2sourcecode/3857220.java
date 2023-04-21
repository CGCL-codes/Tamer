    private static synchronized String getCertificate() throws IOException {
        if (IRODSAccountFactory._certificate == null) {
            ClassLoader cl = IRODSAccountFactory.class.getClassLoader();
            String prefix = "irodsaf_" + System.getProperty("user.name") + "_" + new Date().getTime();
            IRODSAccountFactory._certificate = "";
            for (Enumeration e = cl.getResources("certificates/"); e.hasMoreElements(); ) {
                JarFile jf = ((JarURLConnection) ((URL) e.nextElement()).openConnection()).getJarFile();
                for (Enumeration<JarEntry> ej = jf.entries(); ej.hasMoreElements(); ) {
                    JarEntry je = ej.nextElement();
                    if (!je.isDirectory() && je.getName().startsWith("certificates/")) {
                        InputStream in = (InputStream) jf.getInputStream(jf.getEntry(je.getName()));
                        String[] p = je.getName().split("/");
                        String suffix = "." + p[p.length - 1];
                        LocalFile temp = (LocalFile) LocalFile.createTempFile(prefix, suffix);
                        temp.deleteOnExit();
                        File file = temp.getFile();
                        FileOutputStream out = new FileOutputStream(file);
                        byte[] buffer = new byte[in.available()];
                        int bytes;
                        while ((bytes = in.read(buffer)) != -1) out.write(buffer, 0, bytes);
                        in.close();
                        out.close();
                        IRODSAccountFactory._certificate += file.getPath() + ",";
                    }
                }
            }
        }
        return IRODSAccountFactory._certificate;
    }
