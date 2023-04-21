    @Test
    public void testDownloadAvatar() throws BlipConnectorException, IOException, HttpRequestException {
        BlipConnector bc = newConnector();
        Download userAvatar = bc.getAvatar(BlipConnector.AvatarSize.nano);
        byte[] buffer = new byte[1024];
        int read = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((read = userAvatar.getContent().read(buffer)) > 0) {
            out.write(buffer, 0, read);
        }
        System.out.println(out.toByteArray().length);
    }
