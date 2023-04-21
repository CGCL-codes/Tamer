    @Override
    public Node move(Node destNode) throws MoveException {
        SshNode dest;
        if (!(destNode instanceof SshNode)) {
            throw new MoveException(this, destNode, "target has is different node type");
        }
        dest = (SshNode) destNode;
        try {
            getChannel().rename(escape(slashPath), escape(dest.slashPath));
        } catch (SftpException e) {
            throw new MoveException(this, dest, "ssh failure", e);
        } catch (JSchException e) {
            throw new MoveException(this, dest, "ssh failure", e);
        }
        return dest;
    }
