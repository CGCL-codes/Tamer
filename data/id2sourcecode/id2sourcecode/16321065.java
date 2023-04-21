    private File saveMessages(Session session, MultiChannelMessage mcm, String toList, String ccList, String bccList, MessageRecipient fromUser) throws Exception {
        String charSet = "UTF-8";
        if (mcm.getCharacterEncoding() != null) {
            charSet = mcm.getCharacterEncoding();
        }
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File temp = File.createTempFile("RUNMPS_", "_GeneratedOutput_" + charSet + ".txt", tempDir);
        OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(temp), "UTF8");
        List devices = new ArrayList(10);
        osw.write("Generated output\n");
        osw.write("-------------------------------------------------------\n");
        osw.write("MultiChannelMessage:\n");
        osw.write("Subject: " + mcm.getSubject() + "\n");
        osw.write("Content: " + mcm.getMessage() + "\n");
        osw.write("URL    : " + mcm.getMessageURL() + "\n");
        osw.write("Charset: " + mcm.getCharacterEncoding() + "\n");
        osw.write("-------------------------------------------------------\n");
        osw.write("Sender\n");
        osw.write("Address: " + fromUser.getAddress() + "\n");
        osw.write("MSISDN : " + fromUser.getMSISDN() + "\n");
        osw.write("Channel: " + fromUser.getChannelName() + "\n");
        osw.write("Device : " + fromUser.getDeviceName() + "\n");
        osw.write("\nMessageRecipients\n");
        MessageRecipients mrs = session.getRecipients(toList);
        osw.write("\n'TO' recipients\n");
        Iterator it = mrs.getIterator();
        while (it.hasNext()) {
            MessageRecipient mr = (MessageRecipient) it.next();
            osw.write("Address: " + mr.getAddress() + "\n");
            osw.write("MSISDN : " + mr.getMSISDN() + "\n");
            osw.write("Channel: " + mr.getChannelName() + "\n");
            osw.write("Device : " + mr.getDeviceName() + "\n\n");
            if (!devices.contains(mr.getDeviceName())) {
                devices.add(mr.getDeviceName());
            }
        }
        mrs = session.getRecipients(ccList);
        osw.write("\n'CC' recipients\n");
        it = mrs.getIterator();
        while (it.hasNext()) {
            MessageRecipient mr = (MessageRecipient) it.next();
            osw.write("Address: " + mr.getAddress() + "\n");
            osw.write("MSISDN : " + mr.getMSISDN() + "\n");
            osw.write("Channel: " + mr.getChannelName() + "\n");
            osw.write("Device : " + mr.getDeviceName() + "\n\n");
            if (!devices.contains(mr.getDeviceName())) {
                devices.add(mr.getDeviceName());
            }
        }
        mrs = session.getRecipients(bccList);
        osw.write("\n'BCC' recipients\n");
        it = mrs.getIterator();
        while (it.hasNext()) {
            MessageRecipient mr = (MessageRecipient) it.next();
            osw.write("Address: " + mr.getAddress() + "\n");
            osw.write("MSISDN : " + mr.getMSISDN() + "\n");
            osw.write("Channel: " + mr.getChannelName() + "\n");
            osw.write("Device : " + mr.getDeviceName() + "\n\n");
            if (!devices.contains(mr.getDeviceName())) {
                devices.add(mr.getDeviceName());
            }
        }
        osw.write("-------------------------------------------------------\n");
        it = devices.iterator();
        while (it.hasNext()) {
            String device = (String) it.next();
            osw.write("Message generation for device: " + device + "\n");
            osw.write("\nGenerate as string\n");
            try {
                String ret = new String(mcm.generateTargetMessageAsString(device).getBytes("utf-8"), "utf-8");
                osw.write(ret);
                osw.write("\n");
            } catch (MessageException e) {
                osw.write("ERROR: " + e.getMessage() + "\n");
            }
            osw.write("\nGenerate as URL\n");
            try {
                URL ret = mcm.generateTargetMessageAsURL(device, null);
                osw.write(ret.toString());
                osw.write("\n");
            } catch (MessageException e) {
                osw.write("ERROR: " + e.getMessage() + "\n");
            }
            osw.write("\nGenerate as Mime\n");
            try {
                MimeMultipart ret = mcm.generateTargetMessageAsMimeMultipart(device);
                writeMimeMultipart(ret, osw);
                osw.write("\n");
            } catch (MessageException e) {
                osw.write("ERROR: " + e.getMessage() + "\n");
            }
        }
        osw.write("-------------------------------------------------------\n");
        osw.flush();
        osw.close();
        logger.info("Generated output placed in: " + temp.getAbsolutePath());
        return temp;
    }
