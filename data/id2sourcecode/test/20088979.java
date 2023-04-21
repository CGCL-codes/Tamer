    public ModuleData process(ModuleContext moduleContext, ModuleData inputModuleData) throws ModuleException {
        final Tracer tracer = baseTracer.entering("process(ModuleContext moduleContext, ModuleData inputModuleData)");
        final String AS2_MESSAGE_CONTENT_TYPE = "Request-Content-Type";
        final String CONTENT_TYPE_TEXT_PLAIN_VALUE = "text/plain";
        PartyBean partyEncoding = null;
        PartyBean partySignature = null;
        byte[] response = null;
        try {
            Message message = (Message) inputModuleData.getPrincipalData();
            tracer.info("Message message created: {0}", message);
            XMLPayload mainPayload = message.getDocument();
            tracer.info("XMLPayload mainPayload created: {0}", mainPayload);
            byte[] request = mainPayload.getContent();
            tracer.info("XMLpayloadIN.getContent(): {0}", new String(request));
            ConfigurationSettings configuration = this.configurationFactory.createInitializedConfiguration(message, moduleContext.getChannelID());
            if (configuration.isMessageDirectionInbound()) this.audit.setMessageInformation(message.getMessageId(), Audit.INBOUND); else this.audit.setMessageInformation(message.getMessageId(), Audit.OUTBOUND);
            this.audit.logEntering(VERSION_ID);
            boolean cryptMethodEnc = configuration.getCryptMethodEnc();
            boolean cryptMethodSig = configuration.getCryptMethodSig();
            String micAlgReq = configuration.getMicAlgReq();
            if (configuration.isMessageDirectionInbound()) {
                this.audit.logSuccess("Header mapping from " + message.getFromParty() + " to " + configuration.getXiPartyFrom() + " (party)");
            }
            if (cryptMethodEnc) {
                partyEncoding = this.kmLocal.retrieveParty(configuration.getCertificateForEncryption(), configuration.getViewCertificateForEncryption());
                tracer.info("Retrieved partyEncoding from KeyStore: {0}", partyEncoding);
            }
            if (cryptMethodSig) {
                partySignature = this.kmLocal.retrieveParty(configuration.getCertificateForSignature(), configuration.getViewCertificateForSignature());
                tracer.info("Retrieved partySignature from KeyStore: {0}", partySignature);
            }
            if (configuration.getMessageType().equals(EdifactUtil.MSG_TYPE_EDIFACT)) {
                byte[] attachment = message.getAttachment(EdifactUtil.ATTACHMENT_CONFIG).getContent();
                tracer.info("Payload attachment with key {0} retrieved.", EdifactUtil.ATTACHMENT_CONFIG);
                tracer.info("Attachment: {0}", new String(attachment));
                HashMap additionalParameter = (HashMap) ModuleUtil.getMapInstance(attachment);
                tracer.info("HashMap additionalParameter : {0}", additionalParameter.toString());
                String contentType = null;
                if (configuration.isMessageDirectionOutbound()) {
                    contentType = (String) additionalParameter.get(AS2_MESSAGE_CONTENT_TYPE);
                    if (contentType != null) contentType = contentType.replace('~', '=');
                } else if (configuration.isMessageDirectionInbound()) {
                    contentType = CONTENT_TYPE_TEXT_PLAIN_VALUE;
                }
                tracer.info("HashMap contentType : {0}", contentType);
                tracer.info("Incoming request: {0}", new String(request));
                request = Transformer.decodeBase64(request);
                tracer.info("Decoded request: {0}", new String(request));
                MimeBodyPart part = null;
                if (cryptMethodEnc || cryptMethodSig) {
                    ContentType contentTypeObj = new ContentType(contentType);
                    tracer.info("contentTypeObj.toString(): {0}", contentTypeObj.toString());
                    ByteArrayDataSource byteArrayDataSource = new ByteArrayDataSource(request, contentTypeObj.toString(), null);
                    tracer.info("byteArrayDataSource: {0}", byteArrayDataSource);
                    part = new SMimeBodyPart();
                    tracer.info("SMimeBodyPart part instantiated");
                    part.setDataHandler(new DataHandler(byteArrayDataSource));
                    tracer.info("part.setDataHandler");
                    part.setHeader("Content-Type", contentTypeObj.toString());
                    tracer.info("Part set content type header.");
                    tracer.info("SMimeBodyPart contentType: {0}", part.getContentType());
                    tracer.info("SMimeBodyPart encoding: {0}", part.getEncoding());
                    tracer.info("SMimeBodyPart created.");
                } else {
                    tracer.info("No encryption and/or signature configured. SMimeBodyPart not needed/created.");
                }
                tracer.info("Message direction is: {0}", configuration.isMessageDirectionOutbound() ? "OUTBOUND" : "INBOUND");
                if (configuration.isMessageDirectionOutbound()) {
                    if (!cryptMethodEnc) {
                        tracer.info("No decryption.");
                        this.audit.logSuccess("Cryptographic methods not applicable (Crypt method encryption: " + cryptMethodEnc + ").");
                    } else if (cryptMethodEnc) {
                        tracer.info("Encryption is true.");
                        tracer.info("Try to get certificate for partyEncoding {0}.", partyEncoding.getPartyId());
                        X509Certificate certPartyEncoding = partyEncoding.getCertificate();
                        if (certPartyEncoding != null) tracer.info("Received certificate certPartyEncoding for partyEncoding {0}.", partyEncoding.getPartyId());
                        tracer.info("Try to get privateKey for partyEncoding {0}.", partyEncoding.getPartyId());
                        tracer.info("Class name of partyEncoding certificate is: {0}.", certPartyEncoding.getClass());
                        tracer.info("Package name of partyEncoding certificate is: {0}.", certPartyEncoding.getClass().getPackage());
                        PrivateKey privateKeyPartyEncoding = partyEncoding.getPrivateKey();
                        if (privateKeyPartyEncoding != null) tracer.info("Received private key privateKeyPartyEncoding for partyEncoding {0}.", partyEncoding.getPartyId());
                        part = this.cmLocal.decrypt(part, certPartyEncoding, privateKeyPartyEncoding, configuration);
                        CryptUtil.validateSignatureAgainstChannel(part, cryptMethodSig, micAlgReq);
                        tracer.info("Signature configuration of channel successfully validated against message.");
                        tracer.info("cmLocal.decrypt(part, certPartyEncoding, privateKeyPartyEncoding)");
                        audit.logSuccess("Message was decrypted successfully (Crypt method encryption: " + cryptMethodEnc + ").");
                    }
                    if (!cryptMethodSig) {
                        tracer.info("No verifying.");
                        this.audit.logSuccess("Cryptographic methods not applicable (Crypt method signature: " + cryptMethodSig + ").");
                    } else if (cryptMethodSig) {
                        part = this.cmLocal.verify(part, partySignature.getCertificate());
                        tracer.info("cmLocal.verify(part, partySignature.getCertificate())");
                        this.audit.logSuccess("Message was verified successfully (Crypt method signature: " + cryptMethodSig + ").");
                    }
                } else if (configuration.isMessageDirectionInbound()) {
                    if (!cryptMethodSig) {
                        tracer.info("No signature.");
                        this.audit.logSuccess("Signature handling not used. (Crypt method signature: " + cryptMethodSig + ").");
                    } else if (cryptMethodSig) {
                        tracer.info("Unsigned message content is: {0}", part.getContent());
                        part = this.cmLocal.sign(part, partySignature.getCertificate(), partySignature.getPrivateKey(), micAlgReq);
                        tracer.info("part.getContentType() after signature: {0}", part.getContentType());
                        tracer.info("part.getHeader(\"Content-Type\") after signature: {0}", part.getHeader("Content-Type"));
                        tracer.info("micAlgReq is: {0}", micAlgReq);
                        tracer.info("cmLocal.sign(multipart, partySignature.getCertificate(), partySignature.getPrivateKey(), micAlg)");
                        this.audit.logSuccess("Message was signed successfully (Crypt method signature: " + cryptMethodSig + ").");
                    }
                    if (!cryptMethodEnc) {
                        tracer.info("No decryption.");
                        this.audit.logSuccess("Cryptographic methods not applicable (Crypt method encryption: " + cryptMethodEnc + ").");
                    } else if (cryptMethodEnc) {
                        part = this.cmLocal.encrypt(part, partyEncoding.getCertificate(), configuration.getCryptalgSym());
                        tracer.info("part.getContentType() after encryption: {0}", part.getContentType());
                        tracer.info("cmLocal.encrypt(part, partyEncoding.getCertificate(), partyEncoding.getPrivateKey())");
                        tracer.info("Encrypted Message is: {0}", part.getContent());
                        this.audit.logSuccess("Message was encrypted successfully (Crypt method encryption: " + cryptMethodEnc + ").");
                    }
                }
                if (cryptMethodEnc || cryptMethodSig) {
                    tracer.info("ContentType of message is: {0}", part.getContentType());
                    response = Transformer.convertInputStreamToByteArray(part.getInputStream());
                    tracer.info("response = Transformer.convertInputStreamToByteArray(part.getInputStream())");
                } else {
                    response = request;
                    tracer.info("response = request");
                }
                tracer.info("Response is: {0}", new String(response));
                response = Transformer.encodeBase64(response);
                tracer.info("Encoded response is: {0}", new String(response));
            }
            mainPayload.setContent(response);
            message.setDocument(mainPayload);
            inputModuleData.setPrincipalData(message);
            tracer.info("XMLPayload payload assigned to Message message main payload");
            inputModuleData.setPrincipalData(message);
            tracer.info("inputModuleData.setPrincipalData(message)");
        } catch (ModuleException e) {
            ModuleExceptionEEDM me = null;
            if (e instanceof ModuleExceptionEEDM) {
                me = (ModuleExceptionEEDM) e;
                this.audit.logError(TRACE_EXC_RAISED + me.getMessage() + " - error code " + me.getErrorCode());
            } else {
                String errorMessage = TRACE_EXC_RAISED + e.toString();
                me = new ModuleExceptionEEDM(errorMessage, e, ModuleExceptionEEDM.UNSPECIFIED_ERROR);
                this.audit.logError(TRACE_EXC_RAISED + e.toString());
            }
            tracer.error(TRACE_EXC_RAISED + e.getClass().getName());
            tracer.catched(e);
            tracer.throwing(me);
            throw me;
        } catch (CryptException e) {
            this.audit.logError(TRACE_EXC_RAISED + e.toString());
            this.audit.logCatched(e);
            ModuleExceptionEEDM me = null;
            String errorMessage = e.getErrorMessage();
            me = new ModuleExceptionEEDM(errorMessage, e, ModuleExceptionEEDM.UNSPECIFIED_ERROR);
            tracer.catched(e);
            tracer.throwing(me);
            this.audit.logError("Edifact error " + me.getErrorCode() + "occurred: " + me.getMessage());
            throw me;
        } catch (Throwable t) {
            this.audit.logError(TRACE_EXC_RAISED + t.toString());
            tracer.catched(t);
            String errorMessage = TRACE_EXC_RAISED + t.toString();
            ModuleExceptionEEDM me = new ModuleExceptionEEDM(errorMessage, t, ModuleExceptionEEDM.UNSPECIFIED_ERROR);
            tracer.throwing(t);
            throw me;
        }
        this.audit.logSuccess("Exiting: " + VERSION_ID);
        tracer.leaving();
        return inputModuleData;
    }
