    public UploadResult saveOnDatabase(UploadResult result, Properties formFields, File file, String version) {
        String userId = UserProfileManager.getUserId().toLowerCase();
        Date rightNow = new Date();
        FileAttachment fileAttachment = new FileAttachment();
        fileAttachment.setCreationDate(rightNow);
        fileAttachment.setCreatedBy(userId);
        fileAttachment.setLastUpdate(rightNow);
        fileAttachment.setLastUpdateBy(userId);
        fileAttachment.setFileName(result.getFileName());
        fileAttachment.setFileType(result.getFileType());
        fileAttachment.setFileSize(new Long(result.getFileSize()).intValue());
        if (formFields.containsKey("description")) {
            fileAttachment.setDescription(formFields.getProperty("description"));
        } else {
            StringBuffer buffer = new StringBuffer();
            buffer.append("File \"");
            buffer.append(result.getFileName());
            buffer.append("\" uploaded by user \"");
            buffer.append(userId);
            buffer.append("\" from remote location \"");
            buffer.append(EaasyStreet.getServletRequest().getRemoteHost());
            buffer.append("\" on ");
            buffer.append(DateUtils.dateTimeToString(rightNow));
            fileAttachment.setDescription(buffer.toString());
        }
        InputStream is = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream os = null;
        BufferedOutputStream bos = null;
        try {
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            os = new ByteArrayOutputStream();
            bos = new BufferedOutputStream(os);
            int byteRead = 0;
            byte[] buf = new byte[bis.available()];
            while ((byteRead = bis.read(buf, 0, buf.length)) != -1) {
                bos.write(buf, 0, byteRead);
            }
        } catch (Exception e) {
            EaasyStreet.logError("Exception processing attachment: " + e.toString(), e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    ;
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (Exception e) {
                    ;
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (Exception e) {
                    ;
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    ;
                }
            }
        }
        fileAttachment.setFileData(os.toByteArray());
        int id = FileUtils.saveFileAttachment(fileAttachment, version);
        if (id > -1) {
            String url = EaasyStreet.getServletRequest().getRequestURL().toString();
            url = url.substring(0, url.lastIndexOf("/"));
            result.setFullUrl(url + "/fetch?id=" + id);
            String uri = EaasyStreet.getServletRequest().getRequestURI();
            uri = uri.substring(0, uri.lastIndexOf("/"));
            result.setRelativeUrl(uri + "/fetch?id=" + id);
            result.setKey(new Integer(id));
        }
        return result;
    }
