    void publishToRemoteServer(Document annodoc) {
        OutputStream outpstr = null;
        URL remURL;
        try {
            remURL = new URL(r_url);
            HttpURLConnection url_con = (HttpURLConnection) remURL.openConnection();
            url_con.setRequestMethod("POST");
            if (useAuthorization()) {
                System.out.println("Using authorization to Post");
                url_con.setRequestProperty("Authorization", "Basic " + getBasicAuthorizationString());
            }
            url_con.setRequestProperty("Content-Type", "application/xml");
            url_con.setRequestProperty("charset", "utf-8");
            url_con.setDoInput(true);
            url_con.setDoOutput(true);
            outpstr = url_con.getOutputStream();
            url_con.connect();
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(annodoc), new StreamResult(outpstr));
            outpstr.flush();
            outpstr.close();
            System.out.println(url_con.getResponseMessage());
            url_con.disconnect();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
