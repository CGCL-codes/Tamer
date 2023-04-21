    protected static File getImage(final Context context, final String name, final String id, final boolean fullsize) throws IOException {
        File imageDir;
        if (fullsize) imageDir = getExternalFilesDir(id + "/fs/"); else imageDir = getExternalFilesDir(id);
        final File imageFile = new File(imageDir, name);
        if (imageFile.exists() && imageFile.length() > 0) return imageFile;
        if (isOnline(context)) {
            InputStream is = null;
            final URLConnection ucon = getUrlConnection(context, name, id, fullsize);
            try {
                is = ucon.getInputStream();
            } catch (final IOException e) {
                Log.e(TAG, "Failed to create inputstream.");
                Log.e(TAG, "" + e);
                return null;
            }
            if (is == null) return null;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imageFile);
            } catch (final FileNotFoundException e) {
                Log.e(TAG, e.toString(), e);
                throw e;
            }
            final byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) > 0) fos.write(buffer, 0, len1);
            fos.close();
            if (imageFile.exists() && imageFile.length() > 0) return imageFile;
            imageFile.delete();
            Log.v(TAG, "Geen data ontvangen uit de InputStream.");
        }
        return null;
    }
