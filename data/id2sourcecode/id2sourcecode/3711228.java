    public static AttachmentInfo[] saveRequestFiles(int forumid, int maxAllowFileCount, int maxSizePerDay, int maxFileSize, int todayUploadedSize, String allowFileType, int watermarkstatus, Config config, File[] postfiles, String[] postfileFileNames, String[] postfileContentTypes) throws IOException {
        System.out.println(allowFileType);
        String[] tmp = allowFileType.split("\r\n");
        String[] allowFileExtName = new String[tmp.length];
        int[] maxSize = new int[tmp.length];
        for (int i = 0; i < tmp.length; i++) {
            allowFileExtName[i] = tmp[i].substring(0, tmp[i].lastIndexOf(","));
            maxSize[i] = Utils.null2Int(tmp[i].substring(tmp[i].lastIndexOf(",") + 1), 0);
        }
        boolean isMultipart = postfiles.length > 0;
        if (isMultipart) {
            int saveFileCount = postfiles.length;
            if (logger.isDebugEnabled()) {
                logger.debug("最大允许的上传文件个数：" + maxAllowFileCount + ",当前附件数：" + saveFileCount);
            }
            AttachmentInfo[] attachmentinfo = new AttachmentInfo[saveFileCount];
            if (saveFileCount > maxAllowFileCount) {
                return attachmentinfo;
            }
            saveFileCount = 0;
            Random random = new Random(System.currentTimeMillis());
            for (int i = 0; i < postfiles.length; i++) {
                String filename = postfileFileNames[i];
                String fileextname = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                String filetype = postfileContentTypes[i];
                int filesize = Utils.null2Int(postfiles[i].length());
                String newfilename = "";
                attachmentinfo[saveFileCount] = new AttachmentInfo();
                attachmentinfo[saveFileCount].setSys_noupload("");
                if (!(Utils.isImgFilename(filename) && !filetype.startsWith("image"))) {
                    int extnameid = Utils.getInArrayID(fileextname, allowFileExtName);
                    if (extnameid >= 0 && (filesize <= maxSize[extnameid]) && (maxFileSize >= filesize) && (maxSizePerDay - todayUploadedSize >= filesize)) {
                        todayUploadedSize = todayUploadedSize + filesize;
                        String uploadDir = config.getWebpath() + "upload/";
                        StringBuilder savedir = new StringBuilder("");
                        if (config.getAttachsave() == 1) {
                            savedir.append(Calendar.getInstance().get(Calendar.YEAR));
                            savedir.append("/");
                            savedir.append(Calendar.getInstance().get(Calendar.MONTH));
                            savedir.append("/");
                            savedir.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                            savedir.append("/");
                            savedir.append(forumid);
                            savedir.append("/");
                        } else if (config.getAttachsave() == 2) {
                            savedir.append(forumid);
                            savedir.append("/");
                        } else if (config.getAttachsave() == 3) {
                            savedir.append(fileextname);
                            savedir.append("/");
                        } else {
                            savedir.append(Calendar.getInstance().get(Calendar.YEAR));
                            savedir.append("/");
                            savedir.append(Calendar.getInstance().get(Calendar.MONTH));
                            savedir.append("/");
                            savedir.append(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                            savedir.append("/");
                        }
                        newfilename = "" + (System.currentTimeMillis() & Integer.MAX_VALUE) + i + random.nextInt() + "." + fileextname;
                        newfilename = savedir.toString() + newfilename;
                        if ((fileextname.equals("bmp") || fileextname.equals("jpg") || fileextname.equals("jpeg") || fileextname.equals("png")) && filetype.startsWith("image")) {
                            BufferedImage img = ImageIO.read(postfiles[i]);
                            if (config.getAttachimgmaxwidth() > 0 && img.getWidth() > config.getAttachimgmaxwidth()) {
                                attachmentinfo[saveFileCount].setSys_noupload("图片宽度为" + img.getWidth() + ", 系统允许的最大宽度为" + config.getAttachimgmaxwidth());
                            }
                            if (config.getAttachimgmaxheight() > 0 && img.getHeight() > config.getAttachimgmaxheight()) {
                                attachmentinfo[saveFileCount].setSys_noupload("图片高度为" + img.getHeight() + ", 系统允许的最大高度为" + config.getAttachimgmaxheight());
                            }
                            if (attachmentinfo[saveFileCount].getSys_noupload().equals("")) {
                                FileUtils.copyFile(postfiles[i], new File(uploadDir + newfilename));
                                if (watermarkstatus != 0) {
                                    if (config.getWatermarktype() == 1 && Utils.fileExists(config.getWebpath() + "watermark/" + config.getWatermarkpic())) {
                                        addImageSignPic(img, uploadDir + newfilename, config.getWebpath() + "watermark/" + config.getWatermarkpic(), config.getWatermarkstatus(), config.getAttachimgquality(), (config.getWatermarktransparency() / 10.0f));
                                    } else {
                                        String watermarkText;
                                        watermarkText = config.getWatermarktext().replace("{1}", config.getForumtitle());
                                        watermarkText = watermarkText.replace("{2}", config.getForumurl());
                                        watermarkText = watermarkText.replace("{3}", Utils.getNowShortDate());
                                        watermarkText = watermarkText.replace("{4}", Utils.getNowShortTime());
                                        addImageSignText(img, uploadDir + newfilename, watermarkText, config.getWatermarkstatus(), config.getAttachimgquality(), config.getWatermarkfontname(), config.getWatermarkfontsize());
                                    }
                                }
                                attachmentinfo[saveFileCount].setFilesize(Utils.null2Int(new File(uploadDir + newfilename).length()));
                                attachmentinfo[saveFileCount].setFilesize(filesize);
                            }
                        } else {
                            attachmentinfo[saveFileCount].setFilesize(filesize);
                            FileUtils.copyFile(postfiles[i], new File(uploadDir + newfilename));
                        }
                    } else {
                        if (extnameid < 0) {
                            attachmentinfo[saveFileCount].setSys_noupload("文件格式无效");
                        } else if (maxSizePerDay - todayUploadedSize < filesize) {
                            attachmentinfo[saveFileCount].setSys_noupload("文件大于今天允许上传的字节数" + (maxSizePerDay - todayUploadedSize));
                        } else if (filesize > maxSize[extnameid]) {
                            attachmentinfo[saveFileCount].setSys_noupload("文件大于该类型附件允许的字节数" + maxSize[extnameid]);
                        } else {
                            attachmentinfo[saveFileCount].setSys_noupload("文件大于单个文件允许上传的字节数");
                        }
                    }
                } else {
                    attachmentinfo[saveFileCount].setSys_noupload("文件格式无效");
                }
                attachmentinfo[saveFileCount].setFilename(newfilename);
                attachmentinfo[saveFileCount].setDescription(fileextname);
                attachmentinfo[saveFileCount].setFiletype(filetype);
                attachmentinfo[saveFileCount].setAttachment(filename);
                attachmentinfo[saveFileCount].setDownloads(0);
                attachmentinfo[saveFileCount].setPostdatetime(Utils.getNowTime());
                attachmentinfo[saveFileCount].setSys_index(i);
                saveFileCount++;
            }
            return attachmentinfo;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("无上传附件");
            }
            return null;
        }
    }
