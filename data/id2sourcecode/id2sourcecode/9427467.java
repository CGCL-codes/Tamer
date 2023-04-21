    public Object process(Atom oAtm) throws FileNotFoundException, IOException, MessagingException {
        File oFile;
        FileReader oFileRead;
        String sPathHTML;
        char cBuffer[];
        StringBufferInputStream oInStrm;
        Object oReplaced;
        final String Yes = "1";
        final String sSep = System.getProperty("file.separator");
        if (DebugFile.trace) {
            DebugFile.writeln("Begin EMailSender.process([Job:" + getStringNull(DB.gu_job, "") + ", Atom:" + String.valueOf(oAtm.getInt(DB.pg_atom)) + "])");
            DebugFile.incIdent();
        }
        if (bHasReplacements) {
            sPathHTML = getProperty("workareasput");
            if (!sPathHTML.endsWith(sSep)) sPathHTML += sSep;
            sPathHTML += getParameter("gu_workarea") + sSep + "apps" + sSep + "Mailwire" + sSep + "html" + sSep + getParameter("gu_pageset") + sSep;
            sPathHTML += getParameter("nm_pageset").replace(' ', '_') + ".html";
            if (DebugFile.trace) DebugFile.writeln("PathHTML = " + sPathHTML);
            if (Yes.equals(getParameter("bo_attachimages"))) {
                if (DebugFile.trace) DebugFile.writeln("bo_attachimages=true");
                oInStrm = null;
                if (null != oHTMLStr) {
                    if (null != oHTMLStr.get()) oInStrm = new StringBufferInputStream((String) oHTMLStr.get());
                }
                if (null == oInStrm) oInStrm = new StringBufferInputStream(attachFiles(sPathHTML));
                oHTMLStr = new SoftReference(oInStrm);
                oReplaced = oReplacer.replace(oInStrm, oAtm.getItemMap());
            } else {
                if (DebugFile.trace) DebugFile.writeln("bo_attachimages=false");
                oReplaced = oReplacer.replace(sPathHTML, oAtm.getItemMap());
            }
            bHasReplacements = (oReplacer.lastReplacements() > 0);
        } else {
            oReplaced = null;
            if (null != oFileStr) oReplaced = oFileStr.get();
            if (null == oReplaced) {
                sPathHTML = getProperty("workareasput");
                if (!sPathHTML.endsWith(sSep)) sPathHTML += sSep;
                sPathHTML += getParameter("gu_workarea") + sSep + "apps" + sSep + "Mailwire" + sSep + "html" + sSep + getParameter("gu_pageset") + sSep + getParameter("nm_pageset").replace(' ', '_') + ".html";
                if (DebugFile.trace) DebugFile.writeln("PathHTML = " + sPathHTML);
                if (DebugFile.trace) DebugFile.writeln("new File(" + sPathHTML + ")");
                oFile = new File(sPathHTML);
                cBuffer = new char[new Long(oFile.length()).intValue()];
                oFileRead = new FileReader(oFile);
                oFileRead.read(cBuffer);
                oFileRead.close();
                if (DebugFile.trace) DebugFile.writeln(String.valueOf(cBuffer.length) + " characters readed");
                if (Yes.equals(getParameter("bo_attachimages"))) oReplaced = attachFiles(new String(cBuffer)); else oReplaced = new String(cBuffer);
                oFileStr = new SoftReference(oReplaced);
            }
        }
        if (null == oMailSession) {
            if (DebugFile.trace) DebugFile.writeln("Session.getInstance(Job.getProperties(), null)");
            java.util.Properties oMailProps = getProperties();
            if (oMailProps.getProperty("mail.transport.protocol") == null) oMailProps.put("mail.transport.protocol", "smtp");
            if (oMailProps.getProperty("mail.host") == null) oMailProps.put("mail.host", "localhost");
            oMailSession = Session.getInstance(getProperties(), null);
            if (null != oMailSession) {
                oMailTransport = oMailSession.getTransport();
                try {
                    oMailTransport.connect();
                } catch (NoSuchProviderException nspe) {
                    if (DebugFile.trace) DebugFile.writeln("MailTransport.connect() NoSuchProviderException " + nspe.getMessage());
                    throw new MessagingException(nspe.getMessage(), nspe);
                }
            }
        }
        MimeMessage oMsg;
        InternetAddress oFrom, oTo;
        try {
            if (null == getParameter("tx_sender")) oFrom = new InternetAddress(getParameter("tx_from")); else oFrom = new InternetAddress(getParameter("tx_from"), getParameter("tx_sender"));
            if (DebugFile.trace) DebugFile.writeln("to: " + oAtm.getStringNull(DB.tx_email, "ERROR Atom[" + String.valueOf(oAtm.getInt(DB.pg_atom)) + "].tx_email is null!"));
            oTo = new InternetAddress(oAtm.getString(DB.tx_email), oAtm.getStringNull(DB.tx_name, "") + " " + oAtm.getStringNull(DB.tx_surname, ""));
        } catch (AddressException adre) {
            if (DebugFile.trace) DebugFile.writeln("AddressException " + adre.getMessage() + " job " + getString(DB.gu_job) + " atom " + String.valueOf(oAtm.getInt(DB.pg_atom)));
            oFrom = null;
            oTo = null;
            throw new MessagingException("AddressException " + adre.getMessage() + " job " + getString(DB.gu_job) + " atom " + String.valueOf(oAtm.getInt(DB.pg_atom)));
        }
        if (DebugFile.trace) DebugFile.writeln("new MimeMessage([Session])");
        oMsg = new MimeMessage(oMailSession);
        oMsg.setSubject(getParameter("tx_subject"));
        oMsg.setFrom(oFrom);
        if (DebugFile.trace) DebugFile.writeln("MimeMessage.addRecipient(MimeMessage.RecipientType.TO, " + oTo.getAddress());
        oMsg.addRecipient(MimeMessage.RecipientType.TO, oTo);
        String sSrc = null, sCid = null;
        try {
            if (Yes.equals(getParameter("bo_attachimages"))) {
                BodyPart oMsgBodyPart = new MimeBodyPart();
                oMsgBodyPart.setContent(oReplaced, "text/html");
                MimeMultipart oMultiPart = new MimeMultipart("related");
                oMultiPart.addBodyPart(oMsgBodyPart);
                Iterator oImgs = oDocumentImages.keySet().iterator();
                while (oImgs.hasNext()) {
                    BodyPart oImgBodyPart = new MimeBodyPart();
                    sSrc = (String) oImgs.next();
                    sCid = (String) oDocumentImages.get(sSrc);
                    if (sSrc.startsWith("www.")) sSrc = "http://" + sSrc;
                    if (sSrc.startsWith("http://") || sSrc.startsWith("https://")) {
                        oImgBodyPart.setDataHandler(new DataHandler(new URL(sSrc)));
                    } else {
                        oImgBodyPart.setDataHandler(new DataHandler(new FileDataSource(sSrc)));
                    }
                    oImgBodyPart.setHeader("Content-ID", sCid);
                    oMultiPart.addBodyPart(oImgBodyPart);
                }
                if (DebugFile.trace) DebugFile.writeln("MimeMessage.setContent([MultiPart])");
                oMsg.setContent(oMultiPart);
            } else {
                if (DebugFile.trace) DebugFile.writeln("MimeMessage.setContent([String], \"text/html\")");
                oMsg.setContent(oReplaced, "text/html");
            }
            oMsg.saveChanges();
            if (DebugFile.trace) DebugFile.writeln("Transport.sendMessage([MimeMessage], MimeMessage.getAllRecipients())");
            oMailTransport.sendMessage(oMsg, oMsg.getAllRecipients());
            iPendingAtoms--;
        } catch (MalformedURLException urle) {
            if (DebugFile.trace) DebugFile.writeln("MalformedURLException " + sSrc);
            throw new MessagingException("MalformedURLException " + sSrc);
        }
        if (DebugFile.trace) {
            DebugFile.writeln("End EMailSender.process([Job:" + getStringNull(DB.gu_job, "") + ", Atom:" + String.valueOf(oAtm.getInt(DB.pg_atom)) + "])");
            DebugFile.decIdent();
        }
        return oReplaced;
    }
