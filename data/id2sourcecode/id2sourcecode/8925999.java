    @Override
    protected Uri doInBackground(Uri... params) {
        try {
            InputStream is = mContext.getContentResolver().openInputStream(params[0]);
            mBitmap = BitmapFactory.decodeStream(is);
        } catch (FileNotFoundException e) {
            try {
                URL url = new URL(params[0].toString());
                URLConnection conn = url.openConnection();
                conn.setDoOutput(false);
                conn.setDefaultUseCaches(true);
                conn.setUseCaches(true);
                conn.setConnectTimeout(5000);
                InputStream is = conn.getInputStream();
                mBitmap = BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (FileNotFoundException fe) {
                fe.printStackTrace();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
        if (mBitmap == null) return null;
        if (mBitmap.getHeight() > mDimension || mBitmap.getWidth() > mDimension) {
            int width = mBitmap.getWidth() > mDimension ? mDimension : (mDimension * mBitmap.getWidth() / mBitmap.getHeight());
            int height = mBitmap.getHeight() > mDimension ? mDimension : (mDimension * mBitmap.getHeight() / mBitmap.getWidth());
            mBitmap = Bitmap.createScaledBitmap(mBitmap, width, height, true);
        }
        return params[0];
    }
