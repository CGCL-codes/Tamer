    public FileIO(String filename, String filemode) throws InvalidFileModeException, FileIOException, IOException {
        ir = null;
        pw = null;
        compressed = NO_ZIP;
        pipe = false;
        lineno = 0;
        originalname = (filename != null) ? filename.intern() : null;
        originalmode = (filemode != null) ? filemode.intern() : null;
        mode = UNKNOWN_MODE;
        StringBuffer buf = new StringBuffer();
        int idx;
        if (filemode == null) {
            buf.append("FileIO: incorrect file mode: ").append(filemode).append(" for file ").append(filename);
            throw new InvalidFileModeException(buf.toString());
        }
        if (filemode.indexOf('r') >= 0) {
            mode = READ_MODE;
        } else if (filemode.indexOf('w') >= 0) {
            mode = WRITE_MODE;
        } else {
            buf.append("FileIO: incorrect file mode: ").append(filemode).append(" for file ").append(filename);
            throw new InvalidFileModeException(buf.toString());
        }
        if ((idx = filemode.indexOf('z')) >= 0 || (idx = filemode.indexOf('g')) >= 0) {
            compressed = ZIP;
            pipe = false;
            buf.setLength(0);
            filemode = buf.append(filemode.substring(0, idx)).append(filemode.substring(idx + 1)).toString();
        }
        if ((idx = filemode.indexOf('p')) >= 0) {
            pipe = true;
            compressed = NO_ZIP;
            buf.setLength(0);
            filemode = buf.append(filemode.substring(0, idx)).append(filemode.substring(idx + 1)).toString();
            logger.finest(filemode);
        }
        if (filename != null && (filename.charAt(0) == '|')) {
            pipe = true;
            compressed = NO_ZIP;
            filename = filename.substring(1);
        }
        if (filename != null) {
            filename = filename.trim();
            if (!pipe) {
                compressed = checkCompressionType(filename);
            }
            if (filename.compareTo("-") == 0) {
                filename = null;
            }
        }
        if (filename == null) {
            if (mode == READ_MODE) {
                ir = new geovista.readers.Reader(System.in);
                pw = null;
            } else {
                ir = null;
                pw = new PrintWriter(System.out);
            }
            filename = (mode == READ_MODE) ? STDIN_NAME : STDOUT_NAME;
        } else if (compressed > NO_ZIP) {
            if (mode == READ_MODE) {
                switch(compressed) {
                    case ZIP:
                        ZipFile zf = new ZipFile(filename);
                        Enumeration enumeration = zf.entries();
                        ZipEntry target = (ZipEntry) enumeration.nextElement();
                        ir = new geovista.readers.Reader(zf.getInputStream(target));
                        break;
                    case GZIP:
                        GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(filename));
                        ir = new geovista.readers.Reader(gzis);
                        break;
                }
                pw = null;
            } else {
                File zf = new File(filename);
                switch(compressed) {
                    case ZIP:
                        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zf));
                        String fullpath = zf.getPath();
                        String path = fullpath.substring(0, fullpath.lastIndexOf("."));
                        ZipEntry target = new ZipEntry(path);
                        zos.putNextEntry(target);
                        ir = null;
                        pw = new PrintWriter(zos, true);
                        break;
                    case GZIP:
                        GZIPOutputStream gos = new GZIPOutputStream(new FileOutputStream(zf));
                        ir = null;
                        pw = new PrintWriter(gos, true);
                        break;
                }
            }
        } else if (pipe) {
        } else {
            if (mode == READ_MODE) {
                ir = new geovista.readers.Reader(new FileInputStream(filename));
                pw = null;
            } else {
                ir = null;
                pw = new PrintWriter(new FileOutputStream(filename), true);
            }
        }
        name = filename.intern();
    }
