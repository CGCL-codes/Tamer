    private ReleaseInfo getReleaseInfoFromServer() throws RedirectException, HttpException, InvalidXMLException, UnknownMessageException {
        URL url = null;
        try {
            url = new URL(m_sURL);
        } catch (MalformedURLException e) {
            throw new HttpException(e);
        }
        URLConnection con = null;
        InputStream stream = null;
        ReleaseInfo release = null;
        try {
            con = url.openConnection();
            con.connect();
            if (con instanceof HttpURLConnection) {
                int iResponseCode = ((HttpURLConnection) con).getResponseCode();
                if (iResponseCode != 200) {
                    throw new HttpException(m_sURL, iResponseCode);
                }
            }
            stream = con.getInputStream();
            Object resultObject = null;
            try {
                if (m_context == null) {
                    m_context = JAXBContext.newInstance(JAXB_CONTEXT, this.getClass().getClassLoader());
                }
                Unmarshaller u = m_context.createUnmarshaller();
                resultObject = u.unmarshal(stream);
            } catch (JAXBException jaxbEx) {
                throw new InvalidXMLException(jaxbEx);
            }
            if (!(resultObject instanceof ReleaseInfo)) {
                throw new UnknownMessageException();
            }
            release = (ReleaseInfo) resultObject;
        } catch (IOException e) {
            throw new HttpException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            if (con != null && con instanceof HttpURLConnection) {
                ((HttpURLConnection) con).disconnect();
            }
        }
        return release;
    }
