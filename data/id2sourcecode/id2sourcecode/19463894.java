    public static void disableFileLocking(URL url) {
        try {
            url.openConnection().setDefaultUseCaches(false);
        } catch (IOException e) {
            throw new NetServeRuntimeException("Could not disable file locking!", e);
        }
    }
