    public void execute() throws MojoExecutionException, MojoFailureException {
        if (skip) {
            getLog().info("skipped waiting for " + protocol + "://" + host);
            return;
        }
        URL url = getURL();
        int count = maxcount;
        int trials = 1;
        getLog().info("(timeout: " + timeout + " maxcount: " + maxcount + ")");
        while (true) {
            try {
                getLog().info(trials + ": Try to connect to " + url);
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(timeout);
                InputStream stream = connection.getInputStream();
                getLog().info("success - reached " + url);
                stream.close();
                break;
            } catch (IOException e) {
                if (count > 1) {
                    count--;
                } else if (count != 0) {
                    getLog().warn("cannot connect to " + url, e);
                    throw new MojoExecutionException("cannot connect to " + url, e);
                }
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e1) {
                }
                trials++;
            }
        }
    }
