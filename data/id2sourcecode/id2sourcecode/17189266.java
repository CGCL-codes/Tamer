    public static final FileSystem mount(final String label, final Object param) throws IOException {
        if (param == null) {
            throw new IOException("Invalid null mount parameter.");
        }
        final Object obj[] = SshFileSystem.getChannelSftp(param);
        final ChannelSftp sftp = (ChannelSftp) obj[0];
        final String chRoot = (String) obj[1];
        if (chRoot != null) {
            new SshFileSystem("chroot" + label + chRoot.replace('/', '_'), sftp);
            return FileSystem.mount(label, FileSystemType.CHROOT, "chroot" + label + chRoot.replace('/', '_') + ":" + chRoot);
        }
        return new SshFileSystem(label, sftp);
    }
