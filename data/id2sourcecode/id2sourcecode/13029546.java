    public void enviarTweet(String tweet) {
        try {
            HttpPost post = new HttpPost("http://twemoi.status.net/api/statuses/update.xml");
            final List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("status", tweet));
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            post.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
            consumer.sign(post);
            HttpClient client = new DefaultHttpClient();
            final HttpResponse response = client.execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            response.getEntity().consumeContent();
            if (statusCode != 200) {
                this.enviarMensaje("Error al enviar el Tweet a Status");
                return;
            }
            this.enviarMensaje("Enviado tweet a Status");
        } catch (UnsupportedEncodingException e) {
            this.enviarMensaje("Error al enviar el Tweet a Status");
        } catch (IOException e) {
            this.enviarMensaje("Error al enviar el Tweet a Status");
        } catch (OAuthMessageSignerException e) {
            this.enviarMensaje("Error al enviar el Tweet a Status");
        } catch (OAuthExpectationFailedException e) {
            this.enviarMensaje("Error al enviar el Tweet a Status");
        } catch (OAuthCommunicationException e) {
            this.enviarMensaje("Error al enviar el Tweet a Status");
        }
    }
