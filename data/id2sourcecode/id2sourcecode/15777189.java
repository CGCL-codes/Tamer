    @Override
    public SshNode delete() throws DeleteException {
        SftpATTRS stat;
        ChannelSftp channel;
        try {
            channel = getChannel();
            stat = channel.lstat(escape(slashPath));
            if (stat.isDir()) {
                for (Node child : list()) {
                    child.delete();
                }
                channel.rmdir(escape(slashPath));
            } else {
                channel.rm(escape(slashPath));
            }
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE || e.id == ChannelSftp.SSH_FX_FAILURE) {
                throw new DeleteException(this, new FileNotFoundException());
            }
            throw new DeleteException(this, e);
        } catch (JSchException e) {
            throw new DeleteException(this, e);
        } catch (ListException e) {
            throw new DeleteException(this, e);
        }
        return this;
    }
