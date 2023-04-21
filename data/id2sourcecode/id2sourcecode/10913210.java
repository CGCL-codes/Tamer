    private void deleteUserOnly(String username) throws IOException, InterruptedException {
        String htpasswdScript = SVNAdminServiceProperties.getHtpasswdScript();
        String htpasswdAuthFile = SVNAdminServiceProperties.getHtpasswdUserAuthFile();
        File authFile = new File(htpasswdAuthFile);
        if (!authFile.exists()) {
            throw new RuntimeException("htpasswd authorization file does not exist, could not remove the username: " + username);
        }
        if (!authFile.canRead() || !authFile.canWrite()) {
            throw new RuntimeException("Can't read/write htpasswd authorization file: " + htpasswdAuthFile);
        }
        final String deleteUserFlag = " -D ";
        final String cmd = htpasswdScript + " " + deleteUserFlag + " " + htpasswdAuthFile + " " + username;
        String envp[] = new String[1];
        envp[0] = "PATH=" + System.getProperty("java.library.path");
        LOG.debug("executing cmd: " + cmd + ", with PATH: " + envp[0]);
        Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec(cmd, envp);
        BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));
        String line = null;
        while ((line = input.readLine()) != null) {
            LOG.debug(line);
        }
        int exitVal = pr.waitFor();
        LOG.debug("Exited with code " + exitVal);
    }
