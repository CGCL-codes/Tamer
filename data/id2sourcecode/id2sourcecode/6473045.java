    @Override
    protected void saveLocal() throws Exception {
        parent.setSuccess(false);
        String fileName = Lister.getCurrentInstance().getMember().getFileName();
        File pictureDir = new File(fileName.substring(0, fileName.lastIndexOf(".")) + File.separator + "online" + File.separator);
        pictureDir.mkdirs();
        URL url = null;
        String[] urlStrings = urlJTextArea.getText().trim().split("\\s");
        pictures = new Picture[urlStrings.length];
        for (int n = 0, i = urlStrings.length; n < i; n++) {
            try {
                url = URLHelper.contructURL(urlStrings[n]);
            } catch (Exception e) {
                throw new CommonException(Language.translateStatic("ERROR_INVALIDURL"));
            }
            sizeJLabel.setText(Download.getFormatedSize(url.openConnection().getContentLength()));
            File file = SimpleFileDownloader.downloadFile(url, pictureDir, progressBar);
            pictures[n] = new Picture(file);
            pictures[n].setURL(url);
            pictureJPanel.setPicture(pictures[n]);
        }
        parent.setSuccess(true);
        parent.dispose();
    }
