        private void sendCertificate(Certificate certificate) {
            while (true) {
                while (urlListPosition.hasNext()) {
                    URL homeURL = urlListPosition.next();
                    try {
                        URL url = new URL(homeURL, controllerRegistrationWebContext + "/" + serialNumber);
                        log.info("Attempting to connect to " + url);
                        byte[] encodedCertificate = certificate.getEncoded();
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        connection.setAllowUserInteraction(false);
                        connection.setUseCaches(false);
                        connection.setRequestMethod("PUT");
                        connection.setRequestProperty("Content-Type", "application/octet-stream");
                        connection.setRequestProperty("Content-Length", Integer.toString(encodedCertificate.length));
                        connection.setRequestProperty("User-Agent", getUserAgent());
                        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
                        try {
                            out.write(encodedCertificate);
                            out.flush();
                        } finally {
                            out.close();
                        }
                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) return;
                        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                            log.info("You haven't created a user account yet!");
                        }
                    } catch (ClassCastException e) {
                        log.warn("Configuration error: " + homeURL + " is not a HTTP URL.");
                    } catch (MalformedURLException e) {
                        log.warn("Configuration error: " + homeURL + " is not valid URL.");
                    } catch (ConnectException e) {
                        log.debug("Cannot connect to " + homeURL + ": " + e + ". Moving on...");
                    } catch (CertificateEncodingException e) {
                        throw new Error("Can't register due to certificate implementation error: " + e, e);
                    } catch (UnknownServiceException e) {
                        throw new Error("Can't register due to HTTP connection implementation error: " + e, e);
                    } catch (ProtocolException e) {
                        throw new Error("Implementation Error: unknown HTTP method - " + e, e);
                    } catch (IOException e) {
                        log.debug("Failed to send certificate to " + homeURL + ": " + e.toString(), e);
                    }
                }
                try {
                    int seconds = reattemptDelay / 1000;
                    log.info("Failed to connect to controller registration service. " + "Will try again in " + seconds + " seconds...");
                    Thread.sleep(reattemptDelay);
                } catch (InterruptedException ignored) {
                }
                urlListPosition = serviceURLs.iterator();
            }
        }
