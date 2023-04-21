    public static World loadWorldFromPath(String moduledir, Vector gameLog, InputOutputClient io, Object mundoSemaphore) {
        World theWorld = null;
        File inputAsFile = new File(moduledir);
        if (inputAsFile.isFile()) {
            System.out.println("Attempting world location: " + inputAsFile);
            try {
                theWorld = new World(moduledir, io, false);
                System.out.println("World generated.\n");
                if (mundoSemaphore != null) {
                    synchronized (mundoSemaphore) {
                        mundoSemaphore.notifyAll();
                    }
                }
                gameLog.addElement(inputAsFile.getAbsolutePath());
            } catch (java.io.IOException ioe) {
                io.write(UIMessages.getInstance().getMessage("load.world.cannot.read.world") + " " + inputAsFile + "\n");
                ioe.printStackTrace();
                return null;
            }
        } else {
            try {
                System.out.println("Attempting world location: " + moduledir + "/world.xml");
                theWorld = new World(moduledir + "/world.xml", io, false);
                System.out.println("World generated.\n");
                if (mundoSemaphore != null) {
                    synchronized (mundoSemaphore) {
                        mundoSemaphore.notifyAll();
                    }
                }
                gameLog.addElement(moduledir + "/world.xml");
            } catch (java.io.IOException e) {
                io.write(UIMessages.getInstance().getMessage("load.world.cannot.read.world.ondir") + " " + moduledir + "\n");
                System.out.println(e);
            }
        }
        if (theWorld == null) {
            io.write(UIMessages.getInstance().getMessage("load.world.invalid.dir", "$dir", moduledir));
            return null;
        }
        return theWorld;
    }
