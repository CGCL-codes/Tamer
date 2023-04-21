    public static String WinFindMimeFromData(URL url) {
        byte[] urlBytes;
        byte[] result;
        String urlString = url.toString();
        urlBytes = stringToByteArray(urlString);
        result = FindMimeFromData(urlBytes, null);
        if (result != null) {
            return byteArrayToString(result);
        } else {
            byte[] dataBytes = new byte[256];
            DataInputStream inStream = null;
            try {
                inStream = new DataInputStream(url.openStream());
                inStream.read(dataBytes, 0, 256);
                inStream.close();
            } catch (IOException e) {
                return null;
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                    }
                }
            }
            result = FindMimeFromData(null, dataBytes);
            if (result != null) {
                return byteArrayToString(result);
            } else {
                return null;
            }
        }
    }
