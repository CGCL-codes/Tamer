    public static void sendCPSignal(Socket inputSocket, ByteBuffer byteBuffer) {
        try {
            inputSocket.getChannel().write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
