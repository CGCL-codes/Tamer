    protected String post(String address, Vector args, Parser parser) throws IOException {
        String ret = null;
        URL url;
        HttpURLConnection conn;
        String formData = new String();
        for (int i = 0; i < args.size(); i += 2) {
            if (showPost) {
                System.out.print("POST: " + args.get(i).toString() + " = ");
                System.out.println(args.get(i + 1).toString());
            }
            formData += URLEncoder.encode(args.get(i).toString(), "UTF-8") + "=" + URLEncoder.encode(args.get(i + 1).toString(), "UTF-8");
            if (i + 2 != args.size()) formData += "&";
        }
        try {
            url = new URL("http://" + getServer() + address);
            if (showAddress || showPost) {
                System.out.println("POST: " + address);
                System.out.println("POST: " + formData);
            }
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            String auth = getUsername() + ":" + getPassword();
            conn.setRequestProperty("Authorization", "Basic " + B64Encode(auth.getBytes()));
            if (showAuth) {
                System.out.println("POST: AUTH: " + auth);
            }
            conn.setRequestProperty("Content Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + Integer.toString(formData.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.writeBytes(formData);
            wr.flush();
            wr.close();
            if (conn.getResponseCode() != 200 && conn.getResponseCode() != 302) {
                try {
                    ret = conn.getResponseMessage();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                if (ret == null) {
                    ret = "Unknown Error";
                }
                if (parser != null) {
                    parser.begin(ret);
                }
                return ret;
            }
            isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String s;
            getBuffer = new StringBuffer();
            if (parser != null) {
                parser.begin(null);
            }
            while ((s = reader.readLine()) != null) {
                if (showGet) {
                    System.out.println(s);
                }
                getBuffer.append(s);
                getBuffer.append("\n");
                if (parser == null) {
                } else {
                    if (!parser.parse(s)) {
                        return "Parse Error";
                    }
                }
            }
            if (parser == null && showUnparsedOutput) {
                System.out.println(getBuffer);
            }
            if (parser != null) {
                parser.end(false);
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            if (parser != null) {
                parser.end(true);
            }
            throw (ioe);
        } finally {
            try {
                isr.close();
            } catch (Exception e) {
            }
        }
        return ret;
    }
