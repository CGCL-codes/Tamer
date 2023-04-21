        private TransactionStatus sendSecondLogin(Primitive res) {
            try {
                Primitive secondLogin = buildBasicLoginReq();
                String nonce = res.getElementContents(ImpsTags.Nonce);
                String digestSchema = res.getElementContents(ImpsTags.DigestSchema);
                String digestBytes = mConfig.getPasswordDigest().digest(digestSchema, nonce, mSession.getPassword());
                secondLogin.addElement(ImpsTags.DigestBytes, digestBytes);
                sendRequest(secondLogin);
                return TransactionStatus.TRANSACTION_CONTINUE;
            } catch (ImException e) {
                ImpsLog.logError(e);
                shutdownOnError(new ImErrorInfo(ImErrorInfo.UNKNOWN_ERROR, e.toString()));
                return TransactionStatus.TRANSACTION_COMPLETED;
            }
        }
