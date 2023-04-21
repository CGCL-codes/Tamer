    public GtkImage(URL url) {
        isLoaded = false;
        observers = new Vector();
        errorLoading = false;
        if (url == null) return;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(5000);
        try {
            BufferedInputStream bis = new BufferedInputStream(url.openStream());
            byte[] buf = new byte[5000];
            int n = 0;
            while ((n = bis.read(buf)) != -1) baos.write(buf, 0, n);
            bis.close();
        } catch (IOException e) {
            throw new IllegalArgumentException("Couldn't load image.");
        }
        if (loadImageFromData(baos.toByteArray()) != true) throw new IllegalArgumentException("Couldn't load image.");
        isLoaded = true;
        observers = null;
        props = new Hashtable();
    }
