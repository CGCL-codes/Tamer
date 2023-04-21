    public static InputStream getStream(URL url) throws FileNotFoundException, IOException {
        InputStream result = null;
        try {
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("User-Agent", "HTTP Client");
            uc.setRequestProperty("Accept", "image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, image/png, */*");
            uc.setRequestProperty("Accept-Encoding", "gzip");
            uc.setRequestProperty("Accept-Language", "en");
            uc.setRequestProperty("Accept-Charset", "iso-8859-1,*,utf-8");
            uc.setDoOutput(true);
            uc.setDoInput(true);
            uc.setUseCaches(false);
            uc.setAllowUserInteraction(true);
            result = uc.getInputStream();
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return result;
    }
