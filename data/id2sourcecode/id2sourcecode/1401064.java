    public void Send(String mailAddress, String regid) throws MessagingException {
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress(mailAddress));
        msg.setSubject(msgSubject);
        msg.setText(msgBody + regid + msgBodyEnd);
        Transport.send(msg);
    }
