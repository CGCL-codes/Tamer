    private Thread writeStdOut(Process p, String outputDirName) {
        final File outputDir = new File(outputDirName);
        final InputStreamReader isr = new InputStreamReader(p.getInputStream());
        final String outfileName = outputDir.getAbsolutePath() + File.separator + "stdout.txt";
        Thread t_input = new Thread() {

            public void run() {
                FileWriter fw;
                try {
                    fw = new FileWriter(outfileName);
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
                logger.debug("Done writing standard output");
            }
        };
        t_input.start();
        return t_input;
    }
