    private Image getIcon(String action) {
        try {
            ClassLoader cl = getClass().getClassLoader();
            java.io.InputStream in = cl.getResourceAsStream("/com/moneydance/modules/features/myextension/icon.gif");
            if (in != null) {
                ByteArrayOutputStream bout = new ByteArrayOutputStream(1000);
                byte buf[] = new byte[256];
                int n = 0;
                while ((n = in.read(buf, 0, buf.length)) >= 0) bout.write(buf, 0, n);
                return Toolkit.getDefaultToolkit().createImage(bout.toByteArray());
            }
        } catch (Throwable e) {
        }
        return null;
    }
