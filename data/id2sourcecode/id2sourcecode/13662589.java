        @Override
        public void run() {
            try {
                Random generator = new Random();
                int random = generator.nextInt(99999);
                Log.d(LOG_TAG, "Opening " + mAlbumFileUri.toString());
                InputStream intentInputStream = getContentResolver().openInputStream(mAlbumFileUri);
                File tempArchive = new File(new CacheManager(getApplicationContext()).getInboxDir(), "emailalbum" + random + FileUtils.getExtensionForMimeType(getIntent().getType()));
                OutputStream tempOS = new FileOutputStream(tempArchive);
                Log.d(LOG_TAG, "Write retrieved archive : " + tempArchive.getAbsolutePath());
                byte[] buffer = new byte[256];
                int readBytes = -1;
                while ((readBytes = intentInputStream.read(buffer)) != -1) {
                    tempOS.write(buffer, 0, readBytes);
                }
                mAlbumFileUri = Uri.fromFile(tempArchive);
                tempOS.close();
                intentInputStream.close();
                archiveCopyHandler.sendEmptyMessage(MSG_ARCHIVE_RETRIEVED);
            } catch (IOException e) {
                Message msg = new Message();
                Bundle data = new Bundle();
                msg.arg1 = -1;
                data.putString("EXCEPTION", e.getLocalizedMessage());
                msg.setData(data);
                archiveCopyHandler.sendMessage(msg);
            }
        }
