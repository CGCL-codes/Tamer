    public String setAuthenticator(User user) {
        String rpc = user.getProject().getBaseURL() + "lookup_account.php?email_addr=%s&passwd_hash=%s";
        String auth = null;
        InputStream stream = null;
        try {
            URL url = new URL(String.format(rpc, user.getEmail(), user.getPasswordHash()));
            Log.d(TAG, String.format(user.getProject().name() + ": " + rpc, user.getEmail(), user.getPasswordHash()));
            URLConnection urlConnection = url.openConnection();
            stream = urlConnection.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(stream);
            Element root = document.getDocumentElement();
            assertXMLDocumentStatus(user, root);
            auth = DOMUtils.getText(root, "authenticator");
        } catch (MalformedURLException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (IOException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (SAXException e) {
            throw new WebRPCException(user, e.getMessage());
        } catch (XMLBindException e) {
            throw new WebRPCException(user, e.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        user.setAuthKey(auth);
        return auth;
    }
