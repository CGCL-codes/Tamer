    public void received(com.googlecode.jamr.model.EncoderReceiverTransmitterMessage ert) {
        String serial = ert.getSerial();
        log.trace("received serial: " + serial);
        try {
            com.google.gdata.client.Service.GDataRequest request = service.getRequestFactory().getRequest(com.google.gdata.client.Service.GDataRequest.RequestType.INSERT, postUrl, new com.google.gdata.util.ContentType("application/x-www-form-urlencoded"));
            java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(request.getRequestStream());
            writer.append("sql=" + java.net.URLEncoder.encode("INSERT INTO " + jamrTable + " (serial, recorded_at, reading) VALUES ('" + serial + "', '" + java.text.DateFormat.getInstance().format(ert.getDate()) + "', '" + ert.getReading() + "')", "UTF-8"));
            writer.flush();
            request.execute();
        } catch (Exception e) {
            java.io.StringWriter sw = new java.io.StringWriter();
            java.io.PrintWriter pw = new java.io.PrintWriter(sw);
            e.printStackTrace(pw);
            log.error(sw.toString());
        }
    }
