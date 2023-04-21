    public void writePolling(String message) {
        try {
            String urlString = processUrl(pollWriteUrl);
            urlString = GuiUtils.replace(urlString, "%MESSAGE%", message);
            if (lastMsgId != null) {
                urlString = GuiUtils.replace(urlString, "%LASTMESSAGEID%", lastMsgId);
            }
            debug("writePolling: url=" + urlString + "\n\tMessage=" + message);
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream ostream = connection.getOutputStream();
            message = URLEncoder.encode(message);
            String postMessage;
            if (pollPostTemplate != null) {
                postMessage = processUrl(pollPostTemplate);
                postMessage = GuiUtils.replace(postMessage, "%MESSAGE%", message);
            } else {
                postMessage = "MESSAGE=" + message;
            }
            ostream.write(postMessage.getBytes());
            ostream.flush();
            readFromConnection(connection, extraBuffer);
        } catch (Throwable exc) {
            errorMsg("An error sending a message has occurred:" + exc);
        }
    }
