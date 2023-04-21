    public String getCountryCode(final String ip) throws GeoLocatingException {
        if (ip == null) return null;
        if (!WebCastellumFilter.PATTERN_VALID_CLIENT_ADDRESS.matcher(ip).matches()) return null;
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            final java.net.URL url = new java.net.URL(MessageFormat.format(this.servicePattern, new Object[] { ip }));
            HttpURLConnection.setFollowRedirects(true);
            connection = (HttpURLConnection) url.openConnection(this.proxy);
            connection.setInstanceFollowRedirects(true);
            connection.setConnectTimeout(this.connectTimeout);
            connection.setReadTimeout(this.readTimeout);
            connection.connect();
            final int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) throw new GeoLocatingException("Non-OK status code returned from geo-locating site: " + status);
            input = connection.getInputStream();
            final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
            final NodeList countryAbbrevs = document.getElementsByTagName(this.xmlElementName);
            if (countryAbbrevs.getLength() == 0) throw new GeoLocatingException("No country containing XML received from geo-locating site");
            final String result = countryAbbrevs.item(0).getTextContent();
            if (DEBUG) System.out.println("DefaultGeoLocator: " + result);
            if ("XX".equalsIgnoreCase(result)) return null;
            return result;
        } catch (MalformedURLException e) {
            throw new GeoLocatingException("Malformed URL", e);
        } catch (SocketTimeoutException e) {
            throw new GeoLocatingException("Socket timeout", e);
        } catch (IOException e) {
            throw new GeoLocatingException("I/O failure", e);
        } catch (ParserConfigurationException e) {
            throw new GeoLocatingException("XML parser configuration failure", e);
        } catch (SAXException e) {
            throw new GeoLocatingException("XML parser (SAX) failure", e);
        } catch (RuntimeException e) {
            throw new GeoLocatingException("Unexpected runtime failure", e);
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException ignored) {
            }
            if (connection != null) try {
                connection.disconnect();
            } catch (RuntimeException ignored) {
            }
        }
    }
