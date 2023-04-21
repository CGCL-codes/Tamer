    public static final LispObject loadSystemFile(final String filename, boolean verbose, boolean print, boolean auto) throws ConditionThrowable {
        final int ARRAY_SIZE = 2;
        String[] candidates = new String[ARRAY_SIZE];
        final String extension = getExtension(filename);
        if (extension == null) {
            candidates[0] = filename + '.' + COMPILE_FILE_TYPE;
            candidates[1] = filename.concat(".lisp");
        } else if (extension.equals(".abcl")) {
            candidates[0] = filename;
            candidates[1] = filename.substring(0, filename.length() - 5).concat(".lisp");
        } else candidates[0] = filename;
        InputStream in = null;
        Pathname pathname = null;
        String truename = null;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            String s = candidates[i];
            if (s == null) break;
            ZipFile zipfile = null;
            final String dir = Site.getLispHome();
            try {
                if (dir != null) {
                    File file = IkvmSite.ikvmFileSafe(new File(dir, s));
                    if (file.isFile()) {
                        String ext = getExtension(s);
                        if (ext.equalsIgnoreCase(".abcl")) {
                            try {
                                zipfile = ZipCache.getZip(file.getPath());
                                String name = file.getName();
                                int index = name.lastIndexOf('.');
                                Debug.assertTrue(index >= 0);
                                name = name.substring(0, index).concat("._");
                                ZipEntry entry = zipfile.getEntry(name);
                                if (entry != null) {
                                    in = zipfile.getInputStream(entry);
                                    truename = file.getCanonicalPath();
                                }
                            } catch (ZipException e) {
                            } catch (Throwable t) {
                                Debug.trace(t);
                                in = null;
                            }
                        }
                        if (in == null) {
                            try {
                                in = new FileInputStream(file);
                                truename = file.getCanonicalPath();
                            } catch (IOException e) {
                                in = null;
                            }
                        }
                    }
                } else {
                    URL url = Lisp.class.getResource(s);
                    if (url != null) {
                        try {
                            in = url.openStream();
                            String proto = url.getProtocol();
                            if ("jar".equals(proto) || "ikvmres".equals(proto)) pathname = new Pathname(url);
                            truename = getPath(url);
                        } catch (IOException e) {
                            in = null;
                        }
                    }
                }
                if (in != null) {
                    final LispThread thread = LispThread.currentThread();
                    final SpecialBinding lastSpecialBinding = thread.lastSpecialBinding;
                    thread.bindSpecial(_WARN_ON_REDEFINITION_, NIL);
                    try {
                        return loadFileFromStream(pathname, truename, new Stream(in, SymbolConstants.CHARACTER), verbose, print, auto);
                    } catch (FaslVersionMismatch e) {
                        FastStringBuffer sb = new FastStringBuffer("; Incorrect fasl version: ");
                        sb.append(truename);
                        System.err.println(sb.toString());
                    } finally {
                        thread.lastSpecialBinding = lastSpecialBinding;
                        try {
                            in.close();
                        } catch (IOException e) {
                            return error(new LispError(e.getMessage()));
                        }
                    }
                }
            } finally {
                if (zipfile != null) {
                    try {
                        ZipCache.removeZip(zipfile.getName());
                    } catch (IOException e) {
                        return error(new LispError(e.getMessage()));
                    }
                }
            }
        }
        return error(new LispError("File not found: " + filename));
    }
