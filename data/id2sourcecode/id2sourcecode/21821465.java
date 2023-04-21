    private String validateArticleUpdate() {
        String result = checkLoginAndError();
        if (result != null) {
            return result;
        }
        if (vldRight(bean.getId())) {
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
