    private static File internalDownload(final URL url, final IProgressMonitor monitor) throws URLDownloadException {
        File targetfile = null;
        File tempfile = null;
        InputStream sourcestream = null;
        OutputStream targetstream = null;
        boolean finished = false;
        final URLConnection connection;
        if (url == null) {
            return null;
        }
        try {
            monitor.beginTask("Downloading from " + url.toString(), 100);
            targetfile = getCacheFileFromURL(url);
            if (targetfile.canRead()) {
                monitor.worked(100);
                finished = true;
                return targetfile;
            }
            synchronized (fCurrentDownloads) {
                fCurrentDownloads.add(url);
            }
            String targetextension = targetfile.getName().substring(targetfile.getName().lastIndexOf('.'));
            try {
                tempfile = File.createTempFile("download", targetextension, targetfile.getParentFile());
                targetstream = new FileOutputStream(tempfile);
            } catch (IOException ioe) {
                throw new URLDownloadException("Could not create temporary file", ioe);
            }
            monitor.subTask("Opening Connection");
            try {
                connection = url.openConnection();
                connection.setReadTimeout(10 * 1000);
            } catch (IOException ioe) {
                throw new URLDownloadException("Could not connect to " + url.getHost(), ioe);
            }
            monitor.worked(5);
            monitor.subTask("Preparing Download");
            try {
                Object contenthandler = connection.getContent();
                if (contenthandler instanceof InputStream) {
                    sourcestream = (InputStream) contenthandler;
                } else {
                    throw new URLDownloadException("Unknown type of remote file \"" + url.getFile() + "\" on \"" + url.getHost() + "\"");
                }
            } catch (UnknownHostException e) {
                throw new URLDownloadException("Host \"" + url.getHost() + "\" unknown, check address", e);
            } catch (FileNotFoundException fnfe) {
                throw new URLDownloadException("File \"" + url.getFile() + "\" not found on \"" + url.getHost() + "\"", fnfe);
            } catch (IOException ioe) {
                throw new URLDownloadException("Could not read file \"" + url.getFile() + "\" on host \"" + url.getHost() + "\"", ioe);
            }
            monitor.worked(5);
            try {
                monitor.subTask("Downloading " + url.toString());
                int length = connection.getContentLength();
                if (length == -1) {
                    length = IProgressMonitor.UNKNOWN;
                }
                internalStreamCopyWithProgress(sourcestream, targetstream, targetfile.getName(), length, new SubProgressMonitor(monitor, 95));
                sourcestream.close();
                targetstream.close();
                if (tempfile.renameTo(targetfile) == false) {
                    throw new URLDownloadException("Could not rename temporary file " + tempfile.toString() + " to " + targetfile.toString());
                }
                finished = true;
                return targetfile;
            } catch (SocketTimeoutException ste) {
                throw new URLDownloadException("Connection to " + connection.getURL().getHost() + " timed out", ste);
            } catch (SecurityException se) {
                throw new URLDownloadException("Permission denied by Java Security Manager", se);
            } catch (IOException ioe) {
                throw new URLDownloadException("Error downloading \nfrom: " + connection.getURL().toExternalForm() + "\nto:   " + targetfile.toString(), ioe);
            }
        } finally {
            monitor.done();
            synchronized (fCurrentDownloads) {
                fCurrentDownloads.remove(url);
                fCurrentDownloads.notifyAll();
            }
            if (!finished) {
                try {
                    if (sourcestream != null) {
                        sourcestream.close();
                    }
                    if (targetstream != null) {
                        targetstream.close();
                    }
                } catch (IOException ioe) {
                }
                if ((tempfile != null) && (tempfile.exists())) {
                    if (!tempfile.delete()) {
                        IStatus status = new Status(IStatus.WARNING, AVRPlugin.PLUGIN_ID, "Could not delete temporary file [" + tempfile.toString() + "]", null);
                        AVRPlugin.getDefault().log(status);
                    }
                }
                if ((targetfile != null) && (targetfile.exists())) {
                    if (!targetfile.delete()) {
                        IStatus status = new Status(IStatus.WARNING, AVRPlugin.PLUGIN_ID, "Could not delete temporary target file [" + targetfile.toString() + "]", null);
                        AVRPlugin.getDefault().log(status);
                    }
                }
            }
        }
    }
