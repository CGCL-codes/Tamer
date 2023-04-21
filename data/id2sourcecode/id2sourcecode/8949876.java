    private File getWebRoot(final File requestedWebroot, final File warfile) throws IOException {
        if (warfile != null) {
            HostConfiguration.logger.info("Beginning extraction from war file");
            if (!warfile.exists() || !warfile.isFile()) {
                throw new WinstoneException("The warfile supplied is unavailable or invalid (" + warfile + ")");
            }
            File unzippedDir = null;
            if (requestedWebroot != null) {
                unzippedDir = requestedWebroot;
            } else {
                String tempDirectory = StringUtils.stringArg(args, "tempDirectory", null);
                String child = "winstone" + File.separator;
                if (tempDirectory == null) {
                    final File tempFile = File.createTempFile("dummy", "dummy");
                    tempDirectory = tempFile.getParent();
                    tempFile.delete();
                    final String userName = System.getProperty("user.name");
                    if (userName != null) {
                        child += StringUtils.replace(userName, new String[][] { { "/", "" }, { "\\", "" }, { ",", "" } }) + File.separator;
                    }
                }
                if (hostname != null) {
                    child += hostname + File.separator;
                }
                child += warfile.getName();
                unzippedDir = new File(tempDirectory, child);
            }
            if (unzippedDir.exists()) {
                if (!unzippedDir.isDirectory()) {
                    throw new WinstoneException("The webroot supplied is not a valid directory (" + unzippedDir.getPath() + ")");
                } else {
                    HostConfiguration.logger.debug("The webroot supplied already exists - overwriting where newer ({})", unzippedDir.getCanonicalPath());
                }
            }
            final File timestampFile = new File(unzippedDir, ".timestamp");
            if (!timestampFile.exists() || Math.abs(timestampFile.lastModified() - warfile.lastModified()) > 1000) {
                FileUtils.delete(unzippedDir);
                unzippedDir.mkdirs();
            } else {
                return unzippedDir;
            }
            final JarFile warArchive = new JarFile(warfile);
            for (final Enumeration<JarEntry> e = warArchive.entries(); e.hasMoreElements(); ) {
                final JarEntry element = e.nextElement();
                if (element.isDirectory()) {
                    continue;
                }
                final String elemName = element.getName();
                final File outFile = new File(unzippedDir, elemName);
                if (outFile.exists() && outFile.lastModified() > warfile.lastModified()) {
                    continue;
                }
                outFile.getParentFile().mkdirs();
                final byte buffer[] = new byte[8192];
                final InputStream inContent = warArchive.getInputStream(element);
                final OutputStream outStream = new FileOutputStream(outFile);
                int readBytes = inContent.read(buffer);
                while (readBytes != -1) {
                    outStream.write(buffer, 0, readBytes);
                    readBytes = inContent.read(buffer);
                }
                inContent.close();
                outStream.close();
            }
            new FileOutputStream(timestampFile).close();
            timestampFile.setLastModified(warfile.lastModified());
            return unzippedDir;
        } else {
            return requestedWebroot;
        }
    }
