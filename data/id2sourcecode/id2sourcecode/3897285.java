    public void cleanup() throws IOException {
        canWrite = false;
        timer.cancel();
        bufferedWriter.flush();
        bufferedWriter.close();
        final String newFileName = filename + ".gz";
        final String newFullPath = fullPath + ".gz";
        GZIPOutputStream outgzWriter = new GZIPOutputStream(new FileOutputStream(newFullPath));
        outgzWriter.write(readLogFile().getBytes());
        outgzWriter.close();
        this.filename = newFileName;
        this.fullPath = newFullPath;
    }
