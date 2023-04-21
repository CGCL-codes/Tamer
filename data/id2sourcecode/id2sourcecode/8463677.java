    public static String translate(String orig_text, String orig_google_code, String trans_google_code) {
        String trans_string = null;
        try {
            String safe_text = orig_text;
            safe_text = urlencode(safe_text);
            String file_string = FILE_START_STRING + orig_google_code + "%7C" + trans_google_code + TEXT_VAR + safe_text;
            URL trans_url = new URL(PROTOCOL, HOST, file_string);
            HttpURLConnection connection = (HttpURLConnection) trans_url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream input = connection.getInputStream();
                byte[] buffer = new byte[100000];
                int offset = 0;
                while (true) {
                    int read = input.read(buffer, offset, buffer.length - offset);
                    if (read > 0) {
                        offset += read;
                    } else {
                        break;
                    }
                }
                if (offset > 0) {
                    String response = new String(buffer, 0, offset, "UTF-8");
                    String RESPONSE_STATUS = "\"responseStatus\":";
                    int response_status_index = response.indexOf(RESPONSE_STATUS);
                    if (response_status_index < 0) {
                        return null;
                    }
                    int end_index = response.indexOf("}", response_status_index);
                    int response_code = 0;
                    try {
                        String response_code_str = response.substring(response_status_index + RESPONSE_STATUS.length(), end_index);
                        response_code_str = response_code_str.trim();
                        response_code = Integer.parseInt(response_code_str);
                    } catch (Exception e) {
                    }
                    if (response_code != 200) {
                        return null;
                    }
                    String TRANSLATION_TAG = "\"translatedText\":";
                    int trans_start = response.indexOf(TRANSLATION_TAG);
                    if (trans_start < 0) {
                        return null;
                    }
                    int trans_end = response.indexOf("}", trans_start);
                    if (trans_end < 0) {
                        return null;
                    }
                    String trans_text = response.substring(trans_start + TRANSLATION_TAG.length(), trans_end);
                    trans_text = trans_text.trim();
                    if (trans_text.length() <= 2) {
                        return null;
                    }
                    trans_text = trans_text.substring(1, trans_text.length() - 1);
                    trans_string = unescapeCharacters(trans_text);
                }
            } else {
                System.err.println("Failed to get translation: " + connection.getResponseCode());
            }
            connection.disconnect();
        } catch (Exception e) {
            System.err.println("Translation error: " + e);
        }
        return trans_string;
    }
