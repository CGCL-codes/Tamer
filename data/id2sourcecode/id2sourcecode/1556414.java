    private Thread writeStdErr(Process p, String outputDirName, String outputFileName) {
        final File outputDir = new File(outputDirName);
        final InputStreamReader isr = new InputStreamReader(p.getErrorStream());
        final String errfileName = outputDir.getAbsolutePath() + File.separator + outputFileName;
        Thread t_error = new Thread() {

            public void run() {
                FileWriter fw;
                try {
                    fw = new FileWriter(errfileName);
                } catch (IOException ioe) {
                    logger.error(ioe);
                    return;
                }
                int bytes = 0;
                char[] buf = new char[256];
                while (!(done && (bytes < 0))) {
                    try {
                        bytes = isr.read(buf);
                        if (bytes > 0) {
                            fw.write(buf, 0, bytes);
                            fw.flush();
                        }
                    } catch (IOException ignore) {
                        break;
                    }
                }
                try {
                    fw.close();
                } catch (IOException ioe) {
                    logger.error(ioe);
                    return;
                }
                logger.debug("Done writing standard error");
            }
        };
        t_error.start();
        return t_error;
    }
