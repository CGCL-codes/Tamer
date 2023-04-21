    @Override
    protected void service(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        try {
            httpServletRequest.setCharacterEncoding("UTF-8");
            log.debug("\nquery = " + httpServletRequest.getQueryString());
            log.debug("\n\nfileName = " + httpServletRequest.getParameter("fileName"));
            log.debug("\n\nparentPath = " + httpServletRequest.getParameter("parentPath"));
            String queryString = httpServletRequest.getQueryString();
            if (queryString == null) {
                queryString = "";
            }
            String sid = httpServletRequest.getParameter("sid");
            if (sid == null) {
                sid = "default";
            }
            log.debug("sid: " + sid);
            Long users_id = Sessionmanagement.getInstance().checkSession(sid);
            Long user_level = Usermanagement.getInstance().getUserLevelByID(users_id);
            if (user_level != null && user_level > 0) {
                String room_id = httpServletRequest.getParameter("room_id");
                if (room_id == null) {
                    room_id = "default";
                }
                String moduleName = httpServletRequest.getParameter("moduleName");
                if (moduleName == null) {
                    moduleName = "nomodule";
                }
                String parentPath = httpServletRequest.getParameter("parentPath");
                if (parentPath == null) {
                    parentPath = "nomodule";
                }
                String requestedFile = httpServletRequest.getParameter("fileName");
                if (requestedFile == null) {
                    requestedFile = "";
                }
                String roomName = room_id;
                roomName = StringUtils.deleteWhitespace(roomName);
                String current_dir = getServletContext().getRealPath("/");
                String working_dir = "";
                working_dir = current_dir + "upload" + File.separatorChar;
                if (moduleName.equals("lzRecorderApp")) {
                    working_dir = current_dir + "streams" + File.separatorChar + "hibernate" + File.separatorChar;
                } else if (moduleName.equals("videoconf1")) {
                    if (parentPath.length() != 0) {
                        if (parentPath.equals("/")) {
                            working_dir = working_dir + roomName + File.separatorChar;
                        } else {
                            working_dir = working_dir + roomName + File.separatorChar + parentPath + File.separatorChar;
                        }
                    } else {
                        working_dir = current_dir + roomName + File.separatorChar;
                    }
                } else if (moduleName.equals("userprofile")) {
                    working_dir += "profiles" + File.separatorChar;
                    File f = new File(working_dir);
                    if (!f.exists()) {
                        boolean c = f.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                    working_dir += "profile_" + users_id + File.separatorChar;
                    File f2 = new File(working_dir);
                    if (!f2.exists()) {
                        boolean c = f2.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                } else if (moduleName.equals("remoteuserprofile")) {
                    working_dir += "profiles" + File.separatorChar;
                    File f = new File(working_dir);
                    if (!f.exists()) {
                        boolean c = f.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                    String remoteUser_id = httpServletRequest.getParameter("remoteUserid");
                    if (remoteUser_id == null) {
                        remoteUser_id = "0";
                    }
                    working_dir += "profile_" + remoteUser_id + File.separatorChar;
                    File f2 = new File(working_dir);
                    if (!f2.exists()) {
                        boolean c = f2.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                } else if (moduleName.equals("remoteuserprofilebig")) {
                    working_dir += "profiles" + File.separatorChar;
                    File f = new File(working_dir);
                    if (!f.exists()) {
                        boolean c = f.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                    String remoteUser_id = httpServletRequest.getParameter("remoteUserid");
                    if (remoteUser_id == null) {
                        remoteUser_id = "0";
                    }
                    working_dir += "profile_" + remoteUser_id + File.separatorChar;
                    File f2 = new File(working_dir);
                    if (!f2.exists()) {
                        boolean c = f2.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                    requestedFile = this.getBigProfileUserName(working_dir);
                } else if (moduleName.equals("chat")) {
                    working_dir += "profiles" + File.separatorChar;
                    File f = new File(working_dir);
                    if (!f.exists()) {
                        boolean c = f.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                    String remoteUser_id = httpServletRequest.getParameter("remoteUserid");
                    if (remoteUser_id == null) {
                        remoteUser_id = "0";
                    }
                    working_dir += "profile_" + remoteUser_id + File.separatorChar;
                    File f2 = new File(working_dir);
                    if (!f2.exists()) {
                        boolean c = f2.mkdir();
                        if (!c) {
                            log.error("cannot write to directory");
                        }
                    }
                    requestedFile = this.getChatUserName(working_dir);
                } else {
                    working_dir = working_dir + roomName + File.separatorChar;
                }
                if (!moduleName.equals("nomodule")) {
                    log.debug("requestedFile: " + requestedFile + " current_dir: " + working_dir);
                    String full_path = working_dir + requestedFile;
                    File f = new File(full_path);
                    if (!f.exists() || !f.canRead()) {
                        if (!f.canRead()) {
                            log.debug("LOG DownloadHandler: The request file is not readable");
                        } else {
                            log.debug("LOG DownloadHandler: The request file does not exist / has already been deleted");
                        }
                        log.debug("LOG ERROR requestedFile: " + requestedFile);
                        if (requestedFile.endsWith(".jpg")) {
                            log.debug("LOG endsWith d.jpg");
                            log.debug("LOG moduleName: " + moduleName);
                            requestedFile = DownloadHandler.defaultImageName;
                            if (moduleName.equals("remoteuserprofile")) {
                                requestedFile = DownloadHandler.defaultProfileImageName;
                            } else if (moduleName.equals("remoteuserprofilebig")) {
                                requestedFile = DownloadHandler.defaultProfileImageNameBig;
                            } else if (moduleName.equals("userprofile")) {
                                requestedFile = DownloadHandler.defaultProfileImageName;
                            } else if (moduleName.equals("chat")) {
                                requestedFile = DownloadHandler.defaultChatImageName;
                            }
                            full_path = current_dir + File.separatorChar + "default" + File.separatorChar + requestedFile;
                        } else if (requestedFile.endsWith(".swf")) {
                            requestedFile = DownloadHandler.defaultSWFName;
                            full_path = current_dir + File.separatorChar + "default" + File.separatorChar + DownloadHandler.defaultSWFName;
                        } else {
                            requestedFile = DownloadHandler.defaultPDFName;
                            full_path = current_dir + File.separatorChar + "default" + File.separatorChar + DownloadHandler.defaultPDFName;
                        }
                    }
                    log.debug("full_path: " + full_path);
                    File f2 = new File(full_path);
                    if (!f2.exists() || !f2.canRead()) {
                        if (!f2.canRead()) {
                            log.debug("DownloadHandler: The request DEFAULT-file does not exist / has already been deleted");
                        } else {
                            log.debug("DownloadHandler: The request DEFAULT-file does not exist / has already been deleted");
                        }
                        return;
                    }
                    RandomAccessFile rf = new RandomAccessFile(full_path, "r");
                    int browserType = 0;
                    if ((httpServletRequest.getHeader("User-Agent").contains("Firefox")) || (httpServletRequest.getHeader("User-Agent").contains("Opera"))) {
                        browserType = 1;
                    }
                    log.debug("Detected browser type:" + browserType);
                    httpServletResponse.reset();
                    httpServletResponse.resetBuffer();
                    OutputStream out = httpServletResponse.getOutputStream();
                    if (requestedFile.endsWith(".swf")) {
                        httpServletResponse.setContentType("application/x-shockwave-flash");
                        httpServletResponse.setHeader("Content-Length", "" + rf.length());
                    } else {
                        httpServletResponse.setContentType("APPLICATION/OCTET-STREAM");
                        if (browserType == 0) {
                            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(requestedFile, "UTF-8"));
                        } else {
                            httpServletResponse.setHeader("Content-Disposition", "attachment; filename*=UTF-8'en'" + java.net.URLEncoder.encode(requestedFile, "UTF-8"));
                        }
                        httpServletResponse.setHeader("Content-Length", "" + rf.length());
                    }
                    byte[] buffer = new byte[1024];
                    int readed = -1;
                    while ((readed = rf.read(buffer, 0, buffer.length)) > -1) {
                        out.write(buffer, 0, readed);
                    }
                    rf.close();
                    out.flush();
                    out.close();
                }
            } else {
                System.out.println("ERROR DownloadHandler: not authorized FileDownload " + (new Date()));
            }
        } catch (Exception er) {
            System.out.println("Error downloading: " + er);
            er.printStackTrace();
        }
    }
