    public void invokeOnAlbumsAtPath(PXJob.StatusCheck statusCheck_, PXInvocation invocation_, File directory_) throws InvocationTargetException, InterruptedException {
        LOG.debug("invoking for path: " + directory_.getAbsoluteFile());
        if ((invocation_ != null) && (directory_ != null) && (directory_.isDirectory())) {
            FileFilter albumFileFilter = PXFileFilters.getPXAlbumFileFilter();
            FileFilter directoryFileFilter = PXFileFilters.getPXDirectoryFileFilter();
            if (albumFileFilter.accept(directory_)) {
                ArrayList newArgs = new ArrayList();
                Object[] invocationArgs = invocation_.getArgs();
                newArgs.add(statusCheck_);
                if (invocationArgs != null) {
                    CollectionUtils.addAll(newArgs, invocation_.getArgs());
                }
                PXInvocation invocation = new PXInvocation(invocation_.getMethodName(), newArgs.toArray());
                if (statusCheck_ != null) {
                    statusCheck_.check();
                }
                PXAlbumContent albumContent = this.lockAlbumContentAtPath(directory_);
                if (albumContent != null) {
                    PXAlbumContent albumCopy = null;
                    try {
                        albumCopy = (PXAlbumContent) new PXAlbumContent(albumContent);
                    } catch (Exception anException) {
                        LOG.warn(null, anException);
                    } finally {
                        this.releaseAlbumContent(albumContent);
                    }
                    if (albumCopy != null) {
                        try {
                            invocation.invoke(albumCopy);
                        } catch (NoSuchMethodException e) {
                            LOG.warn(null, e);
                        }
                    }
                } else {
                    LOG.warn("could not lock album at path: " + directory_);
                }
            } else if (directoryFileFilter.accept(directory_)) {
                FileFilter nodeFilter = PXFileFilters.getPXNodeFileFilter();
                File[] nodes = directory_.listFiles(nodeFilter);
                if (nodes != null) {
                    for (int i = 0; i < nodes.length; i++) {
                        this.invokeOnAlbumsAtPath(statusCheck_, invocation_, nodes[i]);
                    }
                }
            } else {
            }
        }
    }
