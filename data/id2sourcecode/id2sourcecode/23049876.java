    public SshNode(SshRoot root, String path) throws JSchException {
        this(root, root.getChannelFtp(), path);
    }
