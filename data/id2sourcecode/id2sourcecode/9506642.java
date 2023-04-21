    public void run() {
        int read;
        byte[] buf = new byte[1024];
        try {
            while ((read = in.read(buf)) > 0) {
                out.write(buf, 0, read);
                out.flush();
            }
        } catch (ClosedChannelException e) {
        } catch (SocketException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
