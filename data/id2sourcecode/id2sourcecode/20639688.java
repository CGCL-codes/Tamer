    static Channel getChannel(String type) {
        if (type.equals("session")) {
            return new ChannelSession();
        }
        if (type.equals("shell")) {
            return new ChannelShell();
        }
        if (type.equals("exec")) {
            return new ChannelExec();
        }
        if (type.equals("x11")) {
            return new ChannelX11();
        }
        if (type.equals("auth-agent@openssh.com")) {
            return new ChannelAgentForwarding();
        }
        if (type.equals("direct-tcpip")) {
            return new ChannelDirectTCPIP();
        }
        if (type.equals("forwarded-tcpip")) {
            return new ChannelForwardedTCPIP();
        }
        if (type.equals("sftp")) {
            return new ChannelSftp();
        }
        if (type.equals("subsystem")) {
            return new ChannelSubsystem();
        }
        return null;
    }
