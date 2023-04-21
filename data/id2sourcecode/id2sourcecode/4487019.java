    public void updateSync(User user) {
        String rpc;
        if (user.getAuthKey() != null) {
            rpc = user.getProject().getBaseURL() + "show_user.php?format=xml&auth=%s";
            rpc = String.format(rpc, user.getAuthKey());
        } else {
            rpc = user.getProject().getBaseURL() + "show_user.php?format=xml&userid=%d";
            rpc = String.format(rpc, user.getBOINCId());
        }
        InputStream stream = null;
        try {
            URL url = new URL(rpc);
            URLConnection urlConnection = url.openConnection();
            stream = urlConnection.getInputStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(stream);
            Element root = document.getDocumentElement();
            assertXMLDocumentStatus(user, root);
            DOMFeeder userFeeder = new DOMFeeder(root);
            userFeeder.inject(user);
            userFeeder.inject(user.getSnapshot());
            user.getSnapshot().setTimeStamp(System.currentTimeMillis() / 1000);
            List<Element> hostElements = DOMUtils.getChildren(root, "host");
            List<Host> hosts = new ArrayList<Host>();
            DOMFeeder feeder = new DOMFeeder(hostElements);
            while (feeder.next()) {
                Host host = new Host();
                feeder.inject(host);
                hosts.add(host);
            }
            user.setHosts(hosts);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (SAXException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (ParserConfigurationException e) {
            Log.e(TAG, e.getMessage(), e);
            throw new WebRPCException(user, e.getMessage());
        } catch (DOMException e) {
            Log.e(TAG, "For user on project " + user.getProject(), e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
    }
