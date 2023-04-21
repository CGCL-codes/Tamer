    private static boolean extractAndLoad(String outFileName, String libName) throws Throwable {
        int read;
        byte[] buf = new byte[4096];
        InputStream is = null;
        FileOutputStream os = null;
        File file = new File(outFileName);
        if (file.exists()) file.delete();
        if (!file.exists()) {
            try {
                os = new FileOutputStream(file);
                is = Library.class.getResourceAsStream(" / " + libName);
                if (is == null || os == null) return false;
                while ((read = is.read(buf)) != -1) os.write(buf, 0, read);
            } finally {
                if (os != null) os.close();
                if (is != null) is.close();
            }
            if (!System.getProperty("os.name").toLowerCase().startsWith("windows")) {
                Runtime.getRuntime().exec(new String[] { " chmod ", " 755 ", outFileName }).waitFor();
            }
            return _load(outFileName);
        }
        return false;
    }
