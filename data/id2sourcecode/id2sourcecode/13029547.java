    public ArrayList<Tweet> getTimeLine() {
        try {
            HttpGet get = new HttpGet("http://twemoi.status.net/api/statuses/home_timeline.xml");
            consumer.sign(get);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    this.enviarMensaje("Problema al coger Timeline Status");
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
                StringReader XMLout = new StringReader(sBuf.toString());
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                xmlParserStatus gwh = new xmlParserStatus();
                xr.setContentHandler(gwh);
                xr.parse(new InputSource(XMLout));
                return gwh.getParsedData();
            }
        } catch (UnsupportedEncodingException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        } catch (IOException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        } catch (OAuthMessageSignerException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        } catch (OAuthExpectationFailedException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        } catch (OAuthCommunicationException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        } catch (ParserConfigurationException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        } catch (SAXException e) {
            this.enviarMensaje("Error: No ha sido posible recoger el timeline de Status");
        }
        return null;
    }
