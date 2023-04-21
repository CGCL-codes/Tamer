    private String read_url(URL url) {
        try {
            InputStream inStream = url.openStream();
            byte[] byte_buff = new byte[256];
            char[] char_buff = new char[256];
            StringBuffer str_buff = new StringBuffer();
            while (true) {
                int bytes_read = inStream.read(byte_buff);
                if (bytes_read == -1) break;
                for (int i = 0; i < bytes_read; ++i) {
                    char_buff[i] = (char) byte_buff[i];
                }
                str_buff.append(char_buff, 0, bytes_read);
            }
            success_flag = true;
            return str_buff.toString();
        } catch (Exception e) {
            System.err.println("URL2String: Exception when reading from " + url.toString());
            System.err.println("            Message is : " + e.getMessage());
            System.err.println("            Stack trace is");
            e.printStackTrace();
            return "";
        }
    }
