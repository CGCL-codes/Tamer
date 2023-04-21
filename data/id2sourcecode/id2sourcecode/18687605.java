    public static byte[] getImage(String url) throws IOException {
        sendBytes += (SERVER_URL + url).length();
        ContentConnection connection = (ContentConnection) Connector.open(SERVER_URL + url);
        DataInputStream iStrm = connection.openDataInputStream();
        ByteArrayOutputStream bStrm = null;
        try {
            byte imageData[];
            int length = (int) connection.getLength();
            if (length != -1) {
                imageData = new byte[length];
                iStrm.readFully(imageData);
            } else {
                bStrm = new ByteArrayOutputStream();
                int ch;
                while ((ch = iStrm.read()) != -1) bStrm.write(ch);
                imageData = bStrm.toByteArray();
                bStrm.close();
            }
            recievedBytes += imageData.length;
            return (imageData == null ? null : imageData);
        } finally {
            if (iStrm != null) iStrm.close();
            if (connection != null) connection.close();
            if (bStrm != null) bStrm.close();
        }
    }
