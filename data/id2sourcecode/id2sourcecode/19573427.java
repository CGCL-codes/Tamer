    public void ki(String mit, String hova) throws Exception {
        JarFile jar = new JarFile(mit);
        Enumeration eenum = jar.entries();
        while (eenum.hasMoreElements()) {
            JarEntry file = (JarEntry) eenum.nextElement();
            File unjarDestinationDirectory = new File(hova);
            File f = new File(unjarDestinationDirectory, file.getName());
            File destinationParent = f.getParentFile();
            destinationParent.mkdirs();
            if (file.isDirectory()) {
                f.mkdir();
                continue;
            }
            InputStream is = jar.getInputStream(file);
            FileOutputStream fos = new FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
        }
    }
