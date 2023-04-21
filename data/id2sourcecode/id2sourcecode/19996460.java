    protected static boolean loadIcon(final String urlString, final File outFile) {
        if (urlString == null) {
            return false;
        }
        try {
            URL url = new URL(StringHelper.encodeURL(urlString));
            InputStream i = url.openStream();
            try {
                ByteArrayOutputStream o = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                int bCount;
                while ((bCount = i.read(buffer)) != -1) {
                    o.write(buffer, 0, bCount);
                }
                o.close();
                FileOutputStream of = new FileOutputStream(outFile);
                try {
                    of.write(o.toByteArray());
                    of.flush();
                } finally {
                    of.close();
                }
            } finally {
                i.close();
            }
        } catch (MalformedURLException ex) {
            Application.getInstance().getLogger().log(Level.FINE, "Error cache channel icon", ex);
            return false;
        } catch (IOException ex) {
            Application.getInstance().getLogger().log(Level.FINE, "Error cache channel icon", ex);
            return false;
        }
        return true;
    }
