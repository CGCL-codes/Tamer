    private void equalsVersion() {
        String line = "";
        try {
            String url_str = JetspeedResources.getString("aipo.version_url", "");
            latest_version = aipo_version = JetspeedResources.getString("aipo.version", "");
            URL url = new URL(url_str);
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bur = new BufferedReader(isr);
            if ((line = bur.readLine()) != null) {
                latest_version = line;
            }
            bur.close();
            isr.close();
            is.close();
        } catch (MalformedURLException e) {
            logger.error("[NewsFormData]", e);
        } catch (UnknownHostException e) {
            logger.error("[NewsFormData]", e);
        } catch (SocketException e) {
            logger.error("[NewsFormData]", e);
        } catch (IOException e) {
            logger.error("[NewsFormData]", e);
        }
    }
