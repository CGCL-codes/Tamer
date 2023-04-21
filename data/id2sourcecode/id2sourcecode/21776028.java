    public boolean addDirectory(File f, MD5ChecksumProperties p, StringBuilder sb, ZipOutputStream zos, Predicate<String> processFile) throws IOException {
        sb.append("Adding " + f + ":\n");
        boolean result = true;
        for (File de : IOUtil.listFilesRecursively(f)) {
            if (!de.isDirectory()) {
                String key = FileOps.stringMakeRelativeTo(de, f).replace('\\', '/');
                if (processFile.contains(key)) {
                    if (zos != null) {
                        if (p.containsKey(key)) {
                            sb.append("Warning: skipped " + key + ", already exists\n");
                        } else {
                            zos.putNextEntry(new ZipEntry(key));
                            if (!p.addMD5(key, de, zos)) {
                                result = false;
                                sb.append("Warning: a different " + key + " already exists\n");
                            }
                        }
                    } else {
                        if (!p.addMD5(key, de, zos)) {
                            result = false;
                            sb.append("Warning: a different " + key + " already exists\n");
                        }
                    }
                }
            }
        }
        return result;
    }
