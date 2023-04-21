    public static void copy(InputStream fis, OutputStream fos) {
        try {
            byte buffer[] = new byte[0xffff];
            int nbytes;
            while ((nbytes = fis.read(buffer)) != -1) fos.write(buffer, 0, nbytes);
        } catch (IOException e) {
            EngineActivator.safeLogError(e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    EngineActivator.safeLogError(e.getMessage(), e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    EngineActivator.safeLogError(e.getMessage(), e);
                }
            }
        }
    }
