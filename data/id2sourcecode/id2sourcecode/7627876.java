    private void synchronize() {
        Calendar syncTime = CalendarUtils.getCalendar();
        try {
            URL url = new URL(account.getSyncUrl());
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Accept-Encoding", "gzip,deflate");
            connection.setDoOutput(true);
            OutputStream out = connection.getOutputStream();
            out.write("login=".getBytes());
            out.write(account.getSyncLogin().getBytes());
            out.write("&password=".getBytes());
            out.write(account.getSyncPassword().getBytes());
            out.write("&lastSync=".getBytes());
            out.write(String.valueOf(account.getLastSync().getTimeInMillis()).getBytes());
            out.write("&data=".getBytes());
            try {
                out.write(toXml().getBytes());
            } catch (SQLException e) {
                logger.error("Unable to get account info!", e);
                Iniglet.displayNotification("Error", "Unable to sync data!\n" + e.getMessage(), MessageType.Error);
            }
            out.flush();
            out.close();
            String encoding = connection.getContentEncoding();
            InputStream in = null;
            if (encoding.contains("gzip")) {
                in = new GZIPInputStream(connection.getInputStream());
            } else if (encoding.contains("deflate")) {
                in = new InflaterInputStream(connection.getInputStream());
            } else {
                in = connection.getInputStream();
            }
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            AccountCrud crud = new AccountCrud(account);
            parser.parse(in, new SynchronizationHandler(crud));
            account.setLastSync(syncTime);
            crud.saveAccount();
            lastSync.setText(account.getLastSync().getTime().toString());
        } catch (IOException e) {
            logger.error("Synchronization Error", e);
            Iniglet.displayNotification("Error", e.getMessage(), MessageType.Error, true);
        } catch (ParserConfigurationException e) {
            logger.error("TODO", e);
            Iniglet.displayNotification("Error", e.getMessage(), MessageType.Error, true);
        } catch (SAXException e) {
            logger.error("TODO", e);
            Iniglet.displayNotification("Error", e.getMessage(), MessageType.Error, true);
        } catch (SQLException e) {
            logger.error("Unable to update account synchronization time!", e);
            Iniglet.displayNotification("Error", e.getMessage(), MessageType.Error, true);
        } catch (Exception e) {
            Iniglet.displayNotification("Error", e.getMessage(), MessageType.Error, true);
        }
    }
