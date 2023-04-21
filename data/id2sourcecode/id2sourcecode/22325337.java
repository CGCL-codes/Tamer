    private void readLargeFile(PrintWriter writer) throws FileNotFoundException, IOException {
        RandomAccessFile random = null;
        boolean hasStarted = false;
        try {
            random = new RandomAccessFile(logFile, "r");
            random.seek(logFile.length() - LogReader.MAX_FILE_LENGTH);
            for (; ; ) {
                String line = random.readLine();
                if (line == null) break;
                line = line.trim();
                if (line.length() == 0) continue;
                if (!hasStarted && (line.startsWith("!ENTRY") || line.startsWith("!SESSION"))) hasStarted = true;
                if (hasStarted) writer.println(line);
                continue;
            }
        } finally {
            try {
                if (random != null) random.close();
            } catch (IOException e1) {
            }
        }
    }
