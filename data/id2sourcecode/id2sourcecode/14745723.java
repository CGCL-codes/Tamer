    public void exec() throws IOException, InterruptedException {
        InputStream stream;
        int i;
        int writePointer;
        if (executed) {
            throw new IllegalStateException("You have already executed '" + program + "'.  Run clear().");
        }
        Process proc = Runtime.getRuntime().exec(unshift(program, args));
        OutputStream outputStream = proc.getOutputStream();
        if (input != null) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(input));
            while ((i = bis.read(ba)) > 0) {
                outputStream.write(ba, 0, i);
            }
        }
        outputStream.close();
        stream = proc.getInputStream();
        writePointer = 0;
        while ((i = stream.read(ba, writePointer, ba.length - writePointer)) > 0) {
            writePointer += i;
        }
        if (i > -1) {
            throw new IOException(program + " generated > " + (ba.length - 1) + " bytes of standard output");
        }
        stream.close();
        executed = true;
        stdout = new String(ba, 0, writePointer);
        stream = proc.getErrorStream();
        writePointer = 0;
        while ((i = stream.read(ba, writePointer, ba.length - writePointer)) > 0) {
            writePointer += i;
        }
        if (i > -1) {
            throw new IOException(program + " generated > " + (ba.length - 1) + " bytes of error output");
        }
        stream.close();
        errout = new String(ba, 0, writePointer);
        exitValue = proc.waitFor();
    }
