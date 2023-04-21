    private Collection<? extends JarEntry> transformFiles(final JarInputStream in, final String inPkgsList, final JarOutputStream out) throws IOException {
        final String[] inPkgsNames = inPkgsList.split(",");
        final Collection<String> inPkgsPaths = adjustPackagesNames(inPkgsNames);
        Collection<JarEntry> res = null;
        for (JarEntry je = in.getNextJarEntry(); je != null; je = in.getNextJarEntry()) {
            final String jeName = je.getName();
            if ((null == jeName) || (jeName.length() <= 0)) continue;
            if (_logger.isDebugEnabled()) _logger.debug("transformEntry(" + jeName + ")");
            final boolean isMetaInfo = jeName.startsWith("META-INF");
            if ((!isMetaInfo) && (null == findInPackagesList(jeName, inPkgsPaths))) continue;
            boolean isFile = !je.isDirectory();
            if (isFile && "META-INF".equalsIgnoreCase(jeName)) isFile = false;
            if (isFile && (!isMetaInfo)) {
                final int sfxPos = jeName.lastIndexOf('.');
                if ((sfxPos >= 0) && (sfxPos < jeName.length())) {
                    final String sfx = jeName.substring(sfxPos);
                    if (".class".equalsIgnoreCase(sfx) || ".jar".equalsIgnoreCase(sfx)) continue;
                } else isFile = false;
            }
            final long jeSize = je.getSize();
            final JarEntry outEntry = new JarEntry(jeName);
            outEntry.setComment(je.getComment());
            outEntry.setTime(je.getTime());
            outEntry.setSize(jeSize);
            out.putNextEntry(outEntry);
            if (isFile) {
                if (jeSize > 0L) {
                    final long cpySize = IOCopier.copyStreams(in, out, IOCopier.DEFAULT_COPY_SIZE, jeSize);
                    if (cpySize != jeSize) throw new StreamCorruptedException("Mismatched read(" + jeSize + ")/write(" + cpySize + ") copy size(s)");
                    if (_logger.isDebugEnabled()) _logger.debug("transformEntry(" + jeName + ") copied " + cpySize + " bytes");
                } else if (_logger.isDebugEnabled()) _logger.debug("transformEntry(" + jeName + ") skip empty file");
            } else if (jeSize > 0L) throw new StreamCorruptedException("Non zero (" + jeSize + ") directory size for entry=" + jeName); else if (_logger.isDebugEnabled()) _logger.debug("transformEntry(" + jeName + ") put directory entry");
            out.closeEntry();
            if (null == res) res = new LinkedList<JarEntry>();
            res.add(outEntry);
        }
        return res;
    }
