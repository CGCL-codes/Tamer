    public static synchronized void loadLib() {
        if (loaded) {
            return;
        }
        libName = null;
        try {
            System.loadLibrary(getLibName(true));
            loaded = true;
        } catch (UnsatisfiedLinkError e) {
            File file = new File(System.getProperty("user.home") + File.separator + ".ov4n" + File.separator + getLibName(true) + ".dll");
            if (!file.exists()) {
                try {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdir();
                    }
                    System.out.println("Install native library " + file.getAbsolutePath());
                    InputStream in = OV4NLibLoader.class.getResourceAsStream("/" + getLibName(true) + ".dll");
                    if (in == null) {
                        throw new Win32Exception("Cant find dll in resource");
                    }
                    OutputStream out = new FileOutputStream(file);
                    byte buffer[] = new byte[8192];
                    int read;
                    int written = 0;
                    while ((read = in.read(buffer)) > -1) {
                        written += read;
                        out.write(buffer, 0, read);
                    }
                    out.close();
                    in.close();
                    if (written == 0) {
                        file.delete();
                        loaded = true;
                    } else {
                        loaded = true;
                    }
                } catch (Exception ex) {
                    file.delete();
                    ex.printStackTrace();
                    throw new Win32Exception("Unable to copy " + file.getName() + " to user.home" + File.separator + ".ov4n.", ex);
                }
            }
            System.load(file.getAbsolutePath());
        }
    }
