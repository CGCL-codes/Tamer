    private void persistData(PduPart part, Uri uri, String contentType) throws MmsException {
        OutputStream os = null;
        InputStream is = null;
        try {
            byte[] data = part.getData();
            if ("text/plain".equals(contentType) || "application/smil".equals(contentType)) {
                ContentValues cv = new ContentValues();
                cv.put(Telephony.Mms.Part.TEXT, new EncodedStringValue(data).getString());
                if (mContentResolver.update(uri, cv, null, null) != 1) {
                    throw new MmsException("unable to update " + uri.toString());
                }
            } else {
                os = mContentResolver.openOutputStream(uri);
                if (data == null) {
                    Uri dataUri = part.getDataUri();
                    if ((dataUri == null) || (dataUri == uri)) {
                        Log.w(TAG, "Can't find data for this part.");
                        return;
                    }
                    is = mContentResolver.openInputStream(dataUri);
                    if (LOCAL_LOGV) {
                        Log.v(TAG, "Saving data to: " + uri);
                    }
                    byte[] buffer = new byte[256];
                    for (int len = 0; (len = is.read(buffer)) != -1; ) {
                        os.write(buffer, 0, len);
                    }
                } else {
                    if (LOCAL_LOGV) {
                        Log.v(TAG, "Saving data to: " + uri);
                    }
                    os.write(data);
                }
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "Failed to open Input/Output stream.", e);
            throw new MmsException(e);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read/write data.", e);
            throw new MmsException(e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing: " + os, e);
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, "IOException while closing: " + is, e);
                }
            }
        }
    }
