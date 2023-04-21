    boolean update(InputStream in, OutputStream out, InputStream newManifest, JarIndex jarIndex) throws IOException {
        ZipInputStream zis = new ZipInputStream(in);
        ZipOutputStream zos = new JarOutputStream(out);
        ZipEntry e = null;
        boolean foundManifest = false;
        boolean updateOk = true;
        if (jarIndex != null) {
            addIndex(jarIndex, zos);
        }
        while ((e = zis.getNextEntry()) != null) {
            String name = e.getName();
            boolean isManifestEntry = equalsIgnoreCase(name, MANIFEST_NAME);
            if ((jarIndex != null && equalsIgnoreCase(name, INDEX_NAME)) || (Mflag && isManifestEntry)) {
                continue;
            } else if (isManifestEntry && ((newManifest != null) || (ename != null))) {
                foundManifest = true;
                if (newManifest != null) {
                    FileInputStream fis = new FileInputStream(mname);
                    boolean ambiguous = isAmbiguousMainClass(new Manifest(fis));
                    fis.close();
                    if (ambiguous) {
                        return false;
                    }
                }
                Manifest old = new Manifest(zis);
                if (newManifest != null) {
                    old.read(newManifest);
                }
                updateManifest(old, zos);
            } else {
                if (!entryMap.containsKey(name)) {
                    ZipEntry e2 = new ZipEntry(name);
                    e2.setMethod(e.getMethod());
                    e2.setTime(e.getTime());
                    e2.setComment(e.getComment());
                    e2.setExtra(e.getExtra());
                    if (e.getMethod() == ZipEntry.STORED) {
                        e2.setSize(e.getSize());
                        e2.setCrc(e.getCrc());
                    }
                    zos.putNextEntry(e2);
                    copy(zis, zos);
                } else {
                    File f = entryMap.get(name);
                    addFile(zos, f);
                    entryMap.remove(name);
                    entries.remove(f);
                }
            }
        }
        for (File f : entries) {
            addFile(zos, f);
        }
        if (!foundManifest) {
            if (newManifest != null) {
                Manifest m = new Manifest(newManifest);
                updateOk = !isAmbiguousMainClass(m);
                if (updateOk) {
                    updateManifest(m, zos);
                }
            } else if (ename != null) {
                updateManifest(new Manifest(), zos);
            }
        }
        zis.close();
        zos.close();
        return updateOk;
    }
