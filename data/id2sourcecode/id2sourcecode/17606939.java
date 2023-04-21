        protected boolean testPhp(String[] php, String[] args) {
            Runtime rt = Runtime.getRuntime();
            String[] s = quoteArgs(getTestArgumentArray(php, args));
            byte[] buf = new byte[BUF_SIZE];
            int c, result, errCode;
            InputStream in = null;
            OutputStream out = null;
            InputStream err = null;
            try {
                proc = rt.exec(s, hashToStringArray(env), homeDir);
                in = proc.getInputStream();
                err = proc.getErrorStream();
                out = proc.getOutputStream();
                out.close();
                out = null;
                while ((c = err.read(buf)) > 0) Util.logError(new String(buf, 0, c, ASCII));
                err.close();
                err = null;
                ByteArrayOutputStream outBuf = new ByteArrayOutputStream();
                while ((c = in.read(buf)) > 0) outBuf.write(buf, 0, c);
                in.close();
                in = null;
                errCode = proc.waitFor();
                result = proc.exitValue();
                if (errCode != 0 || result != 0) throw new IOException("php could not be run, returned error code: " + errCode + ", result: " + result);
                try {
                    checkOldPhpVersion(outBuf);
                } catch (Throwable t) {
                    Util.printStackTrace(t);
                } finally {
                    outBuf.close();
                }
            } catch (IOException e) {
                Util.logFatal("Fatal Error: Failed to start PHP " + java.util.Arrays.asList(s) + ", reason: " + e);
                return false;
            } catch (InterruptedException e) {
                return false;
            } finally {
                try {
                    if (in != null) in.close();
                } catch (Exception e) {
                }
                try {
                    if (out != null) out.close();
                } catch (Exception e) {
                }
                try {
                    if (err != null) err.close();
                } catch (Exception e) {
                }
            }
            return true;
        }
