    private void retrieveStatusPageContent() throws IOException, FailedToRetrieveServerStatusPage {
        if (url == null) {
            setupURL();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(6000);
            Utils.transferContent(connection.getInputStream(), bos, null);
            String text = bos.toString();
            this.content = text;
        } catch (FileNotFoundException e) {
            this.content = null;
            throw new FailedToRetrieveServerStatusPage(e);
        } catch (IOException e) {
            this.content = null;
            throw new FailedToRetrieveServerStatusPage(e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
