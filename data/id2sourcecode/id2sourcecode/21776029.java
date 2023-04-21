    public boolean addZipFile(File f, MD5ChecksumProperties p, StringBuilder sb, ZipOutputStream zos, Predicate<String> processFile) throws IOException {
        sb.append("Adding " + f + ":\n");
        ZipFile zf = new ZipFile(f);
        Enumeration<? extends ZipEntry> entries = zf.entries();
        boolean result = true;
        while (entries.hasMoreElements()) {
            ZipEntry ze = entries.nextElement();
            if (!ze.isDirectory()) {
                String key = ze.getName().replace('\\', '/');
                if (processFile.contains(key)) {
                    if (zos != null) {
                        if (p.containsKey(key)) {
                            sb.append("Warning: skipped " + key + ", already exists\n");
                        } else {
                            zos.putNextEntry(new ZipEntry(key));
                            if (!p.addMD5(key, zf.getInputStream(ze), zos)) {
                                result = false;
                                sb.append("Warning: a different " + key + " already exists\n");
                            }
                        }
                    } else {
                        if (!p.addMD5(key, zf.getInputStream(ze), zos)) {
                            result = false;
                            sb.append("Warning: a different " + key + " already exists\n");
                        }
                    }
                }
            }
        }
        return result;
    }
