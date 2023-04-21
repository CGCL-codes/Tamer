    private Object makeCall(String sURL, Class<?> expectedResultClass) throws HttpException, ServerException, InvalidXMLException, UnknownMessageException, ThrottledException, UnauthorizedException {
        URL url = null;
        try {
            url = new URL(sURL);
        } catch (MalformedURLException e) {
            throw new HttpException(e);
        }
        HttpURLConnection con = null;
        InputStream stream = null;
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("user-agent", m_sUserAgent);
            String auth = "Basic " + (new sun.misc.BASE64Encoder()).encode((m_sUserName + ":" + m_sPassword).getBytes());
            con.setRequestProperty("Authorization", auth);
            waitBetweenCalls();
            con.connect();
            m_lLastCallTime = new java.util.Date().getTime();
            int iResponseCode = con.getResponseCode();
            if (iResponseCode != 200) {
                if (iResponseCode == THROTTLED_CODE && !m_bRetry) {
                    try {
                        Thread.sleep(m_iDelayTime);
                    } catch (InterruptedException e) {
                    }
                    ;
                    m_iDelayTime = m_iDelayTime + 500;
                    m_bRetry = true;
                    makeCall(sURL, expectedResultClass);
                } else if (iResponseCode == THROTTLED_CODE) {
                    throw new ThrottledException();
                } else if (iResponseCode == UNAUTHORIZED_CODE) {
                    throw new UnauthorizedException();
                } else {
                    m_bRetry = false;
                    throw new HttpException(sURL, iResponseCode);
                }
            }
            m_bRetry = false;
            stream = con.getInputStream();
            Object resultObject = null;
            try {
                if (m_context == null) {
                    m_context = JAXBContext.newInstance(JAXB_CONTEXT, this.getClass().getClassLoader());
                }
                Unmarshaller u = m_context.createUnmarshaller();
                u.setEventHandler(new ValidationEventHandler() {

                    public boolean handleEvent(ValidationEvent event) {
                        boolean bContinue;
                        if (event.getSeverity() == ValidationEvent.FATAL_ERROR) {
                            bContinue = false;
                            m_log.warn("Fatal unmarshal error: " + event.getMessage());
                        } else {
                            bContinue = true;
                            m_log.warn("Unmarshal error: " + event.getMessage());
                        }
                        return bContinue;
                    }
                });
                resultObject = u.unmarshal(stream);
            } catch (JAXBException jaxbEx) {
                throw new InvalidXMLException(jaxbEx);
            }
            checkResult(resultObject);
            if (!expectedResultClass.isInstance(resultObject)) {
                throw new UnknownMessageException();
            }
            return resultObject;
        } catch (IOException e) {
            throw new HttpException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                }
            }
            if (con != null) {
                con.disconnect();
            }
        }
    }
