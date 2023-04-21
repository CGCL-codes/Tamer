    public static Image getImage(String url) throws IOException {
        ContentConnection connection = (ContentConnection) Connector.open(url);
        DataInputStream iStrm = connection.openDataInputStream();
        ByteArrayOutputStream bStrm = null;
        Image im = null;
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
            im = Image.createImage(imageData, 0, imageData.length);
        } finally {
            if (iStrm != null) iStrm.close();
            if (connection != null) connection.close();
            if (bStrm != null) bStrm.close();
        }
        return (im == null ? null : im);
    }
