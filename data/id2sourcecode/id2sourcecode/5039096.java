    public ArrayList<Tweet> getTimeLine() {
        try {
            HttpGet get = new HttpGet("http://api.twitter.com/1/statuses/home_timeline.xml");
            consumer.sign(get);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    this.enviarMensaje("Problema al coger Timeline Twitter");
                    return null;
                }
                StringBuffer sBuf = new StringBuffer();
                String linea;
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                while ((linea = reader.readLine()) != null) {
                    sBuf.append(linea);
                }
                reader.close();
                response.getEntity().consumeContent();
                get.abort();
                SAXParserFactory spf = SAXParserFactory.newInstance();
                try {
                    StringReader XMLout = new StringReader(sBuf.toString());
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    xmlParserTwitter gwh = new xmlParserTwitter();
                    xr.setContentHandler(gwh);
                    xr.parse(new InputSource(XMLout));
                    return gwh.getParsedData();
                } catch (ParserConfigurationException e) {
                    this.enviarMensaje("Problema al coger Timeline Twitter");
                } catch (SAXException e) {
                    this.enviarMensaje("Problema al coger Timeline Twitter");
                } catch (IOException e) {
                    this.enviarMensaje("Problema al coger Timeline Twitter");
                }
            }
        } catch (UnsupportedEncodingException e) {
            this.enviarMensaje("Problema al coger Timeline Twitter");
        } catch (IOException e) {
            this.enviarMensaje("Problema al coger Timeline Twitter");
        } catch (OAuthMessageSignerException e) {
            this.enviarMensaje("Problema al coger Timeline Twitter");
        } catch (OAuthExpectationFailedException e) {
            this.enviarMensaje("Problema al coger Timeline Twitter");
        } catch (OAuthCommunicationException e) {
            this.enviarMensaje("Problema al coger Timeline Twitter");
        }
        return null;
    }
