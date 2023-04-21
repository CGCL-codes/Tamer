    public void execute() throws BuildException {
        validate();
        long starttime = System.currentTimeMillis();
        String actionToPerform = getTimeoutaction();
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel channel = fis.getChannel();
            long size = channel.size();
            channel.position(size);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            boolean done = false;
            while (!done && System.currentTimeMillis() < starttime + getTimeout()) {
                while (br.ready() && !done) {
                    String line = br.readLine();
                    for (Iterator i = watches.iterator(); i.hasNext(); ) {
                        Watch watch = (Watch) i.next();
                        FilterChain chain = watch.getFilterChain();
                        StringReader sr = new StringReader(line);
                        Reader chained = chainReaders(chain, sr);
                        int read = chained.read();
                        if (-1 != read) {
                            log.debug("Watch matched, final action will be: " + watch.getAction());
                            actionToPerform = watch.getAction();
                            done = true;
                            break;
                        }
                    }
                }
                try {
                    Thread.currentThread().sleep(100);
                } catch (InterruptedException e) {
                }
            }
            fis.close();
        } catch (IOException e) {
            throw new BuildException("File not found: " + e.getMessage(), e);
        }
        if (null != actionToPerform) {
            log.debug("Calling action: " + actionToPerform);
            Action act = (Action) getHandlerActions().getActions().get(actionToPerform);
            if (null != act) {
                for (Iterator i = act.getTasks().iterator(); i.hasNext(); ) {
                    Task task = (Task) i.next();
                    task.perform();
                }
            }
        } else {
            log.info("FileMonitor task finishing without calling any action.");
        }
    }
