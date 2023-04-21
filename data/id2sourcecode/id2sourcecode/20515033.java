    protected String encodeBase64(String file) {
        try {
            URL url = new URL(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(32000);
            bos.write("data:;base64,".getBytes());
            Base64EncoderStream base = new Base64EncoderStream(bos);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            int l = -1;
            byte[] b = new byte[1024];
            while ((l = in.read(b)) > -1) {
                base.write(b, 0, l);
            }
            in.close();
            base.flush();
            base.close();
            return new String(bos.toByteArray());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }
