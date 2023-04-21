    public static void unzipToDir(String zipFile, String unzipDir) throws Exception {
        ZipFile zip = new ZipFile(zipFile);
        String zipName = zipFile.substring(zipFile.lastIndexOf("/"), zipFile.lastIndexOf("."));
        new File(unzipDir + zipName).mkdirs();
        for (Enumeration en = zip.entries(); en.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) en.nextElement();
            String name = entry.getName();
            System.out.println("entry name: " + name);
            String newname = unzipDir + zipName + name;
            System.out.println("new entry name: " + newname);
            if (entry.isDirectory()) {
                (new File(newname)).mkdirs();
            } else {
                System.out.println("parent: " + new File(newname).getParentFile());
                new File(newname).getParentFile().mkdirs();
            }
        }
        for (Enumeration en = zip.entries(); en.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) en.nextElement();
            String name = entry.getName();
            String newname = unzipDir + zipName + name;
            if (!entry.isDirectory()) {
                int len = 0;
                byte[] buffer = new byte[2048];
                InputStream in = zip.getInputStream(entry);
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(newname));
                while ((len = in.read(buffer)) >= 0) out.write(buffer, 0, len);
                in.close();
                out.close();
            }
        }
        zip.close();
    }
