    public static synchronized String getURLContent(URL url) {
        String result = "";
        InputStream is = null;
        if (url == null) {
            return result;
        }
        try {
            log(HttpUtils.class, "Open Connection...");
            URLConnection connection = url.openConnection();
            is = connection.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter(END_OF_INPUT);
            result = scanner.next();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
