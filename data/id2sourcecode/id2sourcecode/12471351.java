    public final void writeUnmodifiedClasses() throws IOException, IllegalStateException {
        passUnmodifiedClasses = false;
        makeOutputJar();
        for (int i = 0; i < inputs.size(); i++) {
            Input in = inputs.get(i);
            if (!in.isClass()) {
                if (in instanceof JarInput) {
                    JarInput jin = (JarInput) in;
                    ZipEntry entry = jin.getEntry();
                    InputStream s = jin.open();
                    try {
                        ZipEntry newEntry = new ZipEntry(entry.getName());
                        newEntry.setComment(entry.getComment());
                        newEntry.setExtra(entry.getExtra());
                        newEntry.setTime(entry.getTime());
                        putNextEntry(newEntry);
                        copyStream(s, outputJar);
                        outputJar.closeEntry();
                    } finally {
                        s.close();
                    }
                } else {
                    throw new Error("Unknown non-class input: " + in);
                }
            } else {
                String name = in.getClassName();
                if (name == null) {
                    BufferedInputStream s = new BufferedInputStream(in.open(), 65536);
                    try {
                        Object cl = makeClassFromStream(s);
                        String entryName = toEntryName(getClassName(cl));
                        if (!entryNames.contains(entryName)) {
                            putNextEntry(new ZipEntry(entryName));
                            BufferedOutputStream clOut = new BufferedOutputStream(outputJar);
                            writeClassTo(cl, null, clOut);
                            clOut.flush();
                            outputJar.closeEntry();
                        }
                    } finally {
                        s.close();
                    }
                } else {
                    String entryName = toEntryName(name);
                    if (!entryNames.contains(entryName)) {
                        BufferedInputStream s = new BufferedInputStream(in.open());
                        try {
                            putNextEntry(new ZipEntry(entryName));
                            BufferedOutputStream clOut = new BufferedOutputStream(outputJar);
                            copyStream(s, clOut);
                            clOut.flush();
                            outputJar.closeEntry();
                        } finally {
                            s.close();
                        }
                    }
                }
            }
        }
    }
