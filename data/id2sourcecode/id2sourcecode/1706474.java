    static String unGZipData(byte[] data) {
        try {
            ByteArrayInputStream bais;
            GZIPInputStream zippedIn;
            ByteArrayOutputStream unzippedOut;
            bais = new ByteArrayInputStream(data);
            zippedIn = new GZIPInputStream(bais);
            unzippedOut = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesInBuffer;
            while ((bytesInBuffer = zippedIn.read(buffer)) > 0) unzippedOut.write(buffer, 0, bytesInBuffer);
            return new String(unzippedOut.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
