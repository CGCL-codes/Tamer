    private void updatePaymentInfo(PaymentInfo paymentInfo) throws PaymentException, IOException, InterruptedException {
        String url = paymentInfo.getUpdateURL();
        HttpConnection httpConnection = createConnection(url);
        String[] contentType = { null, null };
        parseContentType(contentType, httpConnection.getType());
        if (!UPDATE_MIME_TYPE.equals(contentType[MIME_TYPE])) {
            httpConnection.close();
            throw new PaymentException(PaymentException.INVALID_UPDATE_TYPE, contentType[MIME_TYPE], null);
        }
        byte[] data = null;
        InputStream is;
        try {
            currentUI.notifyStateChange(STATE_DOWNLOADING);
            is = httpConnection.openDataInputStream();
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream(TRANSFER_CHUNK);
                byte[] buffer = new byte[TRANSFER_CHUNK];
                int read;
                while ((read = is.read(buffer, 0, TRANSFER_CHUNK)) > 0) {
                    os.write(buffer, 0, read);
                }
                os.flush();
                data = os.toByteArray();
                os.close();
            } finally {
                is.close();
            }
        } finally {
            httpConnection.close();
        }
        currentUI.notifyStateChange(STATE_VERIFYING);
        paymentInfo.updatePaymentInfo(data, contentType[CHARSET]);
        currentUI.notifyStateChange(STATE_FINISHED);
    }
