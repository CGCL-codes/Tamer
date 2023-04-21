    public static Atlas[] atlasLoader() {
        Atlas[] atlas_list = null;
        try {
            ArrayList<Atlas> atlases = new ArrayList<Atlas>();
            ClassLoader cl = Atlas.class.getClassLoader();
            for (Enumeration<URL> e = cl.getResources(Atlas.PREFIX); e.hasMoreElements(); ) {
                URL url = e.nextElement();
                JarURLConnection jc = (JarURLConnection) url.openConnection();
                JarFile jf = jc.getJarFile();
                for (Enumeration<JarEntry> ej = jf.entries(); ej.hasMoreElements(); ) {
                    JarEntry je = ej.nextElement();
                    if (je.isDirectory() && je.toString().matches("^atlases/[A-Za-z0-9_]+/$")) atlases.add(new Atlas(jf, je));
                }
            }
            if (atlases.size() > 0) {
                atlas_list = atlases.toArray(new Atlas[atlases.size()]);
                if (atlas_list.length > 1) Arrays.sort(atlas_list);
            }
        } catch (IOException ioe) {
            new ErrorEvent().sendWait(ioe);
        }
        return atlas_list;
    }
