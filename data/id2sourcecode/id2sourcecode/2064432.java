    public void updateUserPassword(User user, String oldPassword, String newPassword, UserPrompterFactory upf) {
        SPServerInfo serviceInfo = getProjectLocation().getServiceInfo();
        HttpClient client = ClientSideSessionUtils.createHttpClient(serviceInfo, cookieStore);
        MessageDigest digester;
        try {
            digester = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
        try {
            JSONObject begin = new JSONObject();
            begin.put("uuid", JSONObject.NULL);
            begin.put("method", "begin");
            JSONObject persist = new JSONObject();
            persist.put("uuid", user.getUUID());
            persist.put("propertyName", "password");
            persist.put("type", Datatype.STRING.toString());
            if (oldPassword == null) {
                persist.put("method", "persistProperty");
            } else {
                persist.put("method", "changeProperty");
                persist.put("oldValue", new String(Hex.encodeHex(digester.digest(oldPassword.getBytes()))));
            }
            persist.put("newValue", new String(Hex.encodeHex(digester.digest(newPassword.getBytes()))));
            JSONObject commit = new JSONObject();
            commit.put("uuid", JSONObject.NULL);
            commit.put("method", "commit");
            JSONArray transaction = new JSONArray();
            transaction.put(begin);
            transaction.put(persist);
            transaction.put(commit);
            URI serverURI = new URI("http", null, serviceInfo.getServerAddress(), serviceInfo.getPort(), serviceInfo.getPath() + "/" + ClientSideSessionUtils.REST_TAG + "/project/system", "currentRevision=" + getCurrentRevisionNumber(), null);
            HttpPost postRequest = new HttpPost(serverURI);
            postRequest.setEntity(new StringEntity(transaction.toString()));
            postRequest.setHeader("Content-Type", "application/json");
            HttpUriRequest request = postRequest;
            JSONMessage result = client.execute(request, new JSONResponseHandler());
            if (result.getStatusCode() != 200) {
                logger.warn("Failed password change");
                if (result.getStatusCode() == 412) {
                    upf.createUserPrompter("The password you have entered is incorrect.", UserPromptType.MESSAGE, UserPromptOptions.OK, UserPromptResponse.OK, "OK", "OK").promptUser("");
                } else {
                    upf.createUserPrompter("Could not change the password due to the following: " + result.getBody() + " See logs for more details.", UserPromptType.MESSAGE, UserPromptOptions.OK, UserPromptResponse.OK, "OK", "OK").promptUser("");
                }
            } else {
                upf.createUserPrompter("Password successfully changed. Please log into open projects" + " with your new password.", UserPromptType.MESSAGE, UserPromptOptions.OK, UserPromptResponse.OK, "OK", "OK").promptUser("");
            }
        } catch (AccessDeniedException ex) {
            logger.warn("Failed password change", ex);
            upf.createUserPrompter("The password you have entered is incorrect.", UserPromptType.MESSAGE, UserPromptOptions.OK, UserPromptResponse.OK, "OK", "OK").promptUser("");
        } catch (Exception ex) {
            logger.warn("Failed password change", ex);
            upf.createUserPrompter("Could not change the password due to the following: " + ex.getMessage() + " See logs for more details.", UserPromptType.MESSAGE, UserPromptOptions.OK, UserPromptResponse.OK, "OK", "OK").promptUser("");
        }
    }
