    public PasswordResetXHTMLPage(ViewContext context, String email, String linkReference) throws RepositoryException {
        super(context, "Password Reset");
        this.email = email;
        this.linkReference = linkReference;
        user = null;
        if (!email.isEmpty()) {
            List<Node> results = NodeUtils.getList(getUserByEmail(context, email));
            if (results.size() == 1) {
                user = results.get(0);
            }
        } else if (!linkReference.isEmpty()) {
            List<Node> results = NodeUtils.getList(getUserByLinkReference(context, linkReference));
            if (results.size() == 1) {
                user = results.get(0);
            }
        }
        addTab(new Tab("view", "View", buildViewContent()));
        if (linkReference.isEmpty() && !email.isEmpty() && !(user == null)) {
            String link = UserUtils.generateRandomString(50);
            EmailUtils.sendMail(email, "DIMER Password Reset", "You can reset your password here, " + context.getAppURL("/resetPassword", ViewContext.Format.HTML) + "?linkReference=" + link + ".");
            user.setProperty("passwordLinkReference", link);
            Calendar timestamp = Calendar.getInstance();
            timestamp.add(Calendar.MINUTE, 30);
            user.setProperty("passwordLinkExpiration", timestamp);
        } else if (!linkReference.isEmpty() && email.isEmpty() && !(user == null) && user.hasProperty("passwordLinkExpiration") && user.getProperty("passwordLinkExpiration").getDate().after(Calendar.getInstance())) {
            String newPassword = UserUtils.generateRandomString(8);
            EmailUtils.sendMail(user.getProperty("email").getString(), "DIMER Password Reset", "New password for DIMER account " + user.getName() + " - " + newPassword);
            user.setProperty("password", DigestUtils.digest(newPassword));
            user.setProperty("passwordLinkReference", "");
            Calendar timestamp = Calendar.getInstance();
            timestamp.add(Calendar.YEAR, -1);
            user.setProperty("passwordLinkExpiration", timestamp);
        }
        context.getSession().save();
    }
