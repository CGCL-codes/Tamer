    private String _getWidget(String userId, final WidgetType widgetType, final String height, final String width) {
        if (userId == null || userId.equals("")) {
            userId = Constants.UI_ANONYMOUS;
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(HASHING_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            s_logger.info("NoSuchAlgorithmException caught. Failed to get instance of MessageDigest with algorithm: " + HASHING_ALGORITHM);
        }
        byte[] digest = md.digest((f_accountId + "---" + userId).getBytes());
        final String hashedArg = bytes2String(digest);
        final String widgetPath = f_usePath + Constants.API_VERSION + "/" + Constants.PATH_WIDGET;
        if (widgetType != WidgetType.NOTIFIER) {
            return "<iframe style='border:none' height='" + height + "px' width='" + width + "px' allowtransparency='true' scrolling='no' src='" + widgetPath + "?widget=" + widgetType.getName() + "&u=" + hashedArg + "&height=" + height + "&width=" + width + "'>Sorry your browser does not support iframes!</iframe>";
        } else {
            return "<script type='text/javascript' src=" + widgetPath + "?widget=" + widgetType.getName() + "&u=" + hashedArg + "&height=" + height + "&width=" + width + "&t=outside'></script> <iframe src='" + widgetPath + "?widget=" + widgetType.getName() + "&u=" + hashedArg + "&height=" + height + "&width=" + width + "&t=inside width=\"0\" height=\"0\" frameborder=\"0\" allowtransparency='true' scrolling=\"no\" name=\"ui__notifier\"'>Your browser is not supported. Sorry!</iframe>";
        }
    }
