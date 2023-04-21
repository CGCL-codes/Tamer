    public boolean deletePanic(String blipItSvcHost, Blip panic) {
        boolean result = false;
        HttpEntity httpEntity = null;
        try {
            String panicId = panic.getKey().getId();
            String blipItSvcUrl = String.format("http://%s/blipit/panic/blip/%s", blipItSvcHost, panicId);
            HttpDelete httpDelete = new HttpDelete(blipItSvcUrl);
            HttpResponse httpResponse = httpClient.execute(httpDelete);
            httpEntity = httpResponse.getEntity();
            String response = convertStreamToString(httpEntity.getContent());
            result = response.toLowerCase().contains("success");
        } catch (ClientProtocolException e) {
            logError(e);
        } catch (IOException e) {
            logError(e);
        } finally {
            closeEntity(httpEntity);
        }
        return result;
    }
