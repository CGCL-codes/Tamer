    public String getUser() {
        try {
            HttpGet get = new HttpGet("http://api.twitter.com/1/account/verify_credentials.xml");
            consumer.sign(get);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(get);
            if (response != null) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    this.enviarMensaje("Problema al coger usuario Twitter");
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
                String salida = sBuf.toString();
                String user_name = salida.split("</screen_name>")[0].split("<screen_name>")[1];
                return user_name;
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
