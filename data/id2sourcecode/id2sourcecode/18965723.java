    protected FileObject[] doListChildrenResolved() throws Exception {
        final Vector vector;
        final ChannelSftp channel = fileSystem.getChannel();
        try {
            vector = channel.ls(relPath);
        } finally {
            fileSystem.putChannel(channel);
        }
        if (vector == null) {
            throw new FileSystemException("vfs.provider.sftp/list-children.error");
        }
        final ArrayList children = new ArrayList();
        for (Iterator iterator = vector.iterator(); iterator.hasNext(); ) {
            final LsEntry stat = (LsEntry) iterator.next();
            String name = stat.getFilename();
            if (VFS.isUriStyle()) {
                if (stat.getAttrs().isDir() && name.charAt(name.length() - 1) != '/') {
                    name = name + "/";
                }
            }
            if (name.equals(".") || name.equals("..") || name.equals("./") || name.equals("../")) {
                continue;
            }
            FileObject fo = getFileSystem().resolveFile(getFileSystem().getFileSystemManager().resolveName(getName(), UriParser.encode(name), NameScope.CHILD));
            ((SftpFileObject) FileObjectUtils.getAbstractFileObject(fo)).setStat(stat.getAttrs());
            children.add(fo);
        }
        return (FileObject[]) children.toArray(new FileObject[children.size()]);
    }
