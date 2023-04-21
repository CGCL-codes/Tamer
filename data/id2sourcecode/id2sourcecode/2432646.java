    public void run() {
        byte[] buffer = new byte[1024];
        try {
            for (int count = 0; (count = is.read(buffer)) >= 0; ) os.write(buffer, 0, count);
        } catch (IOException e) {
            throw new ToolsRuntimeException(e);
        }
    }
