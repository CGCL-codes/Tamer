        synchronized X509CRL getCRL(CertificateFactory factory, URI uri) {
            long time = System.currentTimeMillis();
            if (time - lastChecked < CHECK_INTERVAL) {
                if (debug != null) {
                    debug.println("Returning CRL from cache");
                }
                return crl;
            }
            lastChecked = time;
            InputStream in = null;
            try {
                URL url = uri.toURL();
                URLConnection connection = url.openConnection();
                if (lastModified != 0) {
                    connection.setIfModifiedSince(lastModified);
                }
                in = connection.getInputStream();
                long oldLastModified = lastModified;
                lastModified = connection.getLastModified();
                if (oldLastModified != 0) {
                    if (oldLastModified == lastModified) {
                        if (debug != null) {
                            debug.println("Not modified, using cached copy");
                        }
                        return crl;
                    } else if (connection instanceof HttpURLConnection) {
                        HttpURLConnection hconn = (HttpURLConnection) connection;
                        if (hconn.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                            if (debug != null) {
                                debug.println("Not modified, using cached copy");
                            }
                            return crl;
                        }
                    }
                }
                if (debug != null) {
                    debug.println("Downloading new CRL...");
                }
                crl = (X509CRL) factory.generateCRL(in);
                return crl;
            } catch (IOException e) {
                if (debug != null) {
                    debug.println("Exception fetching CRLDP:");
                    e.printStackTrace();
                }
            } catch (CRLException e) {
                if (debug != null) {
                    debug.println("Exception fetching CRLDP:");
                    e.printStackTrace();
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            lastModified = 0;
            crl = null;
            return null;
        }
