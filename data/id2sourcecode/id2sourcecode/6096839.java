    public void validateOAuthRequest(String requestUrl, Map<String, String[]> requestParams, String jsonBody, String rpcServerUrl) throws OAuthException {
        ConsumerData consumerData = consumerDataMap.get(rpcServerUrl);
        if (consumerData == null) {
            throw new IllegalArgumentException("There is no consumer key and secret associated " + "with the given RPC URL " + rpcServerUrl);
        }
        List<OAuth.Parameter> params = new ArrayList<OAuth.Parameter>();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            for (String value : entry.getValue()) {
                params.add(new OAuth.Parameter(entry.getKey(), value));
            }
        }
        OAuthMessage message = new OAuthMessage(POST, requestUrl, params);
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_1);
            byte[] hash = md.digest(jsonBody.getBytes(UTF_8));
            String encodedHash = new String(Base64.encodeBase64(hash, false), UTF_8);
            if (!encodedHash.equals(message.getParameter(OAUTH_BODY_HASH))) {
                throw new IllegalArgumentException("Body hash does not match. Expected: " + encodedHash + ", provided: " + message.getParameter(OAUTH_BODY_HASH));
            }
            OAuthAccessor accessor = consumerData.getAccessor();
            LOG.info("Signature base string: " + OAuthSignatureMethod.getBaseString(message));
            VALIDATOR.validateMessage(message, accessor);
        } catch (NoSuchAlgorithmException e) {
            throw new OAuthException("Error validating OAuth request", e);
        } catch (URISyntaxException e) {
            throw new OAuthException("Error validating OAuth request", e);
        } catch (OAuthException e) {
            throw new OAuthException("Error validating OAuth request", e);
        } catch (IOException e) {
            throw new OAuthException("Error validating OAuth request", e);
        }
    }
