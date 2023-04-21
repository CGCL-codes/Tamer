    protected static File createTempCopy(String codebase) throws OpenEJBException {
        File file = null;
        try {
            File codebaseFile = new File(codebase);
            file = File.createTempFile("openejb_validate", ".jar", null);
            file.deleteOnExit();
            FileUtils.copyFile(file, codebaseFile);
        } catch (Exception e) {
            throw new OpenEJBException(messages.format("cl0002", codebase, e.getMessage()));
        }
        return file;
    }
