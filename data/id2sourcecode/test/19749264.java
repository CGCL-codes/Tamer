    public boolean doesPathExist(URL url) {
        try {
            JarURLConnection jcon = (JarURLConnection) url.openConnection();
            JarFile jarFile = jcon.getJarFile();
            String urlString = url.toString();
            String entryName = urlString.replaceFirst(".*?jar!/", "");
            return (jarFile.getEntry(entryName) != null);
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            System.err.println("JarLoadingSource.doesPathExist(URL) says: " + e);
            System.err.println("====> Aborting SwiFu....");
            System.exit(-1);
            return false;
        }
    }
