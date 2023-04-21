    @Override
    public String getPrependText(boolean checkButton) {
        if (isIgnoringActions()) {
            return "";
        }
        if (checkButton && isToolItemSelected(ToolBarItemKey.PREPEND_TEXT_BUTTON)) {
            return connector.getChannelTabPrefix(channel);
        } else if (!checkButton) {
            return connector.getChannelTabPrefix(channel);
        } else {
            return "";
        }
    }
