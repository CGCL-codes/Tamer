    public void init() throws CPAException {
        Tracer tracer = baseTracer.entering("init()");
        Channel channel = (Channel) this.lookupManager.getCPAObject(CPAObjectType.CHANNEL, this.channelId);
        tracer.info("Channel channel created : {0} with channelId : {1} for party: {2}", new Object[] { channel.getChannelName(), this.channelId, channel.getParty() });
        if (channel.getValueAsString(JCA_ADAPTER_STATUS) != null) {
            this.adapterStatus = channel.getValueAsString(JCA_ADAPTER_STATUS);
            tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_ADAPTER_STATUS, this.adapterStatus });
        } else {
            tracer.error("Channel parameter reading error : key {0}", JCA_ADAPTER_STATUS);
        }
        if (channel.getTransProt().toLowerCase() != null) {
            this.protocol = channel.getTransProt().toLowerCase();
            tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_TRANSPROT, this.protocol });
        } else {
            tracer.error("Channel parameter reading error : key {0}", JCA_HTTP_TRANSPROT);
        }
        if (channel.getDirection().toString().equals(Direction.OUTBOUND.toString())) {
            if (channel.getValueAsString(JCA_HOST) != null) {
                this.host = channel.getValueAsString(JCA_HOST).toLowerCase();
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HOST, this.host });
            } else {
                tracer.error("Channel parameter reading error : key {0}", JCA_HOST);
            }
            if (channel.getValueAsString(JCA_PORT) != null) {
                this.port = new Integer(channel.getValueAsInt(JCA_PORT));
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_PORT, this.port });
            } else {
                tracer.error("Channel parameter reading error : key {0}", JCA_PORT);
            }
            if (channel.getValueAsString(JCA_REQUEST_PATH) != null) {
                this.path = channel.getValueAsString(JCA_REQUEST_PATH);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_REQUEST_PATH, this.path });
            } else {
                tracer.error("Channel parameter reading error : key {0}", JCA_REQUEST_PATH);
            }
            if (channel.getValueAsString(JCA_HTTP_QUERY) != null) {
                this.query = channel.getValueAsString(JCA_HTTP_QUERY);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_QUERY, this.query });
            } else {
                tracer.info("Channel parameter reading : key {0} is not set", JCA_HTTP_QUERY);
            }
            if (channel.getValueAsString(JCA_HTTP_SESSIONAUTH) != null) {
                this.authentication = channel.getValueAsString(JCA_HTTP_SESSIONAUTH).toLowerCase();
                tracer.info("Channel parameter read : key = {0}", new Object[] { JCA_HTTP_SESSIONAUTH, this.authentication });
                if (this.authentication.equalsIgnoreCase("basic")) {
                    if (channel.getValueAsString(JCA_HTTP_USER) != null) {
                        this.user = channel.getValueAsString(JCA_HTTP_USER);
                        tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_USER, this.user });
                    } else {
                        tracer.error("Channel parameter reading error : key {0} is not set", JCA_HTTP_USER);
                    }
                    if (channel.getValueAsString(JCA_HTTP_PASSWORD) != null) {
                        this.password = channel.getValueAsString(JCA_HTTP_PASSWORD);
                        tracer.info("Channel parameter read : key {0}", JCA_HTTP_PASSWORD);
                    } else {
                        tracer.info("Channel parameter reading error : key {0} is not set", JCA_HTTP_PASSWORD);
                    }
                }
            } else {
                tracer.error("Channel parameter reading error : key {0}", JCA_HTTP_SESSIONAUTH);
            }
            this.useProxy = new Boolean(channel.getValueAsBoolean(JCA_HTTP_PROXY));
            if (this.useProxy.booleanValue()) {
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_PROXY, this.useProxy });
                if (channel.getValueAsString(JCA_HTTP_PROXY_HOST) != null) {
                    this.proxyHost = channel.getValueAsString(JCA_HTTP_PROXY_HOST);
                    tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_PROXY_HOST, this.proxyHost });
                } else {
                    tracer.error("Channel parameter reading error: key {0} is not set", JCA_HTTP_PROXY_HOST);
                }
                if (channel.getValueAsString(JCA_HTTP_PROXY_PORT) != null) {
                    this.proxyPort = new Integer(channel.getValueAsInt(JCA_HTTP_PROXY_PORT));
                    tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_PROXY_PORT, this.proxyPort });
                } else {
                    tracer.error("Channel parameter reading error: key {0} is not set", JCA_HTTP_PROXY_PORT);
                }
                if (channel.getValueAsString(JCA_HTTP_PROXY_USER) != null) {
                    this.proxyUser = channel.getValueAsString(JCA_HTTP_PROXY_USER);
                    tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_PROXY_USER, this.proxyUser });
                } else {
                    tracer.error("Channel parameter reading error: key {0} is not set", JCA_HTTP_PROXY_USER);
                }
                if (channel.getValueAsString(JCA_HTTP_PROXY_PASSWORD) != null) {
                    this.proxyPassword = channel.getValueAsString(JCA_HTTP_PROXY_PASSWORD);
                    tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_HTTP_PROXY_PASSWORD, this.proxyPassword });
                } else {
                    tracer.error("Channel parameter reading error: key {0} is not set", JCA_HTTP_PROXY_PASSWORD);
                }
            } else {
                tracer.info("Channel parameter read : no proxy settings defined");
            }
            if (channel.getValueAsString(JCA_CRYPT_USE_ENCRYPTION) != null) {
                this.useEncryption = channel.getValueAsBoolean(JCA_CRYPT_USE_ENCRYPTION);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_CRYPT_USE_ENCRYPTION, new Boolean(this.useEncryption) });
            } else {
                tracer.error("Channel parameter read : key = {0} encryption not found", JCA_CRYPT_USE_ENCRYPTION);
            }
            if (channel.getValueAsString(JCA_CRYPT_USE_SIGNATURE) != null) {
                this.useSignature = channel.getValueAsBoolean(JCA_CRYPT_USE_SIGNATURE);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_CRYPT_USE_SIGNATURE, new Boolean(this.useSignature) });
            } else {
                tracer.error("Channel parameter read : key = {0} signature not found", JCA_CRYPT_USE_SIGNATURE);
            }
            if (channel.getValueAsString(JCA_CRYPT_MICALG_OUT_MDN) != null) {
                this.micAlgMdn = channel.getValueAsString(JCA_CRYPT_MICALG_OUT_MDN);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_CRYPT_MICALG_OUT_MDN, this.micAlgMdn });
            } else {
                tracer.error("Channel parameter read : key = {0} micAlgMdn not found", JCA_CRYPT_MICALG_OUT_MDN);
            }
            if (channel.getValueAsString(JCA_CRYPT_MICALG_OUT_REQ) != null) {
                this.micAlgReq = channel.getValueAsString(JCA_CRYPT_MICALG_OUT_REQ);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_CRYPT_MICALG_OUT_REQ, this.micAlgReq });
            } else {
                tracer.error("Channel parameter read : key = {0} micAlgReq not found", JCA_CRYPT_MICALG_OUT_REQ);
            }
            if (channel.getValueAsString(JCA_EDIFACT_USE_MDN) != null) {
                this.mdnRequired = channel.getValueAsBoolean(JCA_EDIFACT_USE_MDN);
                tracer.info("Channel parameter read : key = {0}, value = {1}", new Object[] { JCA_EDIFACT_USE_MDN, new Boolean(this.mdnRequired) });
            } else {
                tracer.error("Channel parameter read : key = {0} signature not found", JCA_EDIFACT_USE_MDN);
            }
        }
        if (this.useSignature && this.mdnRequired) {
            this.viewCertificateForMdnSignature = channel.getValueAsString(JCA_RCV_PUBKEY_CERTID_MDN_VIEW);
            if ((this.viewCertificateForMdnSignature == null) || (this.viewCertificateForMdnSignature.trim().equals(""))) {
                tracer.error("Channel parameter reading error : key {0}", JCA_RCV_PUBKEY_CERTID_MDN_VIEW);
                String errorMessage = "View certificate for MDN signature entry could not be retrieved from channel.";
                CPAException ce = new CPAException(errorMessage);
                tracer.throwing(ce);
                throw ce;
            }
            tracer.info("View MDN certificate parameter retrieved from channel. viewCertificateForMdnSignature: {0}", this.viewCertificateForMdnSignature);
            this.certificateForMdnSignature = channel.getValueAsString(JCA_RCV_PUBKEY_CERTID_MDN);
            if ((this.certificateForMdnSignature == null) || (this.certificateForMdnSignature.trim().equals(""))) {
                tracer.error("Channel parameter reading error : key {0}", JCA_RCV_PUBKEY_CERTID_MDN);
                String errorMessage = "Certificate for MDN signature entry could not be retrieved from channel.";
                CPAException ce = new CPAException(errorMessage);
                tracer.throwing(ce);
                throw ce;
            }
            tracer.info("MDN certificate parameter retrieved from channel. certificateForMdnSignature: {0}", this.certificateForMdnSignature);
        }
        tracer.leaving();
    }
