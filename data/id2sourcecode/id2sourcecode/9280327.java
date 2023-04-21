    public void uploadCV() {
        String uploadType = Constant.FILE_TYPE_CV;
        StringBuffer[] cvs = new StringBuffer[2];
        System.out.println("fileNames:" + uploadsFileName);
        System.out.println("uploadContentTypes:" + uploadsContentType);
        if (uploads != null) {
            if (uploads.get(0) != null) {
                cvs[0] = new StringBuffer("");
                cvs[0].append("en_" + FileAccessUtil.generateUploadFileName(uploadsFileName.get(0), uploadType));
            }
            if (uploads.get(1) != null) {
                cvs[1] = new StringBuffer("");
                cvs[1].append("cn_" + FileAccessUtil.generateUploadFileName(uploadsFileName.get(1), uploadType));
            }
            System.out.println(cvs[0].toString());
            System.out.println(cvs[1].toString());
            File dir = new File(this.savePath);
            if (!dir.isDirectory()) dir.mkdirs();
            File target_cn = new File(this.savePath, cvs[0].toString());
            File target_en = new File(this.savePath, cvs[1].toString());
            try {
                FileUtils.copyFile(uploads.get(0), target_cn);
                FileUtils.copyFile(uploads.get(1), target_en);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.candidate.setCv_path(savePath + "/" + cvs[0] + ";" + savePath + "/" + cvs[1]);
    }
