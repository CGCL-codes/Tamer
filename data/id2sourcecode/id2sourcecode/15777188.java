    @Override
    public List<SshNode> list() throws ListException {
        List<SshNode> nodes;
        ChannelSftp.LsEntry entry;
        String name;
        boolean dir;
        try {
            nodes = new ArrayList<SshNode>();
            dir = false;
            for (Object obj : getChannel().ls(escape(slashPath))) {
                try {
                    entry = (ChannelSftp.LsEntry) obj;
                    name = entry.getFilename();
                    if (".".equals(name) || "..".equals(name)) {
                        dir = true;
                    } else {
                        nodes.add(join(name));
                    }
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("illegal name: " + obj, e);
                }
            }
            if (!dir && nodes.size() == 1) {
                return null;
            } else {
                return nodes;
            }
        } catch (SftpException e) {
            throw new ListException(this, e);
        } catch (JSchException e) {
            throw new ListException(this, e);
        }
    }
