    public void showProcessOutputs(Process iProc) {
        try {
            int b;
            InputStream in = iProc.getInputStream();
            b = in.read();
            if (b != -1) {
                Application.getInstance().writeLog("OUTPUT:\n");
                write(b);
                while ((b = in.read()) != -1) write(b);
            }
            InputStream err = iProc.getInputStream();
            b = err.read();
            if (b != -1) {
                Application.getInstance().writeLog("ERROR:\n");
                write(b);
                while ((b = err.read()) != -1) write(b);
            }
        } catch (IOException e) {
        }
    }
