    public void fetchCRL(String uri) {
        Object fing;
        X509CRL old_crl = null;
        boolean im_fetching = false;
        synchronized (this) {
            fing = being_fetched.get(uri);
            if (fing == null) {
                fing = new Object();
                being_fetched.put(uri, fing);
                old_crl = (X509CRL) crls.put(uri, null);
                im_fetching = true;
            }
        }
        if (im_fetching) {
            logger.chat("Fetching up to date CRL from <" + uri + ">");
            X509CRL new_crl = null;
            boolean ok = false;
            String problem = "none";
            try {
                URL url = new URL(uri);
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                logger.chat("<" + url.toString() + "> uses protocol " + url.getProtocol());
                InputStream is = url.openStream();
                DataInputStream dis = new DataInputStream(is);
                BufferedInputStream bis = new BufferedInputStream(dis);
                byte[] readcrldata = new byte[100 * 1024];
                int in;
                int i;
                for (i = 0; (in = bis.read()) != -1; i++) readcrldata[i] = (byte) in;
                bis.close();
                logger.chat("Bytes read: " + i);
                byte[] crldata = new byte[i];
                int j = 0;
                for (j = 0; j < i; j++) {
                    crldata[j] = readcrldata[j];
                }
                String crlpem = new String(crldata);
                if (crlpem.indexOf("-----BEGIN X509 CRL") != -1) {
                    new_crl = readCRLFromPEM(crldata, cf);
                } else {
                    new_crl = readCRLFromDER(crldata, cf);
                }
                if (new_crl == null) {
                    problem = "CRL could not be parsed (probably no DER or PEM format)";
                } else {
                    Enumeration e = ListenerManager.getGKS().aliases();
                    while (e.hasMoreElements()) {
                        Certificate c = ListenerManager.getGKS().getCertificate((String) e.nextElement());
                        if (c != null) {
                            try {
                                new_crl.verify(c.getPublicKey());
                                ok = true;
                                break;
                            } catch (Exception ex) {
                            }
                        }
                    }
                    if (!ok) {
                        problem = "The updated CRL was not created by any of the allowed CAs";
                    }
                }
            } catch (java.net.MalformedURLException mfuex) {
                problem = "Unknown protocol to access CRL";
            } catch (CertificateException cex) {
                problem = "CRLs Could not get the X509 CertificateFactory (check application CLASSPATH): " + cex;
            } catch (CRLException crlex) {
                problem = "The address does not return a recognised CRL: " + crlex;
            } catch (java.io.IOException ioex) {
                problem = "Problems updating crl: " + ioex;
            } catch (java.security.KeyStoreException ksex) {
                problem = "Problems getting public keys for allowed CAs (in CRL fetching): " + ksex;
            } finally {
                if (ok) {
                    crls.put(uri, new_crl);
                    Date now = new Date();
                    Date next_update = new_crl.getNextUpdate();
                    if (next_update == null || next_update.before(now)) {
                        next_update = new Date(now.getTime() + Configuration.defaultCRLValidity());
                    }
                    Alarm.get().set(new Waiter(uri, this), next_update.getTime());
                    logger.info("Updated CRL from <" + uri + "> next update = " + next_update);
                } else {
                    Date update_of_old = null;
                    if (old_crl != null) update_of_old = old_crl.getNextUpdate();
                    if (update_of_old != null && Configuration.getCRLGrace() > 0.0) {
                        long valid_time = update_of_old.getTime() - old_crl.getThisUpdate().getTime();
                        if (valid_time < 0) {
                            valid_time = 1;
                        }
                        long grace_so_far = System.currentTimeMillis() - update_of_old.getTime();
                        if ((((double) grace_so_far) / ((double) valid_time)) < Configuration.getCRLGrace()) {
                            crls.put(uri, old_crl);
                            long next_update = System.currentTimeMillis() + (long) (Configuration.getCRLGrace() * (valid_time / 10.0));
                            Alarm.get().set(new Waiter(uri, this), next_update);
                            logger.warning("Updating CRL from <" + uri + "> failed because: " + problem + "\nWithin CRL grace period so keeping previous until: " + new Date(next_update));
                        } else {
                            logger.severe("Updating CRL from <" + uri + "> failed because: " + problem + "\nOut of grace period so there is no valid CRL for this CA.");
                        }
                    } else {
                        logger.severe("Updating CRL from <" + uri + "> failed because: " + problem + "\nOut of grace period or no previously valid CRL, so there is no valid CRL for this CA.");
                    }
                }
                synchronized (this) {
                    being_fetched.remove(uri);
                }
                synchronized (fing) {
                    fing.notifyAll();
                }
            }
        } else {
            synchronized (fing) {
                try {
                    if (logger.CHAT) logger.log("Waiting for CRL update to complete on <" + uri + ">", LoggerLevel.CHAT);
                    fing.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
    }
