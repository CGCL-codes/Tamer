    static Song deserialize(File f) {
        if (f == null || !f.getName().endsWith(".song.xml")) return null;
        Song s;
        String canonicalPath = "";
        try {
            canonicalPath = f.getCanonicalPath();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (Song.instantiated.containsKey(canonicalPath)) return Song.instantiated.get(canonicalPath);
        XStream xs = new XStream();
        configXStream(xs);
        FileInputStream fis = null;
        ByteArrayOutputStream xformed = null;
        try {
            fis = new FileInputStream(f);
            xformed = new ByteArrayOutputStream();
            synchronized (Song.class) {
                songXFormer.transform(new StreamSource(fis), new StreamResult(xformed));
            }
            xformed = convertReferences(new ByteArrayInputStream(xformed.toByteArray()));
            s = (Song) xs.fromXML(xformed.toString("UTF-8"));
        } catch (FileNotFoundException ex) {
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            MainApp.log("Failed to deserialize " + canonicalPath);
            ErrorManager.getDefault().notify(ex);
            return null;
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            if (xformed != null) try {
                xformed.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        s.sourceFile = new File(canonicalPath);
        synchronized (s.pageOrder) {
            for (MusicPage mp : s.pageOrder) mp.deserialize(s);
        }
        findPages(s);
        Song.instantiated.put(canonicalPath, s);
        return s;
    }
