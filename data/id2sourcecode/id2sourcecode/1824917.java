    private static void unzip(ZipInputStream zi, File destination) throws IOException {
        String sdest = destination.getAbsolutePath();
        ZipEntry entry;
        while ((entry = zi.getNextEntry()) != null) {
            String name = entry.getName();
            if (name.startsWith("/") || name.startsWith("\\")) {
                name = name.substring(1);
            }
            String dest = sdest + File.separator + name;
            File f = new File(dest);
            if (entry.isDirectory()) {
                f.mkdirs();
            } else {
                f.getParentFile().mkdirs();
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                byte[] buff = new byte[4096];
                int read;
                while ((read = zi.read(buff)) > 0) {
                    os.write(buff, 0, read);
                }
                os.close();
            }
        }
    }
