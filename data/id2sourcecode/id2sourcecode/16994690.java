    protected void getFile(ZipEntry e) throws IOException {
        String zipName = e.getName();
        switch(mode) {
            case EXTRACT:
                if (zipName.startsWith("/")) {
                    if (!warnedMkDir) System.out.println("Ignoring absolute paths");
                    warnedMkDir = true;
                    zipName = zipName.substring(1);
                }
                if (zipName.endsWith("/")) {
                    return;
                }
                int ix = zipName.lastIndexOf('/');
                if (ix > 0) {
                    String dirName = zipName.substring(0, ix);
                    if (!dirsMade.contains(dirName)) {
                        File d = new File(dirName);
                        if (!(d.exists() && d.isDirectory())) {
                            log.debug("Creating Directory: " + dirName);
                            System.out.println();
                            if (!d.mkdirs()) {
                                log.error("Warning: unable to mkdir " + dirName);
                            }
                            dirsMade.add(dirName);
                        }
                    }
                }
                log.debug("Creating " + zipName);
                FileOutputStream os = new FileOutputStream(zipName);
                InputStream is = zippy.getInputStream(e);
                int n = 0;
                while ((n = is.read(b)) > 0) os.write(b, 0, n);
                is.close();
                os.close();
                break;
            case LIST:
                if (e.isDirectory()) {
                    log.debug("Directory " + zipName);
                } else {
                    log.debug("File " + zipName);
                }
                break;
            default:
                throw new IllegalStateException("mode value (" + mode + ") bad");
        }
    }
