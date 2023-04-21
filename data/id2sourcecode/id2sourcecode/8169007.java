    private boolean fetchUrl(File tmpFile, String urlString, String description, ITaskMonitor monitor) {
        URL url;
        description += " (%1$d%%, %2$.0f KiB/s, %3$d %4$s left)";
        FileOutputStream os = null;
        InputStream is = null;
        try {
            url = new URL(urlString);
            is = url.openStream();
            os = new FileOutputStream(tmpFile);
            MessageDigest digester = getChecksumType().getMessageDigest();
            byte[] buf = new byte[65536];
            int n;
            long total = 0;
            long size = getSize();
            long inc = size / NUM_MONITOR_INC;
            long next_inc = inc;
            long startMs = System.currentTimeMillis();
            long nextMs = startMs + 2000;
            while ((n = is.read(buf)) >= 0) {
                if (n > 0) {
                    os.write(buf, 0, n);
                    digester.update(buf, 0, n);
                }
                long timeMs = System.currentTimeMillis();
                total += n;
                if (total >= next_inc) {
                    monitor.incProgress(1);
                    next_inc += inc;
                }
                if (timeMs > nextMs) {
                    long delta = timeMs - startMs;
                    if (total > 0 && delta > 0) {
                        int percent = (int) (100 * total / size);
                        float speed = (float) total / (float) delta * (1000.f / 1024.f);
                        int timeLeft = (speed > 1e-3) ? (int) (((size - total) / 1024.0f) / speed) : 0;
                        String timeUnit = "seconds";
                        if (timeLeft > 120) {
                            timeUnit = "minutes";
                            timeLeft /= 60;
                        }
                        monitor.setDescription(description, percent, speed, timeLeft, timeUnit);
                    }
                    nextMs = timeMs + 1000;
                }
                if (monitor.isCancelRequested()) {
                    monitor.setResult("Download aborted by user at %1$d bytes.", total);
                    return false;
                }
            }
            if (total != size) {
                monitor.setResult("Download finished with wrong size. Expected %1$d bytes, got %2$d bytes.", size, total);
                return false;
            }
            String actual = getDigestChecksum(digester);
            String expected = getChecksum();
            if (!actual.equalsIgnoreCase(expected)) {
                monitor.setResult("Download finished with wrong checksum. Expected %1$s, got %2$s.", expected, actual);
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            monitor.setResult("File not found: %1$s", e.getMessage());
        } catch (Exception e) {
            monitor.setResult(e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return false;
    }
