    private static void copyFiles() {
        final String fileSeparator = System.getProperty("file.separator");
        try {
            boolean copyResource = false;
            final File systrayDllFile = new File("exec" + fileSeparator + "JSysTray.dll");
            if (!systrayDllFile.isFile()) {
                copyResource = true;
            } else {
                final URL url = MainFrame.class.getResource("/data/JSysTray.dll");
                final URLConnection urlConn = url.openConnection();
                final long len = urlConn.getContentLength();
                if (len != systrayDllFile.length()) {
                    systrayDllFile.delete();
                    copyResource = true;
                }
            }
            if (copyResource) {
                FileAccess.copyFromResource("/data/JSysTray.dll", systrayDllFile);
            }
        } catch (final Throwable e) {
            e.printStackTrace();
        }
    }
