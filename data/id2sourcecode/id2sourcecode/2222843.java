    public boolean equals(Object obj) {
        if (!(obj instanceof CmsNewsletterContent)) {
            return false;
        }
        CmsNewsletterContent newsletterContent = (CmsNewsletterContent) obj;
        if (getOrder() != newsletterContent.getOrder()) {
            return false;
        }
        if (!getContent().equals(newsletterContent.getContent())) {
            return false;
        }
        if (!getChannel().equals(newsletterContent.getChannel())) {
            return false;
        }
        if (!getType().equals(newsletterContent.getType())) {
            return false;
        }
        return true;
    }
