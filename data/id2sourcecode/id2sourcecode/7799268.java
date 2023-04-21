    public static byte[] loadResource(String name) {
        InputStream is = null;
        try {
            is = ApplicationHelper.class.getResourceAsStream(name);
            if (is == null) {
                is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
                if (is == null) {
                    File f = new File(name);
                    if (f.exists()) {
                        is = new BufferedInputStream(new FileInputStream(f));
                    }
                } else {
                    Logger.getLogger(ApplicationHelper.class).info("Loaded '" + name + "' with SystemClassLoader");
                }
            } else {
                Logger.getLogger(ApplicationHelper.class).info("Loaded '" + name + "' with ApplicationHelper");
            }
            if (is == null) {
                Logger.getLogger(ApplicationHelper.class).error("Failed to load '" + name + "'.");
                return null;
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream(is.available());
            while (is.available() > 0) {
                os.write(is.read());
            }
            return os.toByteArray();
        } catch (Exception e) {
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }
