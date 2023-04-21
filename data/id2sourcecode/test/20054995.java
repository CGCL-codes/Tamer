    protected void unzipEntry(ZipEntry e, File dest, boolean force, ZipEntryCommand zec) throws IOException {
        String zipName = e.getName();
        if (zipName.startsWith("/")) {
            zipName = zipName.substring(1);
        }
        if (zipName.endsWith("/")) {
            return;
        }
        int ix = zipName.lastIndexOf('/');
        if (ix > 0) {
            String dirName = zipName.substring(0, ix);
            if (!dirsMade.contains(dirName)) {
                File d = new File(dest, dirName);
                if (!(d.exists() && d.isDirectory())) {
                    if (!d.mkdirs()) {
                        throw new IOException("[UnZip.unzipEntry] Warning: unable to mkdir " + dirName);
                    }
                    dirsMade.add(dirName);
                }
            }
        }
        File fout = new File(dest, zipName);
        if (zec != null) {
            File fzec = zec.preProcess(zipName, fout);
            if (fzec != null) {
                fout = fzec;
            }
        }
        if (force || !fout.exists()) {
            FileOutputStream os = new FileOutputStream(fout);
            InputStream is = zippy.getInputStream(e);
            int n = 0;
            while ((n = is.read(b)) > 0) os.write(b, 0, n);
            is.close();
            os.close();
            zec.postUnzip(zipName, fout);
        } else {
            zec.postSkip(zipName, fout);
        }
    }
