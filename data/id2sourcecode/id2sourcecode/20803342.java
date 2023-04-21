    private EditTransfer getPageEditTransfer(String pageName) {
        String urlString = getUploadPageURL(pageName);
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            return null;
        }
        try {
            String htmlData = new String(FileHelper.readFile(url.openStream()));
            int pagePos = htmlData.indexOf(PAGE_EDIT_TIME_PATTERN);
            htmlData = htmlData.substring(pagePos);
            int endIndex = htmlData.indexOf("'/>");
            String editTime = htmlData.substring(PAGE_EDIT_TIME_PATTERN.length(), endIndex);
            htmlData = htmlData.substring(PAGE_EDIT_TIME_PATTERN.length() + endIndex);
            pagePos = htmlData.indexOf(PAGE_EDIT_TEXT_PATTERN);
            htmlData = htmlData.substring(pagePos);
            endIndex = htmlData.indexOf("</textarea>");
            String text = htmlData.substring(PAGE_EDIT_TEXT_PATTERN.length(), endIndex);
            EditTransfer transfer = new Wiki().new EditTransfer();
            transfer.edittime = Long.parseLong(editTime);
            transfer.text = text;
            return transfer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
