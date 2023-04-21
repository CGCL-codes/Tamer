    private void internalMove(IFileStore destination, boolean overwrite, boolean deleteOriginal, IProgressMonitor monitor) throws CoreException {
        try {
            if (destination instanceof WebDAVFileStore) {
                WebDAVFileStore dstwdfs = (WebDAVFileStore) destination;
                logger.log(Level.FINEST, "" + getUri() + " -> " + dstwdfs.getUri() + "; move: " + deleteOriginal);
                String name = uri.getName();
                if (name.length() == 0) {
                    String[] names = uri.getPath().split("/");
                    for (int i = names.length - 1; i >= 0; i--) if (names[i].length() != 0) {
                        name = names[i];
                        break;
                    }
                }
                String absDestUri = dstwdfs.getUri();
                if (dstwdfs.isCollection()) absDestUri = WebDAVTools.join(dstwdfs.getUri(), name);
                logger.log(Level.FINEST, "destUri: " + absDestUri);
                WebDAVFileStore destStore = WebDAVFileStore.create(absDestUri);
                destStore.refresh();
                if (destStore.exists()) {
                    if (equals(destStore)) throw new CoreException(Activator.createErrorStatus(0, "The source and the destination are the same resource!", null));
                    if (overwrite) destStore.delete(0, monitor); else throw new CoreException(Activator.createErrorStatus(Activator.WILL_I_REPLACE, "", null));
                }
                logger.log(Level.FINEST, "Roots: " + dstwdfs.getRoot() + "; " + getRoot());
                logger.log(Level.FINEST, "Roots.equals: " + getRoot().equals(dstwdfs.getRoot()));
                if (getRoot().equals(dstwdfs.getRoot())) {
                    try {
                        String destUri = WebDAVTools.join(dstwdfs.toURI().getPath(), uri.getName());
                        WebdavResource r = new WebdavResource(new HttpURL(uri, ""));
                        if (deleteOriginal) {
                            if (!r.moveMethod(destUri)) logger.log(Level.FINEST, "Could not move: " + r.getStatusMessage()); else {
                                cache.remove(getUri());
                            }
                        } else {
                            if (!r.copyMethod(destUri)) logger.log(Level.FINEST, "Could not copy: " + r.getStatusMessage());
                        }
                    } catch (HttpException e) {
                        logger.log(Level.SEVERE, e.toString() + " " + getUri() + " (" + e.getReasonCode() + ")");
                        e.printStackTrace();
                        if (e.getReasonCode() == 401) invalidAuth = true; else throw new CoreException(Activator.createErrorStatus(e));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (isCollection()) {
                            IFileStore[] children = childStores(0, monitor);
                            if (children != null) {
                                dstwdfs.createCollection(getName());
                                String basePath = WebDAVTools.join(dstwdfs.getUri(), getName());
                                WebDAVFileStore destChild = create(basePath, true, false);
                                for (int c = 0; c < children.length; c++) {
                                    try {
                                        ((WebDAVFileStore) children[c]).internalMove(destChild, overwrite, deleteOriginal, monitor);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else {
                            InputStream is = new BufferedInputStream(openInputStream(0, monitor));
                            File tmpfile = File.createTempFile("easywebdav", "");
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(tmpfile));
                            byte[] buffer = new byte[1024];
                            while (true) {
                                int read = is.read(buffer);
                                if (read <= 0) break;
                                os.write(buffer, 0, read);
                            }
                            is.close();
                            os.close();
                            dstwdfs.createFile(getName(), tmpfile);
                            tmpfile.delete();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (URIException e) {
            e.printStackTrace();
            throw new CoreException(Activator.createErrorStatus(e));
        } catch (IOException e) {
            e.printStackTrace();
            throw new CoreException(Activator.createErrorStatus(e));
        }
    }
