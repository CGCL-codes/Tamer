    public static boolean downloadResourceToDir(URL codebaseURL, String name, File destDir) {
        if ((codebaseURL == null) || (name == null) || (destDir == null)) return false;
        InputStream inputStream = null;
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        try {
            File cacheFile = new File(destDir, name);
            cacheFile = new File(cacheFile.getCanonicalPath());
            File parentFile = new File(cacheFile.getParent());
            parentFile.mkdirs();
            inputStream = downloadResource(codebaseURL, name);
            if (inputStream == null) {
                System.out.println("  Download of \"" + name + "\" from\n" + "    \"" + codebaseURL + "\" failed.");
                return false;
            }
            in = new BufferedInputStream(inputStream);
            out = new BufferedOutputStream(new FileOutputStream(cacheFile));
            int i;
            while ((i = in.read()) > -1) out.write((byte) i);
            System.out.println("  Successfully downloaded and saved.");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (Exception ex1) {
            }
            try {
                in.close();
            } catch (Exception ex1) {
            }
            try {
                inputStream.close();
            } catch (Exception ex1) {
            }
        }
        return false;
    }
