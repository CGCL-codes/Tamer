    public byte[] getAsBytes() throws AxisFault {
        log.debug("Enter: SOAPPart::getAsBytes");
        if (currentForm == FORM_OPTIMIZED) {
            log.debug("Exit: SOAPPart::getAsBytes");
            try {
                return ((ByteArray) currentMessage).toByteArray();
            } catch (IOException e) {
                throw AxisFault.makeFault(e);
            }
        }
        if (currentForm == FORM_BYTES) {
            log.debug("Exit: SOAPPart::getAsBytes");
            return (byte[]) currentMessage;
        }
        if (currentForm == FORM_BODYINSTREAM) {
            try {
                getAsSOAPEnvelope();
            } catch (Exception e) {
                log.fatal(Messages.getMessage("makeEnvFail00"), e);
                log.debug("Exit: SOAPPart::getAsBytes");
                return null;
            }
        }
        if (currentForm == FORM_INPUTSTREAM) {
            try {
                InputStream inp = null;
                byte[] buf = null;
                try {
                    inp = (InputStream) currentMessage;
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    buf = new byte[4096];
                    int len;
                    while ((len = inp.read(buf, 0, 4096)) != -1) baos.write(buf, 0, len);
                    buf = baos.toByteArray();
                } finally {
                    if (inp != null && currentMessage instanceof org.apache.axis.transport.http.SocketInputStream) inp.close();
                }
                setCurrentForm(buf, FORM_BYTES);
                log.debug("Exit: SOAPPart::getAsBytes");
                return (byte[]) currentMessage;
            } catch (Exception e) {
                log.error(Messages.getMessage("exception00"), e);
            }
            log.debug("Exit: SOAPPart::getAsBytes");
            return null;
        }
        if (currentForm == FORM_SOAPENVELOPE || currentForm == FORM_FAULT) {
            currentEncoding = XMLUtils.getEncoding(msgObject, null);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BufferedOutputStream os = new BufferedOutputStream(baos);
            try {
                this.writeTo(os);
                os.flush();
            } catch (Exception e) {
                throw AxisFault.makeFault(e);
            }
            setCurrentForm(baos.toByteArray(), FORM_BYTES);
            if (log.isDebugEnabled()) {
                log.debug("Exit: SOAPPart::getAsBytes(): " + currentMessage);
            }
            return (byte[]) currentMessage;
        }
        if (currentForm == FORM_STRING) {
            if (currentMessage == currentMessageAsString && currentMessageAsBytes != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Exit: SOAPPart::getAsBytes()");
                }
                return currentMessageAsBytes;
            }
            currentMessageAsString = (String) currentMessage;
            try {
                currentEncoding = XMLUtils.getEncoding(msgObject, null);
                setCurrentForm(((String) currentMessage).getBytes(currentEncoding), FORM_BYTES);
            } catch (UnsupportedEncodingException ue) {
                setCurrentForm(((String) currentMessage).getBytes(), FORM_BYTES);
            }
            currentMessageAsBytes = (byte[]) currentMessage;
            log.debug("Exit: SOAPPart::getAsBytes");
            return (byte[]) currentMessage;
        }
        log.error(Messages.getMessage("cantConvert00", "" + currentForm));
        log.debug("Exit: SOAPPart::getAsBytes");
        return null;
    }
