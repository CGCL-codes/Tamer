        private int sendFile(BluetoothOppSendFileInfo fileInfo) {
            boolean error = false;
            int responseCode = -1;
            int status = BluetoothShare.STATUS_SUCCESS;
            Uri contentUri = Uri.parse(BluetoothShare.CONTENT_URI + "/" + mInfo.mId);
            ContentValues updateValues;
            HeaderSet request;
            request = new HeaderSet();
            request.setHeader(HeaderSet.NAME, fileInfo.mFileName);
            request.setHeader(HeaderSet.TYPE, fileInfo.mMimetype);
            applyRemoteDeviceQuirks(request, fileInfo);
            Constants.updateShareStatus(mContext1, mInfo.mId, BluetoothShare.STATUS_RUNNING);
            request.setHeader(HeaderSet.LENGTH, fileInfo.mLength);
            ClientOperation putOperation = null;
            OutputStream outputStream = null;
            InputStream inputStream = null;
            try {
                synchronized (this) {
                    mWaitingForRemote = true;
                }
                try {
                    if (V) Log.v(TAG, "put headerset for " + fileInfo.mFileName);
                    putOperation = (ClientOperation) mCs.put(request);
                } catch (IOException e) {
                    status = BluetoothShare.STATUS_OBEX_DATA_ERROR;
                    Constants.updateShareStatus(mContext1, mInfo.mId, status);
                    Log.e(TAG, "Error when put HeaderSet ");
                    error = true;
                }
                synchronized (this) {
                    mWaitingForRemote = false;
                }
                if (!error) {
                    try {
                        if (V) Log.v(TAG, "openOutputStream " + fileInfo.mFileName);
                        outputStream = putOperation.openOutputStream();
                        inputStream = putOperation.openInputStream();
                    } catch (IOException e) {
                        status = BluetoothShare.STATUS_OBEX_DATA_ERROR;
                        Constants.updateShareStatus(mContext1, mInfo.mId, status);
                        Log.e(TAG, "Error when openOutputStream");
                        error = true;
                    }
                }
                if (!error) {
                    updateValues = new ContentValues();
                    updateValues.put(BluetoothShare.CURRENT_BYTES, 0);
                    updateValues.put(BluetoothShare.STATUS, BluetoothShare.STATUS_RUNNING);
                    mContext1.getContentResolver().update(contentUri, updateValues, null, null);
                }
                if (!error) {
                    int position = 0;
                    int readLength = 0;
                    boolean okToProceed = false;
                    long timestamp = 0;
                    int outputBufferSize = putOperation.getMaxPacketSize();
                    byte[] buffer = new byte[outputBufferSize];
                    BufferedInputStream a = new BufferedInputStream(fileInfo.mInputStream, 0x4000);
                    if (!mInterrupted && (position != fileInfo.mLength)) {
                        readLength = a.read(buffer, 0, outputBufferSize);
                        mCallback.sendMessageDelayed(mCallback.obtainMessage(BluetoothOppObexSession.MSG_CONNECT_TIMEOUT), BluetoothOppObexSession.SESSION_TIMEOUT);
                        synchronized (this) {
                            mWaitingForRemote = true;
                        }
                        outputStream.write(buffer, 0, readLength);
                        position += readLength;
                        if (position != fileInfo.mLength) {
                            mCallback.removeMessages(BluetoothOppObexSession.MSG_CONNECT_TIMEOUT);
                            synchronized (this) {
                                mWaitingForRemote = false;
                            }
                        } else {
                            outputStream.close();
                            mCallback.removeMessages(BluetoothOppObexSession.MSG_CONNECT_TIMEOUT);
                            synchronized (this) {
                                mWaitingForRemote = false;
                            }
                        }
                        responseCode = putOperation.getResponseCode();
                        if (responseCode == ResponseCodes.OBEX_HTTP_CONTINUE || responseCode == ResponseCodes.OBEX_HTTP_OK) {
                            if (V) Log.v(TAG, "Remote accept");
                            okToProceed = true;
                            updateValues = new ContentValues();
                            updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                            mContext1.getContentResolver().update(contentUri, updateValues, null, null);
                        } else {
                            Log.i(TAG, "Remote reject, Response code is " + responseCode);
                        }
                    }
                    while (!mInterrupted && okToProceed && (position != fileInfo.mLength)) {
                        {
                            if (V) timestamp = System.currentTimeMillis();
                            readLength = a.read(buffer, 0, outputBufferSize);
                            outputStream.write(buffer, 0, readLength);
                            responseCode = putOperation.getResponseCode();
                            if (V) Log.v(TAG, "Response code is " + responseCode);
                            if (responseCode != ResponseCodes.OBEX_HTTP_CONTINUE && responseCode != ResponseCodes.OBEX_HTTP_OK) {
                                okToProceed = false;
                            } else {
                                position += readLength;
                                if (V) {
                                    Log.v(TAG, "Sending file position = " + position + " readLength " + readLength + " bytes took " + (System.currentTimeMillis() - timestamp) + " ms");
                                }
                                updateValues = new ContentValues();
                                updateValues.put(BluetoothShare.CURRENT_BYTES, position);
                                mContext1.getContentResolver().update(contentUri, updateValues, null, null);
                            }
                        }
                    }
                    if (responseCode == ResponseCodes.OBEX_HTTP_FORBIDDEN || responseCode == ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE) {
                        Log.i(TAG, "Remote reject file " + fileInfo.mFileName + " length " + fileInfo.mLength);
                        status = BluetoothShare.STATUS_FORBIDDEN;
                    } else if (responseCode == ResponseCodes.OBEX_HTTP_UNSUPPORTED_TYPE) {
                        Log.i(TAG, "Remote reject file type " + fileInfo.mMimetype);
                        status = BluetoothShare.STATUS_NOT_ACCEPTABLE;
                    } else if (!mInterrupted && position == fileInfo.mLength) {
                        Log.i(TAG, "SendFile finished send out file " + fileInfo.mFileName + " length " + fileInfo.mLength);
                        outputStream.close();
                    } else {
                        error = true;
                        status = BluetoothShare.STATUS_CANCELED;
                        putOperation.abort();
                        Log.i(TAG, "SendFile interrupted when send out file " + fileInfo.mFileName + " at " + position + " of " + fileInfo.mLength);
                    }
                }
            } catch (IOException e) {
                status = BluetoothShare.STATUS_OBEX_DATA_ERROR;
                Log.e(TAG, "Error when sending file");
                Constants.updateShareStatus(mContext1, mInfo.mId, status);
                mCallback.removeMessages(BluetoothOppObexSession.MSG_CONNECT_TIMEOUT);
            } finally {
                try {
                    fileInfo.mInputStream.close();
                    if (!error) {
                        responseCode = putOperation.getResponseCode();
                        if (responseCode != -1) {
                            if (V) Log.v(TAG, "Get response code " + responseCode);
                            if (responseCode != ResponseCodes.OBEX_HTTP_OK) {
                                Log.i(TAG, "Response error code is " + responseCode);
                                status = BluetoothShare.STATUS_UNHANDLED_OBEX_CODE;
                                if (responseCode == ResponseCodes.OBEX_HTTP_UNSUPPORTED_TYPE) {
                                    status = BluetoothShare.STATUS_NOT_ACCEPTABLE;
                                }
                                if (responseCode == ResponseCodes.OBEX_HTTP_FORBIDDEN || responseCode == ResponseCodes.OBEX_HTTP_NOT_ACCEPTABLE) {
                                    status = BluetoothShare.STATUS_FORBIDDEN;
                                }
                            }
                        } else {
                            status = BluetoothShare.STATUS_CONNECTION_ERROR;
                        }
                    }
                    Constants.updateShareStatus(mContext1, mInfo.mId, status);
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (putOperation != null) {
                        putOperation.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error when closing stream after send");
                }
            }
            return status;
        }
