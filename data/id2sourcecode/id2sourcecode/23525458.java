    private static File cacheEntry(ZipInputStream in) {
        File cache = Utilities.getTempFile();
        if (cache != null) {
            OutputStream out = null;
            try {
                out = cache.getOutputStream();
                byte[] bytes = new byte[16384];
                int bytesRead;
                while ((bytesRead = in.read(bytes, 0, bytes.length)) > 0) out.write(bytes, 0, bytesRead);
            } catch (IOException e) {
                Log.error(e);
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    Log.error(e);
                }
            }
        }
        return cache;
    }
