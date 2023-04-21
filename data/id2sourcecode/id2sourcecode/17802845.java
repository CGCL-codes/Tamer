    public byte[] loadResource(String location) throws IOException {
        if ((location == null) || (location.length() == 0)) {
            throw new IOException("The given resource location must not be null and non empty.");
        }
        URL url = null;
        if (urlRoot == null) {
            url = new URL(location);
        } else {
            int firstColonIdx = location.indexOf(':');
            int firstSlashIdx = location.indexOf('/');
            if ((firstColonIdx > 0) && ((firstSlashIdx < 0) || (firstColonIdx < firstSlashIdx))) {
                url = new URL(location);
            } else {
                url = new URL(urlRoot + location);
            }
        }
        URLConnection cxn = url.openConnection();
        InputStream is = null;
        try {
            byte[] byteBuffer = new byte[2048];
            ByteArrayOutputStream bos = new ByteArrayOutputStream(2048);
            is = cxn.getInputStream();
            int bytesRead = 0;
            while ((bytesRead = is.read(byteBuffer, 0, 2048)) >= 0) {
                bos.write(byteBuffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }
