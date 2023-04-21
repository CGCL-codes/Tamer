    public void save() {
        if (file == null) {
            saveAs();
        } else {
            final MetaDataWriter w = new MetaDataWriter(file, md);
            FailableThread t = new FailableThread("File writer thread") {

                @Override
                public void failableRun() throws Throwable {
                    try {
                        w.write();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    setModified(false);
                }
            };
            t.start();
            MEProgressMonitor.start("Saving to file", w);
        }
    }
