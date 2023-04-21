    public static String parseHtml(String fileUrl) {
        InputStream is = null;
        ByteArrayOutputStream bos = null;
        try {
            File myFile = new File(fileUrl);
            is = new FileInputStream(myFile);
            byte[] buffer = new byte[1024];
            int len;
            bos = new ByteArrayOutputStream();
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            String str = new String(bos.toByteArray(), "utf-8");
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }
