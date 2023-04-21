    @Override
    public void run() {
        byte buffer[] = new byte[2048];
        int length;
        try {
            while ((length = in.read(buffer)) > -1) if (out != null) out.write(buffer, 0, length);
            if (out != null) out.flush();
        } catch (IOException e) {
            pid.destroy();
        } finally {
            try {
                if ((out != null) && (doCleanup)) out.close();
            } catch (IOException e) {
                pid.destroy();
            }
        }
    }
