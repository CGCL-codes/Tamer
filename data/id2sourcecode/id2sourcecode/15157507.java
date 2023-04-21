    protected String downloadContent(String url) {
        StringBuffer result = null;
        try {
            URL myUrl = new URL(url);
            URLConnection urlConn = myUrl.openConnection();
            if (myCookie != null) {
                urlConn.setRequestProperty("Cookie", myCookie);
            }
            urlConn.connect();
            Map<String, List<String>> m = urlConn.getHeaderFields();
            if (m.containsKey("Set-Cookie")) {
                List<String> l = m.get("Set-Cookie");
                if (l != null) {
                    Iterator<String> iterator = l.iterator();
                    myCookie = "";
                    while (iterator.hasNext()) {
                        String element = (String) iterator.next();
                        if (myCookie.length() != 0) {
                            myCookie = myCookie + ";";
                        }
                        myCookie = myCookie + element;
                    }
                }
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String s;
            while ((s = in.readLine()) != null) {
                if (result == null) {
                    result = new StringBuffer(s);
                } else {
                    result.append(s);
                }
            }
            in.close();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }
