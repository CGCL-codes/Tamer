    public static boolean getPing(URL url) throws ArcImsException {
        try {
            URL urlWithPing = new URL(url.toString() + "?" + GetVariables.COMMAND + "=" + GetVariables.PING);
            HttpURLConnection conn = (HttpURLConnection) urlWithPing.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("GET");
            logger.info("Trying connect to: " + conn.getURL().toString());
            conn.connect();
            Reader isr = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            String line = reader.readLine();
            if ((conn.getResponseCode() == HttpURLConnection.HTTP_OK) && line.startsWith("IMS")) {
                logger.info("Connection succeeded");
                return true;
            } else {
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.warn("Malformed url", e);
            return false;
        } catch (ConnectException ce) {
            logger.error("Timed out error", ce);
            throw new ArcImsException("arcims_server_timeout");
        } catch (NullPointerException npe) {
            logger.error("NullPointerException", npe);
            throw new ArcImsException("arcims_server_error");
        } catch (IOException e) {
            return false;
        }
    }
