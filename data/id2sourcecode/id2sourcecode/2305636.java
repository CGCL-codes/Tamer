    private void refreshAPIXML() {
        if (DEBUG) System.out.println("Refreshing XML");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String UserID = prefs.get(USER_ID_KEY, USER_ID_DEFUALT);
        String APIKey = prefs.get(API_KEY_KEY, API_KEY_DEFUALT);
        String CharacterID = prefs.get(CHARACTER_ID_KEY, CHARACTER_ID_DEFUALT);
        if (DEBUG) System.out.println("UID: " + UserID + " API: " + APIKey + " CHAR: " + CharacterID);
        eveStatus = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String data = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(UserID, "UTF-8");
            data += "&" + URLEncoder.encode("apiKey", "UTF-8") + "=" + URLEncoder.encode(APIKey, "UTF-8");
            data += "&" + URLEncoder.encode("characterID", "UTF-8") + "=" + URLEncoder.encode(CharacterID, "UTF-8");
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
            eveStatus = builder.parse(connection.getInputStream());
        } catch (UnsupportedEncodingException ex) {
            eveStatus = null;
        } catch (ProtocolException ex) {
            eveStatus = null;
        } catch (MalformedURLException ex) {
            eveStatus = null;
        } catch (ParserConfigurationException ex) {
            eveStatus = null;
        } catch (SAXException ex) {
            eveStatus = null;
        } catch (IOException ex) {
            eveStatus = null;
        }
    }
