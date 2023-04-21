    public static Object[] getURLStream(String address) throws Exception {
        URLConnection conn = null;
        InputStream in = null;
        Object[] urlBOS = new Object[4];
        Object[] urlResponse = new Object[3];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Integer code = 0;
        String newURL = address;
        String host = "";
        try {
            URL url = new URL(newURL);
            conn = url.openConnection();
            conn.setConnectTimeout(30000);
            urlResponse = getURLResponse(conn);
            newURL = (String) urlResponse[0];
            code = (Integer) urlResponse[1];
            host = (String) urlResponse[2];
            urlResponse = null;
            if (code < 400) {
                in = conn.getInputStream();
                byte[] buffer = new byte[1024];
                int numRead;
                long numWritten = 0;
                while ((numRead = in.read(buffer)) != -1) {
                    numWritten += numRead;
                    if (resourcesFree(numWritten)) {
                        bos.write(buffer, 0, numRead);
                    } else {
                        throw new Exception("Not enough available memory.");
                    }
                }
                buffer = null;
            }
        } catch (Exception e) {
            throw new Exception("Could not download url " + newURL + " ERROR: " + e.getMessage());
        } finally {
            urlBOS[0] = newURL;
            urlBOS[1] = code;
            urlBOS[2] = bos;
            urlBOS[3] = host;
            bos = null;
            in = null;
        }
        return urlBOS;
    }
