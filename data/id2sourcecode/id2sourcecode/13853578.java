            public void validate(String uri) throws UrlFieldEditor.ValidatorException {
                try {
                    URL url = URI.create(uri + "/xmlrpc/").toURL();
                    URLConnection connection = url.openConnection();
                    if (!(connection instanceof HttpURLConnection)) {
                        throw new UrlFieldEditor.ValidatorException(Messages.LodgeItPreferencePage_InvalidURLNotHTTP);
                    }
                    HttpURLConnection httpConnection = (HttpURLConnection) connection;
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        throw new UrlFieldEditor.ValidatorException(String.format(Messages.LodgeItPreferencePage_InvalidServerResponseCode, responseCode));
                    }
                } catch (IllegalArgumentException e) {
                    throw new UrlFieldEditor.ValidatorException(Messages.LodgeItPreferencePage_InvalidURL);
                } catch (MalformedURLException e) {
                    throw new UrlFieldEditor.ValidatorException(Messages.LodgeItPreferencePage_InvalidURL);
                } catch (IOException e) {
                    throw new UrlFieldEditor.ValidatorException(Messages.LodgeItPreferencePage_UnableToConnect);
                }
            }
