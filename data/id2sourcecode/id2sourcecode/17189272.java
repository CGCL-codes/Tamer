    private static Object[] getChannelSftp(final Object oparam) throws IOException {
        if (oparam instanceof ChannelSftp) {
            return new Object[] { oparam, null };
        }
        String param = (oparam instanceof String) ? (String) oparam : "";
        if (param.indexOf(':') == -1) {
            throw new IOException("Invalid ssh url : " + param);
        }
        final String host = param.substring(0, param.indexOf(':'));
        param = param.substring(param.indexOf(':') + 1);
        int port = 22;
        String user = null;
        try {
            port = Integer.parseInt(param.substring(0, param.indexOf(':')));
            param = param.substring(param.indexOf(':') + 1);
            user = param.substring(0, param.indexOf(':'));
            param = param.substring(param.indexOf(':') + 1);
        } catch (final Exception e) {
            if (param.indexOf(':') == -1) {
                throw new IOException("Invalid ssh url : " + param);
            }
            user = param.substring(0, param.indexOf(':'));
            param = param.substring(param.indexOf(':') + 1);
        }
        String password = param;
        String chroot = null;
        if (param.indexOf(':') != -1) {
            password = param.substring(0, param.indexOf(':'));
            param = param.substring(param.indexOf(':') + 1);
            chroot = param;
        }
        try {
            return new Object[] { SshFileSystem.connect(host, user, password, port), chroot };
        } catch (final JSchException e) {
            throw new IOException(e);
        }
    }
