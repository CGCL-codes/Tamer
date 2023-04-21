    public static String read(String url) {
        StringBuffer buffer = new StringBuffer();
        try {
            String[][] data = { { "oauth_callback", URLEncoder.encode("http://googlecodesamples.com/oauth_playground/index.php", "UTF-8") }, { "oauth_consumer_key", "anonymous" }, { "oauth_nonce", a64BitRandomString() }, { "oauth_signature_method", "HMAC-SHA1" }, { "oauth_timestamp", timeSinceEpochInMillis() }, { "oauth_signature", "" }, { "oauth_version", "1.0" }, { "scope", URLEncoder.encode("https://www.google.com/calendar/feeds/", "UTF-8") } };
            String signature_base_string = "GET&" + URLEncoder.encode(url, "UTF-8") + "&";
            for (int i = 0; i < data.length; i++) {
                if (i != 5) {
                    logger.debug(i);
                    signature_base_string += URLEncoder.encode(data[i][0], "UTF-8") + "%3D" + URLEncoder.encode(data[i][1], "UTF-8") + "%26";
                }
            }
            signature_base_string = signature_base_string.substring(0, signature_base_string.length() - 3);
            Mac m = Mac.getInstance("HmacSHA1");
            m.init(new SecretKeySpec("anonymous".getBytes(), "HmacSHA1"));
            m.update(signature_base_string.getBytes());
            byte[] res = m.doFinal();
            String sig = URLEncoder.encode(String.valueOf(Base64.encode(res)), "UTF8");
            data[5][1] = sig;
            String header = "OAuth ";
            int i = 0;
            for (String[] item : data) {
                if (i != 7) {
                    header += item[0] + "=\"" + item[1] + "\", ";
                }
                i++;
            }
            header = header.substring(0, header.length() - 2);
            logger.debug("Signature Base String: " + signature_base_string);
            logger.debug("Authorization Header: " + header);
            logger.debug("Signature: " + sig);
            String charset = "UTF-8";
            URLConnection connection = new URL(url + "?scope=" + URLEncoder.encode("https://www.google.com/calendar/feeds/", "UTF-8")).openConnection();
            connection.setRequestProperty("Authorization", header);
            connection.setRequestProperty("Accept", "*/*");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String read;
            while ((read = reader.readLine()) != null) {
                buffer.append(read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
