    private <T> T executeMethod(Class<T> cls, HttpRequestBase httpReq) {
        log.debug("executing {} {}, converting response to {}", new Object[] { httpReq.getMethod(), httpReq.getURI(), cls });
        InputStream is = null;
        try {
            HttpResponse resp = httpClient.execute(httpReq, context);
            int statusCode = resp.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode > 299) {
                log.error("{} executing {} {}", new Object[] { statusCode, httpReq.getMethod(), httpReq.getURI() });
                throw new TwitterException("Twitter answered with " + statusCode);
            }
            HttpEntity entity = resp.getEntity();
            if (entity != null) {
                is = entity.getContent();
                T result = JSONParser.defaultJSONParser().parse(cls, new InputStreamSource(is, false));
                log.debug("result = {}", result);
                return result;
            }
        } catch (TwitterException e) {
            httpReq.abort();
            throw e;
        } catch (Exception e) {
            log.error("error executing {} {}: {}", new Object[] { httpReq.getMethod(), httpReq.getURI(), e });
            httpReq.abort();
            throw new TwitterException("error on " + httpReq.getMethod() + " " + httpReq.getURI(), e);
        } finally {
            if (is != null) {
                IOUtils.closeQuietly(is);
            }
        }
        return null;
    }
