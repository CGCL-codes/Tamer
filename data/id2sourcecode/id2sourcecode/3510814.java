    private static synchronized String getCertificate() throws IOException {
        if (SRBAccountFactory._certificate == null) {
            ClassLoader cl = SRBAccountFactory.class.getClassLoader();
            String prefix = "srbaf_" + System.getProperty("user.name") + "_" + new Date().getTime();
            SRBAccountFactory._certificate = "";
            for (Enumeration e = cl.getResources("certificates/"); e.hasMoreElements(); ) {
                JarFile jf = ((JarURLConnection) ((URL) e.nextElement()).openConnection()).getJarFile();
                for (Enumeration<JarEntry> ej = jf.entries(); ej.hasMoreElements(); ) {
                    JarEntry je = ej.nextElement();
                    if (!je.isDirectory() && je.getName().startsWith("certificates/")) {
                        InputStream in = jf.getInputStream(jf.getEntry(je.getName()));
                        String[] p = je.getName().split("/");
                        String suffix = "." + p[p.length - 1];
                        LocalFile temp = (LocalFile) LocalFile.createTempFile(prefix, suffix);
                        temp.deleteOnExit();
                        File file = temp.getFile();
                        OutputStream out = new FileOutputStream(file);
                        byte[] buffer = new byte[in.available()];
                        int bytes;
                        while ((bytes = in.read(buffer)) != -1) out.write(buffer, 0, bytes);
                        in.close();
                        out.close();
                        SRBAccountFactory._certificate += file.getPath() + ",";
                    }
                }
            }
        }
        return SRBAccountFactory._certificate;
    }
