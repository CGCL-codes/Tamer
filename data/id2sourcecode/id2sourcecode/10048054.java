    public void afterPropertiesSet() throws Exception {
        this.mediator = (SpmlR2PSPMediator) psp.getChannel().getIdentityMediator();
    }
