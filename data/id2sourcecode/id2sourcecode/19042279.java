    protected void createPatch(File patch, ArrayList<Resource> orsrcs, ArrayList<Resource> nrsrcs, boolean verbose) throws IOException {
        MessageDigest md = Digest.getMessageDigest();
        JarOutputStream jout = null;
        try {
            jout = new JarOutputStream(new BufferedOutputStream(new FileOutputStream(patch)));
            for (Resource rsrc : nrsrcs) {
                int oidx = orsrcs.indexOf(rsrc);
                Resource orsrc = (oidx == -1) ? null : orsrcs.remove(oidx);
                if (orsrc != null) {
                    String odig = orsrc.computeDigest(md, null);
                    String ndig = rsrc.computeDigest(md, null);
                    if (odig.equals(ndig)) {
                        if (verbose) {
                            System.out.println("Unchanged: " + rsrc.getPath());
                        }
                        continue;
                    }
                    if (rsrc.getPath().endsWith(".jar")) {
                        if (verbose) {
                            System.out.println("JarDiff: " + rsrc.getPath());
                        }
                        File otemp = rebuildJar(orsrc.getLocal());
                        File temp = rebuildJar(rsrc.getLocal());
                        jout.putNextEntry(new ZipEntry(rsrc.getPath() + Patcher.PATCH));
                        jarDiff(otemp, temp, jout);
                        otemp.delete();
                        temp.delete();
                        continue;
                    }
                }
                if (verbose) {
                    System.out.println("Addition: " + rsrc.getPath());
                }
                jout.putNextEntry(new ZipEntry(rsrc.getPath() + Patcher.CREATE));
                pipe(rsrc.getLocal(), jout);
            }
            for (Resource rsrc : orsrcs) {
                if (verbose) {
                    System.out.println("Removal: " + rsrc.getPath());
                }
                jout.putNextEntry(new ZipEntry(rsrc.getPath() + Patcher.DELETE));
            }
            StreamUtil.close(jout);
            System.out.println("Created patch file: " + patch);
        } catch (IOException ioe) {
            StreamUtil.close(jout);
            patch.delete();
            throw ioe;
        }
    }
