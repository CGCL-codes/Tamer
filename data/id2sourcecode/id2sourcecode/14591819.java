        @Override
        protected Void doInBackground(Void... params) {
            InputStream is = null;
            OutputStream os = null;
            try {
                URL urlresource = new URL(TRVLMWS + url);
                is = (InputStream) urlresource.getContent();
                String dir = Environment.getExternalStorageDirectory() + "/travelme/imagescache/";
                File directories = new File(dir);
                directories.mkdirs();
                File file = new File(directories, imgpath);
                os = new FileOutputStream(file);
                byte[] buffer = new byte[4096];
                for (int n; (n = is.read(buffer)) != -1; ) os.write(buffer, 0, n);
                trvlmdb = databaseHelper.getWritableDatabase();
                ContentValues v = new ContentValues();
                v.put(TravelmeDatabase.PHOTO_CREQUESTED, 0);
                v.put(TravelmeDatabase.PHOTO_CLOCALDIR, dir + imgpath);
                trvlmdb.update(TravelmeDatabase.PHOTO_TNAME, v, TravelmeDatabase.PHOTO_CIDL + " = " + idL, null);
                trvlmdb.close();
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (os != null) try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
