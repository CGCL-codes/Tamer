    private void real_download(URL url, OutputStream pipe) throws Exception {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;
        try {
            URLConnection con = url.openConnection();
            int l = con.getContentLength();
            flength = l > 0 ? l : 1 << 16;
            inputStream = con.getInputStream();
            if (con instanceof java.net.HttpURLConnection) {
                int c = ((java.net.HttpURLConnection) con).getResponseCode();
                if (c < 200 || c > 299) throw new FileNotFoundException();
            }
            byte buf[] = new byte[4096];
            for (; ; ) {
                if (interrupted()) throw new InterruptedException();
                int n = inputStream.available();
                if (n < 1) n = 1; else if (n > buf.length) n = buf.length;
                n = inputStream.read(buf, 0, n);
                if (n <= 0) break;
                arrayOutputStream.write(buf, 0, n);
                System.out.println("escribimos el fichero en accesible");
                pipe.write(buf, 0, n);
                floaded += n;
            }
            cache.put(url, arrayOutputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
