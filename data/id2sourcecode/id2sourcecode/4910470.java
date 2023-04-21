    public static boolean testConnection(String vendor, String urlstring, String username, String password) {
        boolean success = false;
        try {
            if (vendor.equals("Activiti")) {
                urlstring += "/login";
            }
            URL url = new URL(urlstring);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-type", "application/json");
            String data = "{\"userId\":\"" + username + "\",\"password\":\"" + password + "\"}";
            conn.getOutputStream().write(data.getBytes());
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String tempLine = rd.readLine();
                StringBuffer temp = new StringBuffer();
                while (tempLine != null) {
                    temp.append(tempLine);
                    tempLine = rd.readLine();
                }
                rd.close();
                is.close();
                success = new JSONObject(new JSONTokener(temp.toString())).optBoolean("success");
            } else {
                String message = "";
                if (conn.getResponseCode() == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    message = mxResources.get("checkNameAndPassword");
                } else if (conn.getResponseCode() == HttpURLConnection.HTTP_BAD_METHOD) {
                    message = mxResources.get("checkBaseURL");
                } else {
                    message = mxResources.get("checkServer");
                }
                JOptionPane.showMessageDialog(null, message, mxResources.get("connectionFailed"), JOptionPane.ERROR_MESSAGE);
            }
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ConnectException e) {
            JOptionPane.showMessageDialog(null, e.getMessage() + "\n" + mxResources.get("checkServer"), mxResources.get("connectionFailed"), JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return success;
    }
