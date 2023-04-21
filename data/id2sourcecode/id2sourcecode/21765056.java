    public static final LispObject loadCompiledFunction(final String namestring) throws ConditionThrowable {
        final LispThread thread = LispThread.currentThread();
        final boolean absolute = Utilities.isFilenameAbsolute(namestring);
        LispObject device = NIL;
        final Pathname defaultPathname;
        if (absolute) {
            defaultPathname = coerceToPathname(SymbolConstants.DEFAULT_PATHNAME_DEFAULTS.symbolValue(thread));
        } else {
            LispObject loadTruename = SymbolConstants.LOAD_TRUENAME.symbolValue(thread);
            if (loadTruename instanceof Pathname) {
                defaultPathname = (Pathname) loadTruename;
                device = ((Pathname) loadTruename).getDevice();
            } else {
                defaultPathname = coerceToPathname(SymbolConstants.DEFAULT_PATHNAME_DEFAULTS.symbolValue(thread));
            }
        }
        if (device instanceof Pathname) {
            URL url = Lisp.class.getResource(namestring);
            if (url == null) {
                String jarFile = ((Pathname) device).getNamestring();
                if (jarFile.startsWith("jar:file:")) {
                    try {
                        url = new URL(jarFile + "!/" + namestring);
                    } catch (MalformedURLException ex) {
                        Debug.trace(ex);
                    }
                }
            }
            if (url != null) {
                try {
                    String s = url.toString();
                    String zipFileName;
                    String entryName;
                    if (s.startsWith("jar:file:")) {
                        s = s.substring(9);
                        int index = s.lastIndexOf('!');
                        if (index >= 0) {
                            zipFileName = s.substring(0, index);
                            entryName = s.substring(index + 1);
                            if (entryName.length() > 0 && entryName.charAt(0) == '/') entryName = entryName.substring(1);
                            if (Utilities.isPlatformWindows) {
                                if (zipFileName.length() > 0 && zipFileName.charAt(0) == '/') zipFileName = zipFileName.substring(1);
                            }
                            zipFileName = URLDecoder.decode(zipFileName, "UTF-8");
                            ZipFile zipFile = ZipCache.getZip(zipFileName);
                            try {
                                ZipEntry entry = zipFile.getEntry(entryName);
                                if (entry != null) {
                                    long size = entry.getSize();
                                    InputStream in = zipFile.getInputStream(entry);
                                    LispObject obj = loadCompiledFunction(in, (int) size);
                                    return obj != null ? obj : NIL;
                                } else {
                                    entryName = defaultPathname.name.getStringValue() + "." + "abcl";
                                    byte in[] = Utilities.getZippedZipEntryAsByteArray(zipFile, entryName, namestring);
                                    LispObject o = loadCompiledFunction(in);
                                    return o != null ? o : NIL;
                                }
                            } finally {
                                ZipCache.removeZip(zipFile.getName());
                            }
                        }
                    }
                    if (s.startsWith("ikvmres:")) {
                        InputStream in = url.openStream();
                        int bytesAvailable = in.available();
                        ByteArrayOutputStream buf = new ByteArrayOutputStream();
                        while (bytesAvailable > 0) {
                            byte[] b = new byte[bytesAvailable];
                            in.read(b);
                            bytesAvailable = in.available();
                            buf.write(b);
                        }
                        LispObject obj = loadCompiledFunction(buf.toByteArray());
                        return obj != null ? obj : NIL;
                    }
                } catch (VerifyError e) {
                    return error(new LispError("Class verification failed: " + e.getMessage()));
                } catch (IOException e) {
                    Debug.trace(e);
                } catch (Throwable t) {
                    Debug.trace(t);
                }
            }
            try {
                if (IkvmSite.isIKVMDll() && namestring.endsWith(".class")) {
                    String className = namestring.substring(0, namestring.length() - 6);
                    Class c = Class.forName(Lisp.class.getPackage().getName() + "." + className.replace("-", "_"));
                    LispObject obj = loadCompiledFunction(c);
                    return obj != null ? obj : NIL;
                }
            } catch (Throwable cnf) {
                cnf.printStackTrace();
            }
            return error(new LispError("Unable to load " + namestring));
        }
        Pathname pathname = new Pathname(namestring);
        File file = Utilities.getFile(pathname, defaultPathname);
        if (file != null && !file.isFile()) {
            file = IkvmSite.ikvmFileSafe(file);
        }
        if (file != null && file.isFile()) {
            try {
                LispObject obj = loadCompiledFunction(new FileInputStream(file), (int) file.length());
                if (obj != null) return obj;
            } catch (VerifyError e) {
                return error(new LispError("Class verification failed: " + e.getMessage()));
            } catch (Throwable t) {
                Debug.trace(t);
            }
            return error(new LispError("Unable to load " + pathname.writeToString()));
        }
        try {
            LispObject loadTruename = SymbolConstants.LOAD_TRUENAME.symbolValue(thread);
            String zipFileName = ((Pathname) loadTruename).getNamestring();
            ZipFile zipFile = ZipCache.getZip(zipFileName);
            try {
                ZipEntry entry = zipFile.getEntry(namestring);
                if (entry != null) {
                    LispObject obj = loadCompiledFunction(zipFile.getInputStream(entry), (int) entry.getSize());
                    if (obj != null) return obj;
                    Debug.trace("Unable to load " + namestring);
                    return error(new LispError("Unable to load " + namestring));
                }
            } finally {
                ZipCache.removeZip(zipFile.getName());
            }
        } catch (Throwable t) {
            Debug.trace(t);
        }
        return error(new FileError("File not found: " + namestring, new Pathname(namestring)));
    }
