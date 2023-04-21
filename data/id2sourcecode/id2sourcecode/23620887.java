    public void run() {
        try {
            SMAPRequestEnvelope request;
            SMAPReplyEnvelope reply;
            try {
                SMAPParser parser = new SMAPParser();
                SMAPParsedEnvelope pe = parser.parse(this.message);
                pe.setChannel(new SMAPChannel(message.getChannel()));
                request = new SMAPRequestEnvelope(pe);
                reply = request.getReplyEnvelope();
                reply.setReturnHandler(this.message);
            } catch (SMAPException se) {
                try {
                    se.printStackTrace();
                    this.message.sendERR(BEEPError.CODE_GENERAL_SYNTAX_ERROR, "ERROR: " + se.getMessage());
                } catch (BEEPException be) {
                }
                return;
            }
            String methodName = SMAPRegistry.getMethodName(request.getHeader().getRequestProc().getMethodName());
            Class methodClass = SMAPRegistry.getMethodClass(methodName);
            Method method = SMAPRegistry.getMethod(methodClass, methodName);
            if (method == null) {
                try {
                    this.message.sendERR(BEEPError.CODE_PARAMETER_NOT_IMPLEMENTED, "ERROR(" + methodName + "): unregistered method");
                } catch (BEEPException be) {
                }
                return;
            }
            Object[] args = { (Object) request, (Object) reply };
            try {
                method.invoke(methodClass, args);
            } catch (InvocationTargetException ite) {
                ite.printStackTrace();
                try {
                    this.message.sendERR(BEEPError.CODE_TRANSACTION_FAILED, "ERROR(" + methodName + "):" + ite.getMessage());
                } catch (BEEPException be) {
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    this.message.sendERR(BEEPError.CODE_PARAMETER_NOT_IMPLEMENTED, "ERROR(" + methodName + "): unknown method");
                } catch (BEEPException be) {
                }
                return;
            }
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                this.message.getChannel().close();
            } catch (BEEPException be) {
            }
            return;
        }
    }
