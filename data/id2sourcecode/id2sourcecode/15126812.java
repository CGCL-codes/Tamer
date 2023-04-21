    public static void unzip(URL zipfile, String targetdir) throws IOException {
        ZipInputStream jar = new ZipInputStream(zipfile.openStream());
        ZipEntry file;
        while ((file = jar.getNextEntry()) != null) {
            if (file.isDirectory()) continue;
            File f = new File(targetdir + File.separator + file.getName());
            if (f.exists() && f.lastModified() > file.getTime()) continue;
            System.out.println("Extracting " + f.getPath());
            String path = f.getParent();
            new File(path).mkdirs();
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = jar.read(buffer)) > 0) fos.write(buffer, 0, read);
            fos.close();
        }
        jar.close();
    }
