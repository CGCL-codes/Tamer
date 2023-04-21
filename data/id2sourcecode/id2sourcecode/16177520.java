    public Blip savePanic(String blipItSvcHost, Blip panic) {
        Blip savedPanic = null;
        HttpEntity httpEntity = null;
        try {
            String panicJson = PanicBlipUtils.toPanicJson(panic);
            String blipItSvcUrl = String.format("http://%s/blipit/panic/blip", blipItSvcHost);
            StringEntity stringEntity = new StringEntity(panicJson, "UTF-8");
            stringEntity.setContentType(JSON_CONTENT_TYPE);
            HttpPost httpPost = new HttpPost(blipItSvcUrl);
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();
            InputStream content = httpEntity.getContent();
            savedPanic = PanicBlipUtils.toPanic(convertStreamToString(content));
        } catch (UnsupportedEncodingException e) {
            logError(e);
        } catch (ClientProtocolException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return savedPanic;
    }
