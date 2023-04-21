    public static void disableFileLocking(URL url) {
        try {
            url.openConnection().setDefaultUseCaches(false);
        } catch (IOException e) {
            throw new RuntimeException("Could not disable file locking!", e);
        }
    }
