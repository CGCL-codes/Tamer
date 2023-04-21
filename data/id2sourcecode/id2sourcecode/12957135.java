    public java.util.Map<String, String> run() throws IOException {
        JarInputStream jis = new JarInputStream(this.in);
        Manifest mf = jis.getManifest();
        Attributes atts = mf.getMainAttributes();
        java.util.Map<String, String> matts = new HashMap<String, String>();
        for (Object key : atts.keySet()) matts.put(((Attributes.Name) key).toString(), (String) atts.get(key));
        this.filter.processManifest(matts);
        final ZipOutputStream zos = new ZipOutputStream(this.out);
        zos.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
        writeManifest(matts, zos);
        JarEntry entry;
        while ((entry = jis.getNextJarEntry()) != null) {
            String name = entry.getName();
            if (!this.filter.processEntry(name)) continue;
            JarEntry newEntry = new JarEntry(name);
            zos.putNextEntry(newEntry);
            pump(jis, zos, 1024);
        }
        zos.flush();
        this.filter.addEntries(new FileDumper() {

            public void next(String path) throws IOException {
                zos.putNextEntry(new ZipEntry(path));
            }

            public void write(byte[] buffer, int offset, int length) throws IOException {
                zos.write(buffer, offset, length);
            }

            public void close() throws IOException {
            }
        });
        zos.flush();
        zos.close();
        jis.close();
        return matts;
    }
