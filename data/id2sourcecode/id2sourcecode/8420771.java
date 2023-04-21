    public void run() {
        if (DebugFile.trace) {
            DebugFile.writeln("Begin StreamPipe.PipeThread.run()");
        }
        int total = 0;
        try {
            int length;
            while ((length = in.read(buffer)) > 0) {
                if (DebugFile.trace) DebugFile.writeln("OutputStream.write(byte[], 0, " + String.valueOf(length) + ")");
                out.write(buffer, 0, length);
                if (flush) out.flush();
                total += length;
            }
        } catch (IOException ex) {
        }
        if (DebugFile.trace) {
            DebugFile.writeln("End StreamPipe.PipeThread.run() : " + String.valueOf(total));
        }
    }
