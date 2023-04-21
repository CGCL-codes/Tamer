    public static int consumeOutput(Process p, OutputStream destOut, OutputStream destErr, boolean eager) {
        int exitValue = -1;
        try {
            InputStream in = p.getInputStream();
            InputStream err = p.getErrorStream();
            boolean finished = false;
            while (!finished) {
                try {
                    int c;
                    while (in.available() > 0 && (c = in.read()) != -1) if (destOut != null) destOut.write(c);
                    while (err.available() > 0 && (c = err.read()) != -1) if (destErr != null) destErr.write(c);
                    exitValue = p.exitValue();
                    finished = true;
                } catch (IllegalThreadStateException e) {
                    Thread.sleep(eager ? 10 : 500);
                }
            }
        } catch (Exception e) {
            System.err.println("doWaitFor(): unexpected exception - " + e.getMessage());
        }
        return exitValue;
    }
