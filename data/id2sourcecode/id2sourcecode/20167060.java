    public static String connectAndGetResponse(String url_str, String itunes_code) {
        String xml_string = null;
        try {
            URL url = new URL(url_str);
            HttpURLConnection http_conn = (HttpURLConnection) url.openConnection();
            http_conn.setRequestProperty("X-Apple-Store-Front", itunes_code);
            http_conn.setRequestProperty("User-Agent", "iTunes-iPhone/2.2 (2)");
            http_conn.connect();
            if (http_conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                int content_length = http_conn.getContentLength();
                InputStream input_stream = http_conn.getInputStream();
                int alloc_length = content_length;
                int read_total = 0;
                if (content_length < 0) {
                    alloc_length = 512 * 1024;
                }
                byte[] read_buffer = new byte[alloc_length];
                try {
                    while (read_total < read_buffer.length) {
                        int read = input_stream.read(read_buffer, read_total, read_buffer.length - read_total);
                        if (read > 0) {
                            read_total += read;
                            if (content_length < 0) {
                            }
                        } else {
                            break;
                        }
                    }
                } catch (Exception e) {
                }
                if (read_total > 0) {
                    xml_string = new String(read_buffer, 0, read_total, "UTF-8");
                }
            }
        } catch (Exception e) {
            System.err.println("What went wrong? : " + e);
        }
        return xml_string;
    }
