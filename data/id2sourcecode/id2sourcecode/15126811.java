    public static void unzip(String zipfile, String targetdir) throws IOException {
        ZipFile jar = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> fs = jar.entries();
        while (fs.hasMoreElements()) {
            ZipEntry file = fs.nextElement();
            if (file.isDirectory()) continue;
            File f = new File(targetdir + File.separator + file.getName());
            if (f.exists() && f.lastModified() > file.getTime()) continue;
            System.out.println("Extracting " + f.getPath());
            String path = f.getParent();
            new File(path).mkdirs();
            InputStream is = jar.getInputStream(file);
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) > 0) fos.write(buffer, 0, read);
            fos.close();
            is.close();
        }
    }
