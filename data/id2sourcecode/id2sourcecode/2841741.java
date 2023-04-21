    public static void downloadProccess(Context context, FModel item) throws IOException {
        int remote = SongUtil.getRemoteSize(item.getPath());
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double available = (double) stat.getAvailableBlocks() * (double) stat.getBlockSize();
        if (remote > available) {
            item.setStatus(FModel.DOWNLOAD_STATUS.FAIL);
            return;
        }
        item.setStatus(FModel.DOWNLOAD_STATUS.ACTIVE);
        item.setDownloadTo(getFMoldelDownloadFile(context, item));
        LOG.d("begin download ", item.getDownloadTo(), item.getText(), item.getPath());
        if (new File(item.getDownloadTo()).exists()) {
            LOG.d("FModel exist", item.getDownloadTo());
            item.setStatus(FModel.DOWNLOAD_STATUS.EXIST);
            return;
        }
        URL url = new URL(item.getPath());
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("GET");
        connect.setDoOutput(true);
        connect.connect();
        FileOutputStream toStream = new FileOutputStream(new File(item.getDownloadTo()));
        InputStream fromStream = connect.getInputStream();
        if (fromStream == null) {
            LOG.d("Null from stream");
            return;
        }
        byte[] buffer = new byte[1024];
        int lenght = 0;
        int size = connect.getContentLength();
        int current = 0;
        while ((lenght = fromStream.read(buffer)) > 0) {
            toStream.write(buffer, 0, lenght);
            current += lenght;
            item.setPercent(current * 100 / size);
        }
        item.setStatus(FModel.DOWNLOAD_STATUS.DONE);
        item.setPercent(100);
        toStream.close();
        fromStream.close();
        LOG.d("end download ", item.getText(), item.getPath(), item.getDownloadTo());
        return;
    }
