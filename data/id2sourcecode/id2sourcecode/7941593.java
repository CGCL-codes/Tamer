        public void run() {
            while (!isDone()) {
                MailSet set = peek();
                ArrayList<String> host = peekHost();
                if (set != null && host != null) {
                    try {
                        long begin = System.currentTimeMillis();
                        HttpClient client = new HttpClient();
                        client.getHttpConnectionManager().getParams().setConnectionTimeout(LightAttachment.config.getInt("ebigsend.timeout"));
                        LinkedHashMap<String, String> links = new LinkedHashMap<String, String>();
                        boolean failure = false;
                        for (String filename : set.getParts().keySet()) {
                            if (!filename.endsWith("-message")) {
                                String ofilename = set.getParts().get(filename);
                                File targetFile = new File(filename);
                                MessageDigest m = MessageDigest.getInstance("MD5");
                                DigestInputStream dis = new DigestInputStream(new FileInputStream(filename), m);
                                byte[] buf = new byte[4096];
                                while (dis.read(buf) >= 0) {
                                }
                                String CRC = byteToString(m.digest(), "", 0);
                                dis.close();
                                boolean tryAgain = true;
                                int attempt = 1;
                                while (tryAgain && (attempt <= LightAttachment.config.getInt("ebigsend.attempt") || host.size() > 0)) {
                                    if (attempt > 1) Thread.sleep(LightAttachment.config.getInt("ebigsend.attempt-wait"));
                                    try {
                                        attempt++;
                                        boolean already = false;
                                        PostMethod post = new PostMethod(host.get(0));
                                        Part[] data = { new StringPart("CRC", CRC) };
                                        post.setRequestEntity(new MultipartRequestEntity(data, post.getParams()));
                                        int st = client.executeMethod(post);
                                        if (st == HttpStatus.SC_OK) {
                                            String r = post.getResponseBodyAsString();
                                            if (r != null && !r.equals("false") && !r.equals("null")) {
                                                log.info("Attachment '" + ofilename + "' of mail " + set.hashCode() + " was already saved at " + r);
                                                links.put(r, ofilename);
                                                tryAgain = false;
                                                failure = false;
                                                already = true;
                                            }
                                        } else failure = true;
                                        post.releaseConnection();
                                        if (!already) {
                                            if (attempt == 2) log.info("Selecting host " + host.get(0) + " for message " + set.hashCode());
                                            PostMethod filePost = new PostMethod(host.get(0));
                                            Part[] parts = { new FilePart(ofilename, targetFile) };
                                            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
                                            int status = client.executeMethod(filePost);
                                            if (status == HttpStatus.SC_OK) {
                                                String place = filePost.getResponseBodyAsString();
                                                log.info("Attachment '" + ofilename + "' of mail " + set.hashCode() + " successfully saved at " + place + " in " + (attempt - 1) + " attempt(s)");
                                                links.put(place, ofilename);
                                                tryAgain = false;
                                                failure = false;
                                            } else {
                                                log.warn("Attachment '" + ofilename + "' of mail " + set.hashCode() + " couldn't be saved (attempt #" + (attempt - 1) + "): " + HttpStatus.getStatusText(status));
                                                failure = true;
                                                if (attempt > LightAttachment.config.getInt("ebigsend.attempt") && host.size() <= 1) {
                                                    log.error("Mail " + set.hashCode() + " couldn't be saved and will be forwarded");
                                                    SendErrorReportThread sert = new SendErrorReportThread(set, "No e-BigSend server was reachable. The message has been forwarded unchanged.", null);
                                                    sert.start();
                                                    manager.pushToInjectFromFile(set);
                                                    break;
                                                }
                                            }
                                            filePost.releaseConnection();
                                        }
                                    } catch (HttpException e) {
                                        log.warn("Attachment(s) of mail " + set.hashCode() + " couldn't be saved (attempt #" + (attempt - 1) + "): " + e.getMessage(), e);
                                        failure = true;
                                        if (attempt > LightAttachment.config.getInt("ebigsend.attempt") && host.size() <= 1) {
                                            log.error("Mail " + set.hashCode() + " couldn't be saved and will be forwarded");
                                            SendErrorReportThread sert = new SendErrorReportThread(set, "No e-BigSend server was reachable. The message has been forwarded unchanged.", e);
                                            sert.start();
                                            manager.pushToInjectFromFile(set);
                                            break;
                                        }
                                    } catch (IOException e) {
                                        log.warn("Attachment(s) of mail " + set.hashCode() + " couldn't be saved (attempt #" + (attempt - 1) + "): " + e.getMessage(), e);
                                        failure = true;
                                        if (attempt > LightAttachment.config.getInt("ebigsend.attempt") && host.size() <= 1) {
                                            log.error("Mail " + set.hashCode() + " couldn't be saved and will be forwarded");
                                            SendErrorReportThread sert = new SendErrorReportThread(set, "No e-BigSend server was reachable. The message has been forwarded unchanged.", e);
                                            sert.start();
                                            manager.pushToInjectFromFile(set);
                                            break;
                                        }
                                    } catch (Exception e) {
                                        log.warn("Attachment(s) of mail " + set.hashCode() + " couldn't be saved (attempt #" + (attempt - 1) + "): " + e.getMessage(), e);
                                        failure = true;
                                        if (attempt > LightAttachment.config.getInt("ebigsend.attempt") && host.size() <= 1) {
                                            log.error("Mail " + set.hashCode() + " couldn't be saved and will be forwarded");
                                            SendErrorReportThread sert = new SendErrorReportThread(set, "No e-BigSend server was reachable. The message has been forwarded unchanged.", e);
                                            sert.start();
                                            manager.pushToInjectFromFile(set);
                                            break;
                                        }
                                    }
                                    if (attempt > LightAttachment.config.getInt("ebigsend.attempt")) {
                                        host.remove(0);
                                        if (host.size() > 0) attempt = 1;
                                    }
                                }
                            } else {
                                if (!failure) {
                                    StringBuffer data = new StringBuffer();
                                    data.append("Content-Type: text/plain; charset=ISO-8859-1\n");
                                    data.append("Content-Disposition: inline\n");
                                    data.append("Content-Transfer-Encoding: 7bit\n\n");
                                    data.append(LightAttachment.config.getString("ebigsend.head-message") + "\n\n");
                                    for (String key : links.keySet()) {
                                        String lname = key;
                                        String oname = links.get(key);
                                        data.append(LightAttachment.config.getString("ebigsend.att-message").replace("{name}", oname).replace("{address}", lname) + "\n");
                                    }
                                    ByteArrayInputStream inMsgStream = new ByteArrayInputStream(data.toString().getBytes());
                                    MimeMessage msg = set.getMessage();
                                    if (msg.getContent() instanceof MimeMultipart) {
                                        MimeMultipart multi = (MimeMultipart) msg.getContent();
                                        MimeBodyPart bpart = new MimeBodyPart(inMsgStream);
                                        multi.addBodyPart(bpart);
                                        msg.setContent(multi);
                                        msg.saveChanges();
                                    }
                                    manager.pushToInject(set);
                                }
                            }
                        }
                        long end = System.currentTimeMillis();
                        log.info("All attachments of mail " + set.hashCode() + " processed in " + (end - begin) + " ms");
                        while (true) {
                            if (set.isSent()) break;
                            sleep(100);
                        }
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                        e.printStackTrace();
                        SendErrorReportThread sert = new SendErrorReportThread(null, "AttachmentSaver has been stopped.", e);
                        sert.start();
                    } catch (MessagingException e) {
                        log.error(e.getMessage(), e);
                        e.printStackTrace();
                        SendErrorReportThread sert = new SendErrorReportThread(null, "AttachmentSaver has been stopped.", e);
                        sert.start();
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                        e.printStackTrace();
                        SendErrorReportThread sert = new SendErrorReportThread(null, "AttachmentSaver has been stopped.", e);
                        sert.start();
                    } catch (NoSuchAlgorithmException e) {
                        log.error(e.getMessage(), e);
                        e.printStackTrace();
                        SendErrorReportThread sert = new SendErrorReportThread(null, "AttachmentSaver has been stopped.", e);
                        sert.start();
                    }
                    queue.remove(set);
                    hostQueue.remove(host);
                } else {
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                }
            }
        }
