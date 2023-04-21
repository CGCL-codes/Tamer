    private void execUnix() {
        try {
            Process process = null;
            process = Runtime.getRuntime().exec(command);
            process.getOutputStream();
            if (listener == null || listener instanceof BasicRuntimeListener) {
                errreader = new PassiveReaderThread(process.getErrorStream(), this);
                inreader = new PassiveReaderThread(process.getInputStream(), this);
            } else {
                outwriter = new Writer(process.getOutputStream(), this);
                errreader = new ActiveReaderThread(process.getErrorStream(), STDERR, this, outwriter);
                inreader = new ActiveReaderThread(process.getInputStream(), STDOUT, this, outwriter);
            }
            inreader.start();
            errreader.start();
            errreader.join();
            inreader.join();
            if (listener instanceof BasicRuntimeListener) {
                ((BasicRuntimeListener) listener).setOutput(((PassiveReaderThread) inreader).getOutput());
                ((BasicRuntimeListener) listener).setErrors(((PassiveReaderThread) errreader).getOutput());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
