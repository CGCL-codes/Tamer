    public static World loadWorld(String pathnameOrUrl, Vector gameLog, InputOutputClient io, Object mundoSemaphore) {
        World theWorld = null;
        try {
            pathnameOrUrl = goIntoFileIfCompressed(pathnameOrUrl);
        } catch (SecurityException se) {
            ;
        }
        try {
            URL url = null;
            try {
                url = new URL(pathnameOrUrl);
            } catch (MalformedURLException mue) {
                url = WorldLoader.class.getClassLoader().getResource(pathnameOrUrl);
                if (url == null) throw mue;
            }
            if (url.toString().endsWith("/")) url = new URL(url.toString() + "world.xml");
            theWorld = new World(url, io, false);
            System.out.println("World generated.\n");
            if (mundoSemaphore != null) {
                synchronized (mundoSemaphore) {
                    mundoSemaphore.notifyAll();
                }
            }
            gameLog.addElement(theWorld.getResource("world.xml").toString());
            return theWorld;
        } catch (MalformedURLException e1) {
            return loadWorldFromPath(pathnameOrUrl, gameLog, io, mundoSemaphore);
        } catch (IOException ioe) {
            io.write(UIMessages.getInstance().getMessage("load.world.cannot.read.word") + " " + pathnameOrUrl + "\n");
            ioe.printStackTrace();
            return null;
        }
    }
