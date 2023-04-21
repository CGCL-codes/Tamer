    protected boolean unpack_jar(String jarname, String[] files) {
        System.out.println("Unpacking " + jarname);
        try {
            File file = new File(jarname);
            if (!(file.exists())) {
                System.out.println("ERROR: File " + jarname + " does not exist!");
                return false;
            }
            JarFile jarfile = new JarFile(file);
            int entries = 0;
            JarEntry[] unpackthese = new JarEntry[0];
            if (files.length == 0) {
                for (Enumeration enu = jarfile.entries(); enu.hasMoreElements(); ) {
                    entries++;
                    Object o = enu.nextElement();
                }
                unpackthese = new JarEntry[entries];
                int j = 0;
                for (Enumeration enu = jarfile.entries(); enu.hasMoreElements(); ) {
                    unpackthese[j] = (JarEntry) enu.nextElement();
                    j++;
                }
            } else {
                unpackthese = new JarEntry[files.length];
                for (int i = 0; i < files.length; i++) {
                    unpackthese[i] = new JarEntry(files[i]);
                }
            }
            System.out.println(unpackthese.length + " files to be unjarred.");
            Comparator _comparebylenght = new Comparebylenght();
            Arrays.sort(unpackthese, _comparebylenght);
            for (int i = 0; i < unpackthese.length; i++) {
                File f = new File(unpackthese[i].getName());
                if (unpackthese[i].isDirectory()) {
                    boolean cre = f.mkdirs();
                    System.out.println("Creation of " + unpackthese[i].getName() + " " + cre);
                } else {
                    String dirname = unpackthese[i].getName();
                    if (dirname.lastIndexOf("/") > -1) {
                        dirname = dirname.substring(0, dirname.lastIndexOf("/"));
                        File f2 = new File(dirname);
                        f2.mkdirs();
                    }
                    InputStream stream = jarfile.getInputStream(unpackthese[i]);
                    FileOutputStream fos = new FileOutputStream(f);
                    byte c[] = new byte[4096];
                    int read = 0;
                    while ((read = stream.read(c)) != -1) fos.write(c, 0, read);
                    fos.close();
                    stream.close();
                }
            }
        } catch (Exception e) {
            System.out.println("ERROR:");
            System.out.println(e);
            return false;
        }
        return true;
    }
