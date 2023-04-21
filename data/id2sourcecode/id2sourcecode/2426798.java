    private int receiveFile(BluetoothOppReceiveFileInfo fileInfo, Operation op) {
        int status = -1;
        BufferedOutputStream bos = null;
        InputStream is = null;
        boolean error = false;
        try {
            is = op.openInputStream();
        } catch (IOException e1) {
            Log.e(TAG, "Error when openInputStream");
            status = BluetoothShare.STATUS_OBEX_DATA_ERROR;
            error = true;
        }
        Uri contentUri = Uri.parse(BluetoothShare.CONTENT_URI + "/" + mInfo.mId);
        if (!error) {
            ContentValues updateValues = new ContentValues();
            updateValues.put(BluetoothShare._DATA, fileInfo.mFileName);
            mContext.getContentResolver().update(contentUri, updateValues, null, null);
        }
        int position = 0;
        if (!error) {
            bos = new BufferedOutputStream(fileInfo.mOutputStream, 0x10000);
        }
        if (!error) {
            int outputBufferSize = op.getMaxPacketSize();
            byte[] b = new byte[outputBufferSize];
            int readLength = 0;
            long timestamp = 0;
            try {
                while ((!mInterrupted) && (position != fileInfo.mLength)) {
                    if (V) timestamp = System.currentTimeMillis();
                    readLength = is.read(b);
                    if (readLength == -1) {
                        if (D) Log.d(TAG, "Receive file reached stream end at position" + position);
                        break;
                    }
                    bos.write(b, 0, readLength);
                    position += readLength;
                    if (V) {
                        Log.v(TAG, "Receive file position = " + position + " readLength " + readLength + " bytes took " + (System.currentTimeMillis() - timestamp) + " ms");
                    }
                    ContentValues updateValues = new ContentValues();
                    updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                    mContext.getContentResolver().update(contentUri, updateValues, null, null);
                }
            } catch (IOException e1) {
                Log.e(TAG, "Error when receiving file");
                status = BluetoothShare.STATUS_OBEX_DATA_ERROR;
                error = true;
            }
        }
        if (mInterrupted) {
            if (D) Log.d(TAG, "receiving file interrupted by user.");
            status = BluetoothShare.STATUS_CANCELED;
        } else {
            if (position == fileInfo.mLength) {
                if (D) Log.d(TAG, "Receiving file completed for " + fileInfo.mFileName);
                status = BluetoothShare.STATUS_SUCCESS;
            } else {
                if (D) Log.d(TAG, "Reading file failed at " + position + " of " + fileInfo.mLength);
                if (status == -1) {
                    status = BluetoothShare.STATUS_UNKNOWN_ERROR;
                }
            }
        }
        if (bos != null) {
            try {
                bos.close();
            } catch (IOException e) {
                Log.e(TAG, "Error when closing stream after send");
            }
        }
        return status;
    }
