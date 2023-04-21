    public void run() {
        setStatus("Unpacking...");
        try {
            long size = 0;
            long currentsize = 0;
            Enumeration e = myself.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                size += ze.getSize();
            }
            e = myself.entries();
            while (e.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) e.nextElement();
                setStatus("unpacking: " + ze.getName());
                if (verbose) System.out.println("unpacking: " + ze.getName());
                if (ze.isDirectory()) {
                    File f = new File(installdir, ze.getName());
                    if (!f.exists()) f.mkdirs();
                    continue;
                }
                InputStream is = myself.getInputStream(ze);
                File f = new File(installdir, ze.getName());
                File p = new File(f.getParent());
                if (!p.exists()) p.mkdirs();
                if (f.exists()) {
                    if (isConfig(ze.getName())) {
                        int ret = askKeepOverWriteMerge(f);
                        switch(ret) {
                            case KEEP:
                                continue;
                            case OVERWRITE:
                                break;
                            case MERGE:
                                merge(is, f);
                                continue;
                        }
                    } else if (isSpecial(ze.getName())) {
                        boolean keep = askOk(ze.getName() + " already exists and is a special file," + "do you want to keep the current one?");
                        if (keep) continue;
                    }
                }
                FileOutputStream fos = new FileOutputStream(f);
                byte[] b = new byte[1024];
                int read = 0;
                try {
                    while ((read = is.read(b, 0, (int) (ze.getSize() > b.length ? b.length : ze.getSize()))) > 0) {
                        fos.write(b, 0, read);
                    }
                } catch (EOFException eof) {
                }
                fos.flush();
                fos.close();
                currentsize += ze.getSize();
                meter.setMeter((double) currentsize / size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setStatus("Making executables...");
        makeExecutable(executables);
        setStatus("Done");
        card.show(panel, "done");
        installed = true;
    }
