    @Override
    protected Boolean doInBackground(IXmppFacade... params) {
        boolean result = true;
        IXmppFacade facade = params[0];
        try {
            publishProgress(STATE_CONNECTION_RUNNING);
            mConnection = facade.createConnection();
            if (!mConnection.connect()) {
                mErrorMessage = mConnection.getErrorMessage();
                return false;
            }
            publishProgress(STATE_LOGIN_RUNNING);
            if (!mConnection.login()) {
                mErrorMessage = mConnection.getErrorMessage();
                publishProgress(STATE_LOGIN_FAILED);
                return false;
            }
            publishProgress(STATE_LOGIN_SUCCESS);
        } catch (RemoteException e) {
            mErrorMessage = "Exception during connection :" + e;
            result = false;
        }
        return result;
    }
