    public void loadHandlers(File path) {
        String name, urlStr;
        ZipInputStream zipIn = null;
        ZipEntry entry;
        try {
            ClassLoader loader = new RemoteClassLoader();
            URL urlFile = path.toURI().toURL();
            zipIn = new ZipInputStream(urlFile.openStream());
            while ((entry = zipIn.getNextEntry()) != null) {
                name = entry.getName();
                if (name.matches("handlers/input/.+\\.class")) {
                    System.out.println("Loading input handler: " + name);
                    urlStr = mgr.getClass().getResource("/" + name).toString();
                    Class<InputHandler> cls = (Class<InputHandler>) loader.loadClass(urlStr);
                    Constructor<InputHandler> con = (Constructor<InputHandler>) cls.getConstructors()[0];
                    InputHandler in = con.newInstance(mgr);
                    registerHandler(in, in.getHooks());
                } else if (name.matches("handlers/output/.+\\.class")) {
                    System.out.println("Loading output handler: " + name);
                    urlStr = mgr.getClass().getResource("/" + name).toString();
                    Class<OutputHandler> cls = (Class<OutputHandler>) loader.loadClass(urlStr);
                    Constructor<OutputHandler> con = (Constructor<OutputHandler>) cls.getConstructors()[0];
                    OutputHandler out = con.newInstance(mgr);
                    registerHandler(out, out.getHooks());
                }
            }
        } catch (Exception e) {
            System.out.println("Could not load handlers: " + e);
        } finally {
            try {
                if (zipIn != null) zipIn.close();
            } catch (IOException ex) {
                System.out.println("Could not close zip input stream: " + ex);
            }
        }
    }
