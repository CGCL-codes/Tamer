    private Result call(String method, String apiKey, Map<String, String> params, Session session) {
        params = new HashMap<String, String>(params);
        InputStream inputStream = null;
        String cacheEntryName = Cache.createCacheEntryName(method, params);
        if (session == null && cache != null) {
            if (cache.contains(cacheEntryName) && !cache.isExpired(cacheEntryName)) {
                inputStream = cache.load(cacheEntryName);
            }
        }
        if (inputStream == null) {
            params.put(PARAM_API_KEY, apiKey);
            if (session != null) {
                params.put("sk", session.getKey());
                String sig = Authenticator.createSignature(method, params, session.getSecret());
                params.put("api_sig", sig);
            }
            try {
                HttpURLConnection urlConnection = openConnection(apiRootUrl);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                OutputStream outputStream = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                String post = buildParameterQueue(method, params);
                if (debugMode) {
                    System.out.println("body: " + post);
                }
                writer.write(post);
                writer.close();
                int responseCode = urlConnection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_FORBIDDEN || responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getErrorStream();
                } else if (responseCode != HttpURLConnection.HTTP_OK) {
                    this.lastResult = Result.createHttpErrorResult(responseCode, urlConnection.getResponseMessage());
                    return lastResult;
                } else {
                    inputStream = urlConnection.getInputStream();
                    if (cache != null) {
                        long expires = urlConnection.getHeaderFieldDate("Expires", -1);
                        if (expires == -1) {
                            long expirationTime = cache.getExpirationPolicy().getExpirationTime(method, params);
                            if (expirationTime > 0) {
                                if (expirationTime == Long.MAX_VALUE) {
                                    expires = Long.MAX_VALUE;
                                } else {
                                    expires = System.currentTimeMillis() + expirationTime;
                                }
                            }
                        }
                        if (expires != -1) {
                            cache.store(cacheEntryName, inputStream, expires);
                            inputStream = cache.load(cacheEntryName);
                            if (inputStream == null) throw new CallException("caching failed.");
                        }
                    }
                }
            } catch (IOException e) {
                throw new CallException(e);
            }
        }
        try {
            Document document = newDocumentBuilder().parse(new InputSource(new InputStreamReader(inputStream, "UTF-8")));
            Element root = document.getDocumentElement();
            String statusString = root.getAttribute("status");
            Status status = "ok".equals(statusString) ? Status.OK : Status.FAILED;
            if (status == Status.FAILED) {
                if (cache != null) cache.remove(cacheEntryName);
                Element error = (Element) root.getElementsByTagName("error").item(0);
                int errorCode = Integer.parseInt(error.getAttribute("code"));
                String message = error.getTextContent();
                if (debugMode) System.err.printf("Failed. Code: %d, Error: %s%n", errorCode, message);
                this.lastResult = Result.createRestErrorResult(errorCode, message);
            } else {
                this.lastResult = Result.createOkResult(document);
            }
            return this.lastResult;
        } catch (IOException e) {
            throw new CallException(e);
        } catch (SAXException e) {
            throw new CallException(e);
        }
    }
