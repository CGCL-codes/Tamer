    public void writeToStream() {
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            byte input[] = new byte[4096];
            int bytesread;
            while ((bytesread = is.read(input)) != -1) os.write(input, 0, bytesread);
        } catch (Exception e) {
            if (!e.getMessage().equals("Pipe closed")) System.err.println("StreamThreader error: " + e.toString());
        } finally {
            stop();
        }
    }
