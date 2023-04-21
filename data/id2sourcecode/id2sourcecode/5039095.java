    public void enviarTweet(String tweet) {
        try {
            HttpPost post = new HttpPost("http://api.twitter.com/1/statuses/update.xml");
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
                this.enviarMensaje("Error al enviar el Tweet a Twitter");
                return;
            }
            this.enviarMensaje("Enviado tweet a Twitter");
        } catch (UnsupportedEncodingException e) {
            this.enviarMensaje("Error al enviar el Tweet a Twitter");
        } catch (IOException e) {
            this.enviarMensaje("Error al enviar el Tweet a Twitter");
        } catch (OAuthMessageSignerException e) {
            this.enviarMensaje("Error al enviar el Tweet a Twitter");
        } catch (OAuthExpectationFailedException e) {
            this.enviarMensaje("Error al enviar el Tweet a Twitter");
        } catch (OAuthCommunicationException e) {
            this.enviarMensaje("Error al enviar el Tweet a Twitter");
        }
    }
