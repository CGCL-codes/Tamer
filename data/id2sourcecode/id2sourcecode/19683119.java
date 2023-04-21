    private void callInterFaxSoap(XmlSerializable request, XmlDeserializable response) throws IOException {
        final URL url = new URL(INTERFAX_SOAP_API_URL);
        final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        if (connectTimeout != null) {
            conn.setConnectTimeout(connectTimeout.intValue());
        }
        if (readTimeout != null) {
            conn.setReadTimeout(readTimeout.intValue());
        }
        conn.setRequestProperty("Accept", "application/soap+xml");
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        conn.connect();
        final OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        try {
            writer.write(SOAP_HEADER);
            writer.write(request.toXml());
            writer.write(SOAP_FOOTER);
        } finally {
            writer.close();
        }
        final InputStream is = conn.getInputStream();
        try {
            final Document doc = docBuilder.parse(is);
            response.fromXml(doc.getFirstChild().getFirstChild().getFirstChild());
        } catch (SAXException e) {
            throw new IOException(e);
        } finally {
            is.close();
        }
    }
