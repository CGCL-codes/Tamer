    private void initMediaSize() throws MmsException {
        ContentResolver cr = mContext.getContentResolver();
        InputStream input = null;
        try {
            input = cr.openInputStream(mUri);
            if (input instanceof FileInputStream) {
                FileInputStream f = (FileInputStream) input;
                mSize = (int) f.getChannel().size();
            } else {
                while (-1 != input.read()) {
                    mSize++;
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException caught while opening or reading stream", e);
            if (e instanceof FileNotFoundException) {
                throw new MmsException(e.getMessage());
            }
        } finally {
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException caught while closing stream", e);
                }
            }
        }
    }
