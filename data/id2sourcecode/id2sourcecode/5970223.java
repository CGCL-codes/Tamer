    public void talk(String tts, Object... params) {
        String locale = parameterManager.getAParam(ParameterEnum.locale);
        String textToSpeech = i18nManager.getTraduction(tts);
        if (textToSpeech == null) textToSpeech = tts;
        String googleService = parameterManager.getAParam(ParameterEnum.googleSpeech);
        if (googleService == null) {
            googleService = ParameterEnum.googleSpeech.getDefaut();
        }
        try {
            HttpClient httpclient = new DefaultHttpClient();
            String encodeUrl = URLEncoder.encode(String.format(textToSpeech, params), "UTF-8");
            HttpGet httpget = new HttpGet(String.format(googleService, locale.substring(0, 2)) + encodeUrl);
            httpget.addHeader("User-Agent", "Mozilla/5.0");
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity googleVoiceResponse = response.getEntity();
            Player player = new Player(googleVoiceResponse.getContent());
            player.play();
            player.close();
        } catch (JavaLayerException e) {
            if (jdbManager != null) jdbManager.error(this.getClass(), I18NEnum.exceptionTechnique.name(), e.getCause(), e.getMessage());
        } catch (Exception e) {
            if (jdbManager != null) jdbManager.error(this.getClass(), I18NEnum.exceptionTechnique.name(), e.getCause(), e.getMessage());
        }
    }
