    public FileStatus addFile(long vdid, File file) {
        Session session = null;
        Session session_2 = null;
        Long afid = null;
        MessageContext msgc = MessageContext.getCurrentContext();
        UserData ud = UserAuthentication.authenticateUser(msgc);
        VirtualDirectory vd = null;
        try {
            logger.info("User: " + ud.getPrincipal() + " is uploading file " + file.getFilename() + " " + file.getUrn());
            vd = VirtualDirectorySelect.getByIDAndUID(new Long(vdid), ud.getId());
            file.setVdid(vdid);
            file.setUserProfile(ud.getId().longValue());
            if (vd == null) {
                logger.info("Invalid Directory id " + vdid + " file will NOT be added");
                return new FileStatus(file, Status.ADD_FILE_FAILED_INVALID_VDID);
            }
            List listVirDirIDs = VirtualDirectorySelect.getAllByUserId(ud.getId().longValue());
            long nTotalVirDirsSize = UserQuotaHelper.calculateUserVirDirsSize(listVirDirIDs);
            if (nTotalVirDirsSize + file.getFilesize() > PeerserverProperties.getInstance().getUserQuotaValue() * 1000000) {
                logger.info("User " + ud.getPrincipal() + " has reached their file storage quota of " + PeerserverProperties.getInstance().getUserQuotaValue() * 1000000 + "MB");
                return new FileStatus(file, Status.ADD_FILE_FAILED_USER_QUOTA_EXCEEDED);
            }
            List vds = VirtualDirectorySelect.getAllByUserId(ud.getId().longValue());
            for (ListIterator vds_iter = vds.listIterator(); vds_iter.hasNext(); ) {
                VirtualDirectory virdir = (VirtualDirectory) vds_iter.next();
                File exists = FileSelect.getFileByUrnAndVdid(file.getUrn(), virdir.getId().longValue());
                if (exists != null) {
                    logger.info("User is attempting to add a file, that is already on the peerserver");
                    exists.setUrn(null);
                    return new FileStatus(exists, Status.ADD_FILE_FAILED_FILE_ALREADY_EXISTS);
                }
            }
            Message msg = msgc.getRequestMessage();
            Attachments attach = msg.getAttachmentsImpl();
            if (attach == null) {
                logger.info("Add File Failed, no file data attached to message");
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
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream(new java.io.File(storage_dir, filename));
                } catch (Exception exp) {
                    logger.info("Error saving file at path " + storage_dir);
                    fos = new FileOutputStream(new java.io.File(filename));
                }
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
                URN urn = null;
                if (new java.io.File(storage_dir, filename).exists()) {
                    urn = URN.createSHA1Urn(new java.io.File(storage_dir, filename));
                } else if (new java.io.File(filename).exists()) {
                    urn = URN.createSHA1Urn(new java.io.File(filename));
                }
                if (urn != null) {
                    ActualFile af = new ActualFile();
                    if (new java.io.File(storage_dir, filename).exists()) {
                        af.setLocation(new java.io.File(storage_dir, filename).getAbsolutePath());
                    } else if (new java.io.File(filename).exists()) {
                        af.setLocation(new java.io.File(filename).getAbsolutePath());
                    }
                    session = ActualFileSelect.getSession();
                    ActualFileSelect.insert(af, session);
                    ActualFileSelect.closeSession(session);
                    afid = af.getId();
                    logger.info("File uploaded file was save at " + af.getLocation());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ActualFileSelect.closeSession(session);
        }
        session = null;
        try {
            if (afid == null) {
                logger.info("Add file failed, could not insert record into database");
                return new FileStatus(file, Status.ADD_FILE_FAILED_DATABASE_ERROR);
            }
            session = FileSelect.getSession();
            file.setActualfile(afid);
            if (!vd.isPublic_folder()) {
                file.setLocation(new String("http://" + RouterService.getInstance().getIPAddress() + ":" + RouterService.getInstance().getPort() + "/uri-res/N2R?" + file.getUrn() + "&userid=" + vd.getUserID()));
            }
            FileSelect.insert(file, session);
            FileSelect.closeSession(session);
            if (vd.isPublic_folder()) {
                session = FileSelect.getSession();
                file = FileSelect.getById(file.getId().longValue());
                file.setLocation("http://" + RouterService.getInstance().getIPAddress() + ":" + "8080" + "/servlet/PublicFolderBrowser.jsp?userid=" + vd.getUserID() + "&fileid=" + file.getId().toString());
                FileSelect.update(file, session);
                FileSelect.closeSession(session);
            }
        } catch (Exception e) {
            FileSelect.closeSession(session);
            e.printStackTrace();
        }
        RouterService.getInstance().getFileManager().addFileIfShared(file);
        return new FileStatus(file, Status.ADD_FILE_SUCCESSFUL);
    }
