    public Task exportToLocalFile(java.io.File localfile, boolean overwrite) throws RoctopusException {
        if (!exists()) {
            throw new RoctopusException("The file " + getPath() + " doesn't exist!");
        }
        if (localfile.exists() && !localfile.isDirectory() && !overwrite) throw new RoctopusException("A file" + localfile.getAbsolutePath() + " already exists and should not be overwritten!");
        if (localfile.isDirectory() && !overwrite) {
            java.io.File f = new java.io.File(localfile.getAbsolutePath() + java.io.File.separator + getName());
            if (f.exists()) throw new RoctopusException("A file" + localfile.getAbsolutePath() + " already exists and should not be overwritten!");
        }
        Task expt = new UnicoreExportTask(this, localfile);
        expt.setAutomaticallyAdvanceToCleanup(true);
        expt.startSync();
        return expt;
    }
