    public static String getServiceResponse(String service) {
        InputStream inputStream;
        URL url;
        String revision = "";
        try {
            url = new URL(service);
            inputStream = url.openStream();
        } catch (MalformedURLException e) {
            inputStream = null;
        } catch (IOException e) {
            inputStream = null;
        }
        if (inputStream != null) {
            try {
                int bytesRead;
                BufferedInputStream buffer = new BufferedInputStream(inputStream);
                ByteArrayOutputStream content = new ByteArrayOutputStream();
                byte data[] = new byte[512];
                while ((bytesRead = buffer.read(data, 0, 512)) != -1) content.write(data, 0, bytesRead);
                revision = content.toString("UTF-8").trim();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return revision;
    }
