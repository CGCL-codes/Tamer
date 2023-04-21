    public final String extractManifest() {
        try {
            if (mans != null) return mans;
            if (ai != null) apk_path = ai.publicSourceDir;
            if (apk_path == null) return null;
            ZipFile zip = new ZipFile(apk_path);
            ZipEntry entry = zip.getEntry("AndroidManifest.xml");
            if (entry != null) {
                InputStream is = zip.getInputStream(entry);
                if (is != null) {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream((int) entry.getSize());
                    byte[] buf = new byte[4096];
                    int n;
                    while ((n = is.read(buf)) != -1) baos.write(buf, 0, n);
                    is.close();
                    mans = decompressXML(baos.toByteArray());
                    return mans;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
