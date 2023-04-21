    private void extractTar(File tarFile, File cacheDir) throws IOException, VerifyTarException {
        int count = 0;
        long totalSize = 0;
        long free = FileSystemUtils.freeSpace(journal.getDataRootDir().getPath());
        long fsize = tarFile.length();
        long toDelete = fsize + minFreeDiskSpace - free;
        if (toDelete > 0) free += free(toDelete);
        byte[] buf = new byte[bufferSize];
        TarInputStream tar = new TarInputStream(new FileInputStream(tarFile));
        InputStream in = tar;
        try {
            TarEntry entry = skipDirectoryEntries(tar);
            if (entry == null) throw new IOException("No entries in " + tarFile);
            String entryName = entry.getName();
            Map<String, byte[]> md5sums = null;
            MessageDigest digest = null;
            if ("MD5SUM".equals(entryName)) {
                if (checkMD5) {
                    try {
                        digest = MessageDigest.getInstance("MD5");
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    md5sums = new HashMap<String, byte[]>();
                    BufferedReader lineReader = new BufferedReader(new InputStreamReader(tar));
                    String line;
                    while ((line = lineReader.readLine()) != null) {
                        md5sums.put(line.substring(34), MD5Utils.toBytes(line.substring(0, 32)));
                    }
                }
                entry = skipDirectoryEntries(tar);
            } else if (checkMD5) {
                getLog().warn("Missing MD5SUM entry in " + tarFile);
            }
            for (; entry != null; entry = skipDirectoryEntries(tar)) {
                entryName = entry.getName();
                byte[] md5sum = null;
                if (md5sums != null && digest != null) {
                    md5sum = md5sums.remove(entryName);
                    if (md5sum == null) throw new VerifyTarException("Unexpected TAR entry: " + entryName + " in " + tarFile);
                    digest.reset();
                    in = new DigestInputStream(tar, digest);
                }
                File fOri = new File(cacheDir, entryName.replace('/', File.separatorChar));
                File f = new File(fOri.getAbsolutePath() + ".tmp");
                File dir = f.getParentFile();
                if (dir.mkdirs()) {
                    log.info("M-WRITE " + dir);
                }
                log.info("M-WRITE " + f);
                FileOutputStream out = new FileOutputStream(f);
                boolean cleanup = true;
                try {
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    cleanup = false;
                } finally {
                    try {
                        out.close();
                    } catch (Exception ignore) {
                    }
                    if (cleanup) {
                        log.info("M-DELETE " + f);
                        f.delete();
                    }
                }
                if (md5sums != null && digest != null) {
                    if (!Arrays.equals(digest.digest(), md5sum)) {
                        log.info("M-DELETE " + f);
                        f.delete();
                        throw new VerifyTarException("Failed MD5 check of TAR entry: " + entryName + " in " + tarFile);
                    } else log.info("MD5 check is successful for " + entryName + " in " + tarFile);
                }
                free -= f.length();
                count++;
                totalSize += f.length();
                if (f.exists()) f.renameTo(fOri);
            }
        } finally {
            tar.close();
        }
        toDelete = prefFreeDiskSpace - free;
        if (toDelete > 0) {
            freeNonBlocking(toDelete);
        }
    }
