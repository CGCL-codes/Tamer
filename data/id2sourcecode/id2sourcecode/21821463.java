    private String validateArticleSubmit() {
        String result = checkLoginAndError();
        if (result != null) {
            return result;
        }
        if (!imageCaptchaService.validateResponseForID(contextPvd.getSessionId(false), checkCode)) {
            addActionError("��֤�����");
            return showError();
        }
        if (!HtmlChecker.check(bean.getContent())) {
            addActionError("���ݲ��ܰ�������");
            return showError();
        }
        if (vldUploadRule()) {
            return showError();
        }
        if (vldChannel(bean.getChannel().getId(), bean)) {
            return showError();
        }
        return null;
    }
