        public void endElement(String uri, String name, String qname) throws SAXException {
            qname = qname.toLowerCase();
            switch(state) {
                case SESSIONS:
                    if (qname.equals("sessions")) state = START; else throw new SAXException("expecting sessions");
                    break;
                case SESSION:
                    if (qname.equals("session")) {
                        current.valid = true;
                        context.addSession(current.sessionId, current);
                        state = SESSIONS;
                    } else throw new SAXException("expecting session");
                    break;
                case PEER:
                    if (qname.equals("peer")) state = SESSION; else throw new SAXException("unexpected element: " + qname);
                    break;
                case PEER_CERTS:
                    if (qname.equals("certificates")) {
                        try {
                            CertificateFactory fact = CertificateFactory.getInstance(certType);
                            current.peerCerts = (Certificate[]) fact.generateCertificates(new ByteArrayInputStream(buf.toString().getBytes())).toArray(new Certificate[0]);
                        } catch (Exception ex) {
                            throw new SAXException(ex);
                        }
                        current.peerVerified = true;
                        state = PEER;
                    } else throw new SAXException("unexpected element: " + qname);
                    break;
                case CERTS:
                    if (qname.equals("certificates")) {
                        try {
                            CertificateFactory fact = CertificateFactory.getInstance(certType);
                            current.localCerts = (Certificate[]) fact.generateCertificates(new ByteArrayInputStream(buf.toString().getBytes())).toArray(new Certificate[0]);
                        } catch (Exception ex) {
                            throw new SAXException(ex);
                        }
                        state = SESSION;
                    } else throw new SAXException("unexpected element: " + qname);
                    break;
                case SECRET:
                    if (qname.equals("secret")) {
                        byte[] encrypted = null;
                        try {
                            encrypted = Base64.decode(buf.toString());
                            if (encrypted.length != 68) throw new IOException("encrypted secret not 68 bytes long");
                            pbekdf.nextBytes(key, 0, key.length);
                            pbekdf.nextBytes(iv, 0, iv.length);
                            pbekdf.nextBytes(mackey, 0, mackey.length);
                            cipher.reset();
                            cipher.init(cipherAttr);
                            mac.init(macAttr);
                        } catch (Exception ex) {
                            throw new SAXException(ex);
                        }
                        mac.update(encrypted, 0, 48);
                        byte[] macValue = mac.digest();
                        for (int i = 0; i < macValue.length; i++) {
                            if (macValue[i] != encrypted[48 + i]) throw new SAXException("MAC mismatch");
                        }
                        current.masterSecret = new byte[48];
                        for (int i = 0; i < current.masterSecret.length; i += 16) {
                            cipher.update(encrypted, i, current.masterSecret, i);
                        }
                        state = SESSION;
                    } else throw new SAXException("unexpected element: " + qname);
                    break;
                default:
                    throw new SAXException("unexpected element: " + qname);
            }
            buf.setLength(0);
        }
