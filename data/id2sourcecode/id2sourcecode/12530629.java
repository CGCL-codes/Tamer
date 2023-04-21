    public static void XMLEcho() {
        Document doc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode("1412045", "UTF-8");
            data += "&" + URLEncoder.encode("apiKey", "UTF-8") + "=" + URLEncoder.encode("EjmPCQNFMp73b229pXCtiNTwMNVE2q5kUftEwXUVbHHJvH25P3rOOJFqn8yUNnl2", "UTF-8");
            data += "&" + URLEncoder.encode("characterID", "UTF-8") + "=" + URLEncoder.encode("284049160", "UTF-8");
            String link = "/char/SkillInTraining.xml.aspx";
            URL url = new URL("http://api.eve-online.com" + link);
            URLConnection connection = url.openConnection();
            ((HttpURLConnection) connection).setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            PrintWriter output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            output.write(data);
            output.flush();
            output.close();
            doc = builder.parse(connection.getInputStream());
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (doc == null) {
            System.err.println("doc null");
            System.exit(1);
        }
        System.out.println(doc.getElementsByTagName("trainingTypeID").item(0).getFirstChild().getNodeValue());
    }
