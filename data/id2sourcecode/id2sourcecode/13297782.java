    private void onOK() {
        if (StringUtils.isEmpty(nameField.getText())) {
            return;
        }
        XjcHelperState xjcHelperState = XjcHelperState.getInstance(project);
        Map<String, Param> params = xjcHelperState.getState().getParams();
        if (params.containsKey(nameField.getText())) {
            String question = String.format("Name [%s] already exist, do you want to overwrite ?", nameField.getText());
            int ret = Messages.showOkCancelDialog(project, question, Constant.TITLE, Messages.getQuestionIcon());
            if (ret != 0) {
                return;
            }
        }
        param.setName(nameField.getText());
        params.put(param.getName(), param);
        dispose();
    }
