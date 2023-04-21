    public void sendMail(StringWriter data, Customer cust) throws Exception {
        String host = "mail.empowerconsultancy.in";
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.debug", "true");
        Session session = Session.getInstance(props);
        try {
            Message msg = new MimeMessage(session);
            MimeMessageHelper message = new MimeMessageHelper((MimeMessage) msg, true, "UTF-8");
            message.setFrom("suman.ravuri@empowerconsultancy.in");
            message.setTo(cust.getToAddress());
            if (null != cust.getCcAddress()) ;
            {
                message.setCc(new InternetAddress(cust.getCcAddress()));
            }
            if (null != cust.getBccAddress()) ;
            {
                message.setBcc(new InternetAddress(cust.getBccAddress()));
            }
            message.setSubject(cust.getSubject());
            message.setText(data.getBuffer().toString());
            String filePath = cust.getFilePath();
            String fileName;
            fileName = getFileName(filePath);
            message.addAttachment(fileName, new File(filePath));
            Transport.send(message.getMimeMessage());
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
