    public String post(ArrayList<Map<String, String>> multiHeaders, String serviceName, String serviceDescription, String serviceSlaType, String notificationURI) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter("http.useragent", "sla@soi OCCI Client v0.3 Multipart");
        HttpPost httpPost = new HttpPost("http://" + hostname + ":" + port + serviceResource);
        Header categoryHeader = new BasicHeader("Category", "service; scheme='http://sla-at-soi.eu/ism/service#'; title='" + serviceName + "'");
        Header attributeHeaderNotification = new BasicHeader("Attribute", "eu.slasoi.task.notificationUri='" + notificationURI + "'");
        Header attributeHeaderServiceName = new BasicHeader("Attribute", "eu.slasoi.infrastructure.service.name='" + serviceName + "'");
        Header attributeHeaderServiceDescription = new BasicHeader("Attribute", "eu.slasoi.infrastructure.service.description='" + serviceDescription + "'");
        Header attributeHeaderServiceSlaType = new BasicHeader("Attribute", "eu.slasoi.infrastructure.service.sla='" + serviceSlaType + "'");
        httpPost.addHeader(categoryHeader);
        httpPost.addHeader(attributeHeaderNotification);
        httpPost.addHeader(attributeHeaderServiceName);
        httpPost.addHeader(attributeHeaderServiceDescription);
        httpPost.addHeader(attributeHeaderServiceSlaType);
        httpPost.setEntity(Multipart.createMultipartEntity(multiHeaders));
        String statusLine = null;
        Header[] headersArray = httpPost.getAllHeaders();
        String[] fields = { Response.Location };
        HashMap<String, String> occiHeaders = new HashMap<String, String>();
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            statusLine = httpResponse.getStatusLine().toString();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("----------------------------------------");
            logger.info("StatusLine - (full) - " + httpResponse.getStatusLine());
            logger.info("   StatusCode - " + statusCode);
            logger.info("   Reason - " + httpResponse.getStatusLine().getReasonPhrase());
            logger.info("   Protocol - " + httpResponse.getStatusLine().getProtocolVersion().toString());
            logger.info("----------------------------------------");
            if (StatusCode.validate(statusCode)) {
                logger.info("Response Validated");
            } else {
                logger.error("Response NOT Validated");
            }
            Header[] headers = httpResponse.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Header header = headers[i];
                logger.info("header - response - " + header.toString());
                logger.info("   headerName - " + header.getName());
                logger.info("   headerValue - " + header.getValue());
                for (int h = 0; h < fields.length; h++) {
                    logger.info("   Looking for  - " + fields[h]);
                    if (fields[h].equals(header.getName().toString())) {
                        logger.info("   Found an OCCI Header - " + header.getName());
                        occiHeaders.put(header.getName(), header.getValue());
                    }
                }
            }
        } catch (org.apache.http.conn.HttpHostConnectException e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e);
            return null;
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        logger.info("occiHeaders - " + occiHeaders);
        if (occiHeaders.containsKey(Response.Location)) {
            logger.info("Valid Provision");
            return occiHeaders.get(Response.Location).toString().replaceAll(Response.jobs, "");
        }
        logger.info("NOT a Valid Provision" + statusLine);
        return null;
    }
