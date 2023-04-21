    public void write() {
        if (parent == null) throw new ApplicationException("Can't live without my parent!");
        if (entries == null || entries.size() < 4) throw new ApplicationException("have no entries to write! Please do a read before write.");
        File dest = getInfoPath();
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(dest);
            for (InfoEntry ie : entries) {
                pw.println(ie.id + " " + ie.value);
            }
        } catch (Exception e) {
            throw new ApplicationException("could not write info file", e);
        } finally {
            if (pw != null) pw.close();
        }
    }
