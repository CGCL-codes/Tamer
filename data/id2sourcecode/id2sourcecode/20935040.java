    public void handleMessage(javax.mail.Message orgMsg, String from, String to, String subject, java.util.Date sentDate, javax.mail.Part txtPart, boolean txtPartSwitchRowPresent, javax.mail.Part imgPart) {
        try {
            MP_USER userMatch = new MP_USER();
            userMatch.setPostEmail(from);
            Vector userMatches = userMatch.selectWhere(db.getConnection(), true);
            if (userMatches.size() == 0) {
                log.log(Level.SEVERE, "No user found with postEmail '" + from + "'");
            } else {
                MP_USER mpUser = (MP_USER) userMatches.get(0);
                String disKey = getDispatchKey();
                MP_POST_CHANNEL mpChannel = new MP_POST_CHANNEL();
                mpChannel.setUserId(mpUser.getUserId());
                String channelName = mpUser.getDefaultChannel();
                String txtc = "";
                if (txtPart != null) {
                    txtc = (String) txtPart.getContent();
                    if (txtPartSwitchRowPresent) {
                        txtc = Utils.removeFirstTxtLine(txtc);
                    }
                    String fl = Utils.getFirstTxtLine(txtc);
                    if (fl.startsWith(disKey)) {
                        channelName = fl.substring(disKey.length());
                        txtc = Utils.removeFirstTxtLine(txtc);
                    }
                }
                mpChannel.setChannelName(channelName);
                if (!mpChannel.dbSelect(db.getConnection())) {
                    log.log(Level.SEVERE, "Could not find the default channel '" + mpUser.getDefaultChannel() + "' for user '" + mpUser.getUserId() + "'");
                } else {
                    MP_POST mpPost = new MP_POST();
                    mpPost.setUserId(mpUser.getUserId());
                    mpPost.setChannelName(mpChannel.getChannelName());
                    mpPost.setPostEmail(from);
                    mpPost.setPostDate(new java.sql.Timestamp(sentDate.getTime()));
                    mpPost.setTitle(subject);
                    mpPost.setDescription(txtc);
                    mpPost.setImageMimeType(Utils.getMimeType(imgPart));
                    mpPost.dbInsert(db.getConnection());
                    String imgFilename = null;
                    if (imgPart != null) {
                        Preferences mailProps = PrefsMgr.getInstance().getPreferences();
                        String imgDir = mailProps.get(WMLBLOG_IMAGE_PATH, null);
                        String disp = imgPart.getDisposition();
                        String ext = ".jpg";
                        if (imgPart.isMimeType("application/octet-stream")) {
                            String ifn = imgPart.getFileName();
                            if (ifn != null) {
                                ifn = ifn.toLowerCase();
                                if (ifn.endsWith(".gif")) {
                                    ext = ".gif";
                                }
                            }
                        } else if (imgPart.isMimeType("image/gif")) {
                            ext = ".gif";
                        }
                        String iDir = imgDir + "/" + mpUser.getUserId().replace(' ', '_') + "/" + mpChannel.getChannelName().replace(' ', '_');
                        File iDirFile = new File(iDir);
                        iDirFile.mkdirs();
                        imgFilename = iDir + "/p" + Publisher.file_dateFormat.format(sentDate) + ext;
                        log.log(Level.INFO, "Saving attachment to file " + imgFilename);
                        try {
                            File f = new File(imgFilename);
                            OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
                            InputStream is = imgPart.getInputStream();
                            int c;
                            while ((c = is.read()) != -1) {
                                os.write(c);
                            }
                            os.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    if (imgFilename != null) {
                        log.log(Level.INFO, "Upload Image");
                        publisher.uploadImage(mpChannel, mpPost, imgFilename);
                    }
                    log.log(Level.INFO, "Upload HTML Blog");
                    publisher.uploadHTMLandWML(mpUser, mpChannel);
                    log.log(Level.INFO, "Post Done!");
                }
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception during handleMessage()", e);
        }
    }
