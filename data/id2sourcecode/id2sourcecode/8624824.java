    @SuppressWarnings("rawtypes")
    private void getContents(File file, String path) {
        try {
            JarFile jar = new JarFile(file);
            Enumeration jarEnum = jar.entries();
            while (jarEnum.hasMoreElements()) {
                JarEntry entry = (JarEntry) jarEnum.nextElement();
                String entryName = entry.getName();
                File entryFile = new File(path, java.io.File.separator + entryName);
                if (!entryName.startsWith("META-INF")) if (entry.isDirectory()) entryFile.mkdir(); else {
                    InputStream in = jar.getInputStream(entry);
                    OutputStream output = new FileOutputStream(entryFile);
                    while (in.available() > 0) output.write(in.read());
                    output.close();
                    in.close();
                }
            }
        } catch (Exception ex) {
            System.err.println("Error encountered while unjarring");
        }
    }
