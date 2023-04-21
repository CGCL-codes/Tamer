        @Override
        public int ndefMakeReadOnly(final int nativeHandle) throws RemoteException {
            this.lastException = null;
            this.lastErrorCode = ErrorCodes.SUCCESS;
            final NfcInformation nfcInformation = NfcInformationCache.getCache().get(nativeHandle);
            if (nfcInformation == null || nfcInformation.connectedStatus != NfcInformation.STATUS_CONNECTED) {
                this.lastErrorCode = ErrorCodes.ERROR_DISCONNECT;
                return this.lastErrorCode;
            }
            nfcInformation.intializeTime();
            final NfcTagConnection connection = nfcInformation.mNfcTagConnection;
            try {
                nfcInformation.intializeTime();
                if (connection.isReadOnly() == true) {
                    nfcInformation.intializeTime();
                    return this.lastErrorCode = ErrorCodes.SUCCESS;
                }
                nfcInformation.intializeTime();
                if (connection.isLockable() == false) {
                    nfcInformation.intializeTime();
                    return this.lastErrorCode = ErrorCodes.ERROR_WRITE;
                }
                nfcInformation.intializeTime();
                connection.writeMessage(connection.readMessage(), NfcTagManager.ACTION_BIT_CHECK_WRITE | NfcTagManager.ACTION_BIT_ERASE | NfcTagManager.ACTION_BIT_LOCK);
            } catch (final NfcException exception) {
                this.lastException = exception;
                this.lastErrorCode = HelperForNfc.obtainErrorCode(this.lastException, ErrorCodes.ERROR_WRITE);
            }
            nfcInformation.intializeTime();
            return this.lastErrorCode;
        }
