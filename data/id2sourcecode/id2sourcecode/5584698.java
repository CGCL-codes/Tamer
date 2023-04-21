    public static final int testSSHAccess(final PrintStream out, final BufferedReader in, final SSHAccessor ssh, final String host, final String username, final String password) throws IOException {
        {
            final long connStart = System.currentTimeMillis();
            ssh.connect(host);
            final long connEnd = System.currentTimeMillis(), connDuration = connEnd - connStart;
            out.println("\tConnected to " + host + " in " + connDuration + " msec.");
        }
        {
            final long lgnStart = System.currentTimeMillis();
            ssh.login(username, password);
            final long lgnEnd = System.currentTimeMillis(), lgnDuration = lgnEnd - lgnStart;
            out.println("\tLogged in after " + lgnDuration + " msec.");
        }
        return testSSHAccessChannels(out, in, ssh);
    }
