    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ActionErrors errors = new ActionErrors();
        String login = request.getParameter("userLogin");
        String email = null;
        String password = null;
        Connection con = null;
        try {
            con = ConnectionPool.getConnection("users");
            PreparedStatement stmt = con.prepareStatement("SELECT email FROM users WHERE login = ?");
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                email = rs.getString(1);
            } else {
                log.error("User name not found: " + login);
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.resetpwd.username"));
                saveErrors(request, errors);
                return (mapping.getInputForward());
            }
            rs.close();
            stmt.close();
            password = getRandomPassword();
            stmt = con.prepareStatement("UPDATE users SET password = ? WHERE login = ?");
            stmt.setString(1, Encrypt.getBase64(password));
            stmt.setString(2, login);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            log.error("Database error. " + e.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.resetpwd.database"));
            saveErrors(request, errors);
            return (mapping.getInputForward());
        } finally {
            ConnectionPool.releaseConnection(con);
        }
        try {
            Settings settings = Settings.getInstance();
            String sender = settings.getSetting("mail2.sender");
            String recipient = email;
            String subject = "SPIDR Password Reset";
            String host = settings.getSetting("mail2.mail.smtp.host");
            SimpleSMTPHeader header;
            Writer writer;
            SMTPClient client = new SMTPClient();
            header = new SimpleSMTPHeader(sender, recipient, subject);
            client.connect(host);
            if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
                client.disconnect();
                throw new Exception("SMTP server refused connection.");
            }
            client.login();
            client.setSender(sender);
            client.addRecipient(recipient);
            writer = client.sendMessageData();
            if (writer != null) {
                writer.write(header.toString());
                writer.write("Your temporary password is: " + password + "\n");
                writer.write("Please change this password from within your SPIDR user profile when you log in.");
                writer.close();
                client.completePendingCommand();
            }
            client.logout();
            client.disconnect();
        } catch (Exception e) {
            log.error("E-mail error. " + e.getMessage());
            errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.resetpwd.email"));
            saveErrors(request, errors);
            return (mapping.getInputForward());
        }
        return (mapping.findForward("success"));
    }
