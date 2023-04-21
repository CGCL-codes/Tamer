    public void send() throws IOException {
        try {
            this.message.setFrom(new InternetAddress(this.sender));
            String[] emailToArray = this.to.split(",");
            for (int i = 0; i < emailToArray.length; i++) {
                this.message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailToArray[i]));
            }
            String[] emailToArrayCC = this.cc.split(",");
            for (int i = 0; i < emailToArrayCC.length; i++) {
                this.message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailToArrayCC[i]));
            }
            this.message.setSubject(this.sujet);
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(this.body, this.contentType);
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(textPart);
            this.message.setContent(mp);
            Transport.send(this.message);
            logger.info("Envoi du mail");
        } catch (AddressException e) {
            logger.error("Adresse mail mal configur�");
            e.printStackTrace();
        } catch (MessagingException e) {
            logger.info("Serveur mail non configur�");
            e.printStackTrace();
        }
    }
