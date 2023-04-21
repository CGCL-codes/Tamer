    protected String getMovieImage(String url) {
        try {
            URL movie = new URL(url);
            InputStream is = movie.openStream();
            DataInputStream dis = new DataInputStream(new BufferedInputStream(is));
            byte[] buffer = new byte[1000];
            int read = -1;
            ByteArrayOutputStream byteout = new ByteArrayOutputStream();
            while ((read = dis.read(buffer)) >= 0) {
                byteout.write(buffer, 0, read);
            }
            byteout.flush();
            byte[] encodedImage = Base64.encodeBase64(byteout.toByteArray());
            String data = new String(encodedImage, "utf-8");
            is = null;
            dis = null;
            byteout = null;
            return data;
        } catch (Exception e) {
            return null;
        }
    }
