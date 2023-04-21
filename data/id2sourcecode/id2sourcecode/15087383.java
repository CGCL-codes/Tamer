    private void uploadDmpRecords() {
        LOGGER.info("st: Uploading " + dmpRecords.size() + " DmpAft packets");
        try {
            URL url = new URL(Main.UPLOAD_URL);
            for (int i = 0; i < dmpRecords.size(); i++) {
                DmpRecord record = (DmpRecord) dmpRecords.get(i);
                if (record.isValid()) {
                    LOGGER.info("st: Uploading record " + i + " to " + url.toString());
                    URLConnection conn = url.openConnection();
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(record.toPostRequest());
                    wr.flush();
                    wr.close();
                }
            }
        } catch (MalformedURLException mue) {
            LOGGER.error(mue);
        } catch (IOException ioe) {
            LOGGER.error(ioe);
        }
        LOGGER.info("st: Done uploading " + dmpRecords.size() + " DmpAft packets");
    }
