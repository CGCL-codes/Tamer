    private void processBdbLogs(final File checkpointDir, final String lastBdbCheckpointLog) throws IOException {
        File bdbDir = getBdbSubDirectory(checkpointDir);
        if (!bdbDir.exists()) {
            bdbDir.mkdir();
        }
        PrintWriter pw = new PrintWriter(new FileOutputStream(new File(checkpointDir, "bdbje-logs-manifest.txt")));
        try {
            boolean pastLastLogFile = false;
            Set<String> srcFilenames = null;
            do {
                FilenameFilter filter = new FilenameFilter() {

                    public boolean accept(File dir, String name) {
                        return name != null && name.toLowerCase().endsWith(".jdb");
                    }
                };
                srcFilenames = new HashSet<String>(Arrays.asList(new File(path).list(filter)));
                List tgtFilenames = Arrays.asList(bdbDir.list(filter));
                if (tgtFilenames != null && tgtFilenames.size() > 0) {
                    srcFilenames.removeAll(tgtFilenames);
                }
                if (srcFilenames.size() > 0) {
                    srcFilenames = new TreeSet<String>(srcFilenames);
                    int count = 0;
                    for (final Iterator i = srcFilenames.iterator(); i.hasNext() && !pastLastLogFile; ) {
                        String name = (String) i.next();
                        if (this.checkpointCopy) {
                            FileUtils.copyFiles(new File(path, name), new File(bdbDir, name));
                        }
                        pw.println(name);
                        if (name.equals(lastBdbCheckpointLog)) {
                            pastLastLogFile = true;
                        }
                        count++;
                    }
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.fine("Copied " + count);
                    }
                }
            } while (!pastLastLogFile && srcFilenames != null && srcFilenames.size() > 0);
        } finally {
            pw.close();
        }
    }
