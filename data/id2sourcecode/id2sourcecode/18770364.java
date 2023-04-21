    public boolean importData(Component parent, URL url) {
        InputStream in = null;
        ProgressMonitor pm = null;
        try {
            in = url.openStream();
            DataInputStream din = new DataInputStream(in);
            int size = readHeader(din);
            log.info("Importing " + size + " sectors");
            if (parent != null) {
                pm = new ProgressMonitor(parent, "Importing TWX Proxy database...", null, 1, size);
                pm.setMillisToPopup(1000);
            }
            for (int id = 1; id <= size; id++) {
                readSector(din, id);
                if (pm != null) {
                    if (pm.isCanceled()) {
                        return false;
                    }
                    pm.setProgress(id);
                }
            }
            if (pm != null) {
                pm.close();
            }
            in.close();
            return true;
        } catch (IOException ex) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return false;
    }
