    private static Manifest getManifest() {
        try {
            URL url = OpOpenApplet.class.getResource(OpOpenApplet.class.getSimpleName() + ".class");
            if (url != null) {
                JarURLConnection jconn = (JarURLConnection) url.openConnection();
                Manifest mf = jconn.getManifest();
                return mf;
            } else {
                logger.warn("could not read manifest file!");
            }
        } catch (IOException exc) {
        } catch (ClassCastException exc) {
        }
        return null;
    }
