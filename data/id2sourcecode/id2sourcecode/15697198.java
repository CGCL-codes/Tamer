    private static void downloadFile(final File inFile, final File outDir, final long sleepTimeout, final Ui ui) throws IOException {
        final URI uri = inFile.toURI();
        final URL url = uri.toURL();
        final String contentType = url.openConnection().getContentType();
        ui.println("URI: {0}", uri);
        ui.println("URL: {0}", url);
        if (!inFile.exists()) {
            ui.println("No such file or directory.");
            return;
        }
        if (inFile.isDirectory()) {
            ui.println("File is a directory.");
            return;
        }
        final boolean outDirExists = mkdir(outDir, ui);
        if (!outDirExists) {
            ui.println("{0}: failed to create directory.", outDir);
            return;
        }
        final long inFileLength = inFile.length();
        final String basename = inFile.getName();
        final File outFile = new File(outDir, basename);
        ui.println("Downloading {0} ({1}, {2} byte(s)) to {3}", url, contentType, Long.valueOf(inFileLength), outFile);
        ui.setProgressMaximum(inFileLength);
        final OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile, true));
        final long initialOutFileLength = outFile.length();
        final AtomicLong progressValue = new AtomicLong(initialOutFileLength);
        final Thread progressUpdater = new Thread("ProgressUpdater") {

            @Override
            public void run() {
                while (ui.setProgressValue(progressValue.get()) != inFileLength && !interrupted()) {
                    try {
                        sleep(100);
                    } catch (final InterruptedException ie) {
                        break;
                    }
                }
            }
        };
        progressUpdater.start();
        final long t0 = System.currentTimeMillis();
        final long t0Precise = System.nanoTime();
        while (!Thread.interrupted()) {
            InputStream in = null;
            try {
                in = new BufferedInputStream(new FileInputStream(inFile));
                if (outFile.exists()) {
                    if (!outFile.isFile()) {
                        ui.println("{0} is not a regular file.", outFile);
                        break;
                    }
                    final long outFileLength = outFile.length();
                    if (outFileLength == inFileLength) {
                        ui.println("File has been fully retrieved.");
                        break;
                    } else if (outFileLength > inFileLength) {
                        ui.println("Local file is {0} byte(s) longer than the remote one (local file length: {1}; remote file length: {2}).", Long.valueOf(outFileLength - inFileLength), Long.valueOf(outFileLength), Long.valueOf(inFileLength));
                        progressUpdater.interrupt();
                        break;
                    }
                    if (outFileLength != 0) {
                        ui.println("Skipping {0} byte(s)...", Long.valueOf(outFileLength));
                        final long bytesSkipped = in.skip(outFileLength);
                        if (bytesSkipped != outFileLength) {
                            ui.println("Expected to skip {0} byte(s); skipped {1} instead.", Long.valueOf(outFileLength), Long.valueOf(bytesSkipped));
                            progressUpdater.interrupt();
                            break;
                        }
                        ui.println("Done skipping");
                    }
                }
                int b;
                while ((b = in.read()) != -1) {
                    out.write(b);
                    progressValue.getAndIncrement();
                }
                break;
            } catch (final IOException ioe) {
                ui.println(ioe.getMessage());
                ui.println("Continuing in {0} ms...", Long.valueOf(sleepTimeout));
                try {
                    Thread.sleep(sleepTimeout);
                } catch (final InterruptedException ie) {
                    ui.println("Interrupted");
                    break;
                }
                continue;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (final IOException ioe) {
                }
            }
        }
        out.flush();
        out.close();
        final long t1 = System.currentTimeMillis();
        final long t1Precise = System.nanoTime();
        try {
            progressUpdater.join();
        } catch (final InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        final long timeMillis = t1 - t0;
        final double timeMillisPrecise = (t1Precise - t0Precise) / 1000 / 1e3;
        final long finalOutFileLength = outFile.length();
        final long bytesTransferred = finalOutFileLength - initialOutFileLength;
        final double time;
        final long rate;
        if (timeMillis == 0) {
            time = timeMillisPrecise / 1e3;
            rate = (long) (bytesTransferred / timeMillisPrecise * 1000L);
        } else {
            time = timeMillis / 1e3;
            rate = (long) (bytesTransferred / ((double) timeMillis) * 1000L);
        }
        ui.println("{0} of {1} byte(s) retrieved in {2} second(s); {3} byte(s) per second", Long.valueOf(finalOutFileLength), Long.valueOf(inFileLength), Double.valueOf(time), Long.valueOf(rate));
    }
