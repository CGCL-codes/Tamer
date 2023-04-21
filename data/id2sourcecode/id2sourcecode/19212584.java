    public FileStatus updateFile(long vdid, File file) {
        Session session = null;
        Session session_2 = null;
        Long afid = null;
        MessageContext msgc = MessageContext.getCurrentContext();
        UserData ud = UserAuthentication.authenticateUser(msgc);
        VirtualDirectory vd = null;
        try {
            vd = VirtualDirectorySelect.getByIDAndUID(new Long(vdid), ud.getId());
            file.setVdid(vdid);
            file.setUserProfile(ud.getId().longValue());
            logger.info("User " + ud.getId() + " is updating file " + file.getFilename());
            if (vd == null) {
                logger.info("Could no update file, invalid directory id " + vdid);
                return new FileStatus(file, Status.ADD_FILE_FAILED_INVALID_VDID);
            }
            List listVirDirIDs = VirtualDirectorySelect.getAllByUserId(ud.getId().longValue());
            long nTotalVirDirsSize = UserQuotaHelper.calculateUserVirDirsSize(listVirDirIDs);
            if (nTotalVirDirsSize + file.getFilesize() > PeerserverProperties.getInstance().getUserQuotaValue() * 1000000) {
                logger.info("Adding this file would go over your alloted " + "total file size quota.");
                return new FileStatus(file, Status.ADD_FILE_FAILED_USER_QUOTA_EXCEEDED);
            }
            File exists = FileSelect.getFileByUrn(file.getUrn());
            if (exists != null) {
                logger.info("User is attempting to update to a file that already exists on peerserver");
                exists.setUrn(null);
                return new FileStatus(exists, Status.ADD_FILE_FAILED_FILE_ALREADY_EXISTS);
            }
            Message msg = msgc.getRequestMessage();
            Attachments attach = msg.getAttachmentsImpl();
            if (attach == null) {
                logger.info("No file data was attache to file update message");
                return new FileStatus(file, Status.ADD_FILE_FAILED_NO_ATTACHEMENT);
            }
            Iterator iter = attach.getAttachments().iterator();
            if (iter.hasNext()) {
                AttachmentPart part = (AttachmentPart) iter.next();
                DataHandler dh = part.getDataHandler();
                String username = ud.getPrincipal();
                username = username.trim();
                java.io.File storage_dir = getFileStorageDirectory(username);
                storage_dir.mkdirs();
                String filename = file.getFilename();
                int index = 1;
                while (new java.io.File(storage_dir, filename).exists()) {
                    int idx = file.getFilename().lastIndexOf(".");
                    if (idx != -1) {
                        String one = file.getFilename().substring(0, idx);
                        String two = file.getFilename().substring(idx + 1, file.getFilename().length());
                        filename = one + "-" + index + "." + two;
                    } else {
                        filename = file.getFilename() + "_" + index;
                    }
                }
                FileOutputStream fos = new FileOutputStream(new java.io.File(storage_dir, filename));
                byte[] bytes = new byte[4096];
                InputStream in_stream = dh.getInputStream();
                int read = in_stream.read(bytes);
                while (read != -1) {
                    fos.write(bytes, 0, read);
                    read = in_stream.read(bytes);
                }
                fos.flush();
                fos.close();
                in_stream.close();
                URN urn = URN.createSHA1Urn(new java.io.File(storage_dir, filename));
                ActualFile af = new ActualFile();
                af.setLocation(new java.io.File(storage_dir, filename).getAbsolutePath());
                session = ActualFileSelect.getSession();
                ActualFileSelect.insert(af, session);
                afid = af.getId();
                ActualFileSelect.closeSession(session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ActualFileSelect.closeSession(session);
        }
        try {
            if (afid == null) {
                return new FileStatus(file, Status.ADD_FILE_FAILED_DATABASE_ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (file.getActualfile() != null) {
            ActualFile acfile = ActualFileSelect.getByID(file.getActualfile().longValue());
            try {
                Session af_session = ActualFileSelect.getSession();
                ActualFileSelect.delete(acfile, af_session);
                ActualFileSelect.closeSession(af_session);
            } catch (Exception e2) {
                logger.error("", e2);
            }
        }
        try {
            Session file_session = FileSelect.getSession();
            file.setActualfile(afid);
            FileSelect.update(file, file_session);
            FileSelect.closeSession(file_session);
        } catch (Exception e3) {
            logger.error("", e3);
        }
        return new FileStatus(file, Status.ADD_FILE_SUCCESSFUL);
    }
