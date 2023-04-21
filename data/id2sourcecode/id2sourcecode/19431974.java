    @Override
    protected Boolean doInBackground(String... params) {
        String isbn = params[0];
        BookJB jb = addItemToDB(isbn);
        if (jb != null) {
            try {
                URI dest = new URI(jb.getThumbUrl());
                HttpGet cover = new HttpGet(dest);
                HttpResponse resp = client.execute(cover);
                if (resp.getEntity().getContentType().getValue().equals("image/jpeg")) {
                    Bitmap image = BitmapFactory.decodeStream(resp.getEntity().getContent());
                    File file = new File(c.getExternalCacheDir(), jb.getVolumeId() + ".jpg");
                    image.compress(CompressFormat.JPEG, COMPRESSION_RATIO, new FileOutputStream(file));
                }
            } catch (Exception e) {
                Log.e(DownloadDataTask.class.getName(), "Unable to save thumbnail", e);
            }
            STATUS retVal;
            if (library instanceof LibStatus) {
                retVal = ((LibStatus) library).checkAvailability(jb.getIsbn());
                if (retVal != null) {
                    jb.setState(retVal.name());
                    mDbHelper.updateItem(jb);
                }
            }
        }
        return jb != null;
    }
