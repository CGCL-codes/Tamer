    public boolean deletePersonPhoto(String idPerson) {
        try {
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket(GDSystem.getServerIp(), 9999);
            File file = new File(Constants.PHOTO_DIR + idPerson + ".jpg");
            FileChannel fileIn = new FileInputStream(file).getChannel();
            OutputStream dataout = sslsocket.getOutputStream();
            if (file.exists()) {
                return file.delete();
            } else {
                return false;
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
