    public Object stream_save(HttpServletRequest request, HttpServletResponse response, Map<String, Object> sGlobal, Map<String, Object> space, Map<String, Object> sConfig, InputStream inputStream, String albumid, String fileext, String name, String title, Integer delsize, String from) throws Exception {
        Map<String, String> jchConfig = JavaCenterHome.jchConfig;
        if (albumid == null) albumid = "0";
        if (fileext == null || fileext.equals("")) fileext = "jpg";
        if (name == null) name = "";
        if (title == null) title = "";
        if (delsize == null) delsize = 0;
        if (from == null) from = "";
        String creatAlbumid = null;
        if (!albumid.equals("0")) {
            Pattern pattern = Pattern.compile("^(?i)new:(.+)$");
            Matcher matcher = pattern.matcher(albumid);
            if (matcher.find()) {
                creatAlbumid = matcher.group(1);
            } else if (Integer.parseInt(albumid) < 0) {
                albumid = "0";
            }
        }
        Map<String, Object> setarr = new HashMap<String, Object>();
        String filepath = getFilePath(request, fileext, true);
        String newfilename = request.getSession().getServletContext().getRealPath(jchConfig.get("attachDir") + "./" + filepath);
        File newFile = new File(newfilename);
        FileOutputStream fileOutputStream = null;
        boolean writeSuccess = false;
        try {
            fileOutputStream = new FileOutputStream(newFile);
            int bufferSize = 1024 * 5;
            byte[] bufferArray = new byte[bufferSize];
            int readCount;
            while ((readCount = inputStream.read(bufferArray)) != -1) {
                fileOutputStream.write(bufferArray, 0, readCount);
            }
            fileOutputStream.close();
            fileOutputStream = null;
            inputStream.close();
            inputStream = null;
            writeSuccess = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
        if (writeSuccess) {
            int supe_uid = (Integer) sGlobal.get("supe_uid");
            long size = newFile.length();
            if (Common.empty(space)) {
                List<Map<String, Object>> query = dataBaseService.executeQuery("SELECT * FROM " + JavaCenterHome.getTableName("space") + " WHERE uid='" + supe_uid + "'");
                if (query.size() > 0) {
                    space = query.get(0);
                    sGlobal.put("supe_username", Common.addSlashes((String) space.get("username")));
                } else {
                    return -4;
                }
            }
            sGlobal.put("member", space);
            Integer maxattachsize = (Integer) Common.checkPerm(request, response, sGlobal, "maxattachsize");
            if (maxattachsize != null && maxattachsize != 0) {
                if ((Integer) space.get("attachsize") + size - delsize > maxattachsize + (Integer) space.get("addsize")) {
                    newFile.delete();
                    return -1;
                }
            }
            if (!validateImage(newFile)) {
                newFile.delete();
                return -2;
            }
            String thumbPath = ImageUtil.makeThumb(request, response, newfilename);
            int thumb = thumbPath != null ? 1 : 0;
            if ((Integer) sConfig.get("allowwatermark") == 1) {
                ImageUtil.makeWaterMark(request, response, newfilename);
            }
            String filename = Common.addSlashes((name != null && !name.equals("") ? name : filepath.substring(filepath.lastIndexOf("/") + 1)));
            title = Common.getStr(title, 200, true, true, true, 0, 0, request, response);
            int albumId;
            if (!albumid.equals("0")) {
                if (!Common.empty(creatAlbumid)) {
                    String albumname = (String) Common.sHtmlSpecialChars(creatAlbumid.trim());
                    if (Common.empty(albumname)) albumname = Common.sgmdate(request, "yyyyMMdd", 0);
                    Map<String, Object> arr = new HashMap<String, Object>();
                    arr.put("albumname", albumname);
                    arr.put("target_ids", "");
                    albumId = createAlbum(request, arr);
                } else {
                    albumId = Common.intval(albumid);
                    if (albumId != 0) {
                        List<Map<String, Object>> query = dataBaseService.executeQuery("SELECT albumname,friend FROM " + JavaCenterHome.getTableName("album") + " WHERE albumid='" + albumId + "' AND uid='" + supe_uid + "'");
                        Map<String, Object> value = query.size() > 0 ? query.get(0) : null;
                        if (!Common.empty(value)) {
                            String albumname = Common.addSlashes((String) value.get("albumname"));
                            int albumfriend = (Integer) value.get("friend");
                        } else {
                            String albumname = Common.sgmdate(request, "yyyyMMdd", 0);
                            Map<String, Object> arr = new HashMap<String, Object>();
                            arr.put("albumname", albumname);
                            arr.put("target_ids", "");
                            albumId = createAlbum(request, arr);
                        }
                    }
                }
            } else {
                albumId = 0;
            }
            setarr.put("albumid", albumId);
            setarr.put("uid", supe_uid);
            setarr.put("username", sGlobal.get("supe_username"));
            setarr.put("dateline", sGlobal.get("timestamp"));
            setarr.put("filename", filename);
            setarr.put("postip", Common.getOnlineIP(request));
            setarr.put("title", title);
            setarr.put("type", fileext);
            setarr.put("size", size);
            setarr.put("filepath", filepath);
            setarr.put("thumb", thumb);
            int tempI = dataBaseService.insertTable("pic", setarr, true, false);
            setarr.put("picid", tempI);
            StringBuilder setsql = new StringBuilder();
            if (!from.equals("")) {
                Map<String, Integer> reward = Common.getReward(from, false, 0, "", true, request, response);
                if (!Common.empty(reward)) {
                    if (reward.get("credit") != 0) {
                        setsql.append(",credit=credit+");
                        setsql.append(reward.get("credit"));
                    }
                    if (reward.get("experience") != 0) {
                        setsql.append(",experience=experience+");
                        setsql.append(reward.get("experience"));
                    }
                }
            }
            dataBaseService.executeUpdate("UPDATE " + JavaCenterHome.getTableName("space") + " SET attachsize=attachsize+'" + size + "', updatetime='" + sGlobal.get("timestamp") + "' " + setsql.toString() + " WHERE uid='" + supe_uid + "'");
            if (albumId != 0) {
                String file = filepath + (thumb == 1 ? ".thumb.jpg" : "");
                dataBaseService.executeUpdate("UPDATE " + JavaCenterHome.getTableName("album") + " " + "SET picnum=picnum+1, updatetime='" + sGlobal.get("timestamp") + "', pic='" + file + "', picflag='1' " + "WHERE albumid='" + albumId + "'");
            }
            if ((Integer) sConfig.get("allowftp") != 0) {
                FtpUtil ftpUtil = new FtpUtil();
                if (ftpUtil.ftpUpload(request, newfilename, filepath)) {
                    setarr.put("remote", 1);
                    Map<String, Object> setData = new HashMap<String, Object>();
                    setData.put("remote", 1);
                    Map<String, Object> whereData = new HashMap<String, Object>();
                    whereData.put("picid", setarr.get("picid"));
                    dataBaseService.updateTable("pic", setData, whereData);
                } else {
                    return -4;
                }
            }
            updateStat(request, "pic", false);
            return setarr;
        }
        return -3;
    }
