    public static File downloadResourceAsTempFile(URL url, int timeoutSeconds, ProgressMonitor mon) throws IOException {
        if (timeoutSeconds == -1) timeoutSeconds = 10;
        URISplit split = URISplit.parse(url.toString());
        if (split.file.startsWith("file:/")) {
            if (split.params != null && split.params.length() > 0) {
                throw new IllegalArgumentException("local file URLs cannot have arguments");
            }
            try {
                return new File(new URL(split.file).toURI());
            } catch (URISyntaxException ex) {
                throw new IllegalArgumentException(ex);
            }
        }
        File local = FileSystem.settings().getLocalCacheDir();
        FileSystem fs = FileSystem.create(toUri(split.path));
        String id = fs.getLocalRoot().toString().substring(FileSystem.settings().getLocalCacheDir().toString().length());
        File localCache = new File(local, "temp");
        localCache = new File(localCache, id);
        if (!localCache.exists()) {
            if (!localCache.mkdirs()) {
                throw new IOException("unable to make directory: " + localCache);
            }
        }
        String filename = split.file.substring(split.path.length());
        if (split.params != null && split.params.length() > 0) {
            String safe = split.params;
            safe = safe.replaceAll("\\+", "_");
            safe = safe.replaceAll("-", ".");
            safe = Ops.safeName(safe);
            filename = filename.replaceAll("@", "_") + "@" + safe.replaceAll("@", "_");
        } else {
            filename = filename.replaceAll("@", "_") + "@";
        }
        if (filename.length() > 50) {
            String[] ss = filename.split("@", -2);
            String base = ss[0];
            if (base.length() > 50) base = base.substring(0, 50);
            String args = ss[1];
            if (args.length() > 0) args = String.format("%09x", args.hashCode());
            filename = base + String.format("%09x", ss[0].hashCode()) + "@" + args;
        }
        filename = new File(localCache, filename).toString();
        Object action = "";
        File result = new File(filename);
        File newf = new File(filename + ".temp");
        synchronized (DataSetURI.class) {
            if (result.exists() && (System.currentTimeMillis() - result.lastModified()) / 1000 < timeoutSeconds && !newf.exists()) {
                logger.log(Level.FINE, "using young temp file {0}", result);
                action = ACTION_USE_CACHE;
            } else if (newf.exists()) {
                logger.log(Level.FINE, "waiting for other thread to load temp resource {0}", newf);
                action = ACTION_WAIT_EXISTS;
            } else {
                File newName = result;
                while (newName.exists()) {
                    String[] ss = filename.toString().split("@", -2);
                    if (ss.length == 2) {
                        filename = ss[0] + "@" + ss[1] + "@0";
                    } else {
                        int i = Integer.parseInt(ss[2]);
                        filename = ss[0] + "@" + ss[1] + "@" + (i + 1);
                    }
                    newName = new File(filename);
                }
                if (!newName.equals(result)) {
                    if (!result.renameTo(newName)) {
                        System.err.println("unable to move old file out of the way.  Using alternate name " + newName);
                        result = newName;
                        newf = new File(filename + ".temp");
                    }
                }
                logger.log(Level.FINE, "this thread will downloading temp resource {0}", newf);
                action = ACTION_DOWNLOAD;
                OutputStream out = new FileOutputStream(result);
                out.write("DataSetURI.downloadResourceAsTempFile: This placeholding temporary file should not be used.\n".getBytes());
                out.close();
                OutputStream outf = new FileOutputStream(newf);
                outf.close();
            }
        }
        if (action == ACTION_USE_CACHE) {
            return result;
        } else if (action == ACTION_WAIT_EXISTS) {
            long t0 = System.currentTimeMillis();
            mon.setProgressMessage("waiting for resource");
            mon.started();
            try {
                while (newf.exists()) {
                    try {
                        Thread.sleep(300);
                        if (System.currentTimeMillis() - t0 > 60000) {
                            logger.log(Level.FINE, "waiting for other process to finish loading %s...{0}", newf);
                        }
                        if (mon.isCancelled()) {
                            throw new InterruptedIOException("cancel pressed");
                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(DataSetURI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } finally {
                mon.finished();
            }
            return result;
        } else {
            boolean fail = true;
            try {
                InputStream in;
                logger.log(Level.FINE, "reading URL {0}", url);
                URLConnection urlc = url.openConnection();
                urlc.setConnectTimeout(3000);
                in = new DasProgressMonitorInputStream(urlc.getInputStream(), mon);
                OutputStream out = new FileOutputStream(newf);
                DataSourceUtil.transfer(Channels.newChannel(in), Channels.newChannel(out));
                fail = false;
            } finally {
                if (fail) {
                    newf.delete();
                    result.delete();
                }
            }
        }
        result.deleteOnExit();
        checkNonHtml(newf, url);
        synchronized (DataSetURI.class) {
            if (!result.delete()) {
                throw new IllegalArgumentException("unable to delete " + result + " to make way for " + newf);
            }
            if (!newf.renameTo(result)) {
                throw new IllegalArgumentException("unable to rename " + newf + " to " + result);
            }
        }
        return result;
    }
