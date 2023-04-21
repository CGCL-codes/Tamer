    @SuppressWarnings("deprecation")
    public static int useGetAccount(String name, String email, String date, String username, String token) {
        try {
            URL url = new URL("http://eiffel.itba.edu.ar/hci/service/Security.groovy?method=UpdateAccount&username=" + username + "&authentication_token=" + token);
            String ret = "<account>" + "<name>" + name + "</name>" + "<email>" + email + "</email>" + "<birth_date>" + date + "</birth_date>" + "</account>";
            URLConnection urlc = url.openConnection();
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);
            String encodedPost = URLEncoder.encode(ret);
            PrintStream ps = new PrintStream(urlc.getOutputStream());
            ps.print("account=" + encodedPost);
            ps.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String str;
            StringBuffer sb = new StringBuffer();
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            br.close();
            String response = sb.toString();
            if (response == null) {
                return -1;
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(response));
            Document dom = db.parse(is);
            NodeList nl = dom.getElementsByTagName("response");
            String status = ((Element) nl.item(0)).getAttributes().item(0).getTextContent();
            if (status.equalsIgnoreCase("ok")) {
                return 0;
            } else {
                nl = dom.getElementsByTagName("error");
                String code = ((Element) nl.item(0)).getAttributes().item(0).getTextContent();
                if (code.toString().equals("201")) {
                    return -1;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
