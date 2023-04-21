    public String uploadImg() {
        log.debug("�ϴ��ļ���" + uploadFileFileName);
        if (uploadFileFileName == null) {
            error = "�ϴ�ʧ�ܣ�û���ҵ��ϴ��ļ�";
            log.info(error);
            return SUCCESS;
        }
        UploadRule rule = (UploadRule) contextPvd.getSessionAttr(UploadRule.KEY + uploadRuleId);
        if (rule == null) {
            error = "�ϴ�ʧ�ܣ�δ�ҵ��ϴ�����";
            log.info(error);
            return SUCCESS;
        }
        int suffixIndex = uploadFileFileName.lastIndexOf('.');
        if (suffixIndex == -1) {
            error = "�ϴ�ʧ�ܣ��ļ�û�к�׺�������ϴ���";
            log.info(error);
            return SUCCESS;
        }
        String name = uploadFileFileName.substring(0, suffixIndex);
        String suffix = uploadFileFileName.substring(suffixIndex + 1).toLowerCase();
        if (StringUtils.isBlank(name)) {
            error = "�ϴ�ʧ�ܣ����ļ���û���ļ��������ϴ���";
            log.info(error);
            return SUCCESS;
        }
        if (!rule.isGenName() && StrUtils.countCn(name) > name.length()) {
            error = "�ϴ�ʧ�ܣ����ļ�������ģ��������ϴ���";
            log.info(error);
            return SUCCESS;
        }
        if (!rule.getAcceptImg().contains(suffix)) {
            error = "�ϴ�ʧ�ܣ��ú�׺�������ϴ���" + suffix;
            log.info(error);
            return SUCCESS;
        }
        uploadPath = rule.getPathName(name, suffix, Constants.UPLOAD_IMAGE);
        String imgRelPath = rule.getRootPath() + uploadPath;
        String realPath = contextPvd.getAppRealPath(imgRelPath);
        try {
            File toSave;
            if (isZoom) {
                long start = System.currentTimeMillis();
                toSave = new File(realPath);
                ImageScale.resizeFix(uploadFile, toSave, zoomWidth, zoomHeight);
                long end = System.currentTimeMillis();
                log.info("�ϴ���ѹ���ļ���{}��ѹ����ʱ��{}ms��", realPath, end - start);
            } else {
                toSave = new File(realPath);
                FileUtils.copyFile(uploadFile, toSave);
                log.info("�ϴ��ļ��ɹ���{}", realPath);
            }
            if (rule.isNeedClear()) {
                rule.addUploadFile(UtilsFile.sanitizeFileName(uploadFileFileName), toSave.getName(), toSave.getAbsolutePath(), toSave.length());
            }
            return SUCCESS;
        } catch (IOException e) {
            error = "�ϴ�ʧ�ܣ���Ϣ:" + e.getMessage();
            log.error("�ϴ�ʧ�ܣ�", e);
            return SUCCESS;
        }
    }
