    private BufferedWriter openCacheFile() throws IOException {
        boolean hasLock = false;
        while (!hasLock) {
            synchronized (this) {
                if (!lock) {
                    lock = true;
                    hasLock = true;
                }
            }
            if (!hasLock) try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        tmpFile = new File(cacheDir, "changeLog.tmp");
        if (tmpFile.exists()) tmpFile.delete();
        tmpFile.createNewFile();
        BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile));
        if (cacheFile.exists()) {
            BufferedReader in = new BufferedReader(new FileReader(cacheFile));
            String nextLine = in.readLine();
            while (nextLine != null) {
                out.write(nextLine);
                out.newLine();
                nextLine = in.readLine();
            }
            in.close();
        }
        return out;
    }
