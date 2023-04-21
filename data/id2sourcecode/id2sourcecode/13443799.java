    public IppResponse send() throws IppException, IOException {
        if (alreadySent) throw new IllegalStateException("Request is already sent");
        alreadySent = true;
        OutputStream stream = connection.getOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        out.writeShort(VERSION);
        out.writeShort(operation_id);
        out.writeInt(request_id);
        logger.log(Component.IPP, "OperationID: " + Integer.toHexString(operation_id) + " RequestID: " + request_id);
        logger.log(Component.IPP, "Operation Attributes");
        RequestWriter writer = new RequestWriter(out);
        writer.writeOperationAttributes(operationAttributes);
        if (jobAttributes != null) {
            logger.log(Component.IPP, "Job Attributes");
            out.write(IppDelimiterTag.JOB_ATTRIBUTES_TAG);
            writer.writeAttributes(jobAttributes);
        }
        if (printerAttributes != null) {
            logger.log(Component.IPP, "Printer Attributes");
            out.write(IppDelimiterTag.PRINTER_ATTRIBUTES_TAG);
            writer.writeAttributes(printerAttributes);
        }
        out.write(IppDelimiterTag.END_OF_ATTRIBUTES_TAG);
        if (data instanceof InputStream) {
            byte[] readbuf = new byte[2048];
            int len = 0;
            while ((len = ((InputStream) data).read(readbuf)) > 0) out.write(readbuf, 0, len);
        } else if (data != null) {
            out.write((byte[]) data);
        }
        out.flush();
        stream.flush();
        connection.setConnectTimeout(timeout);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            IppResponse response = new IppResponse(requestUri, operation_id);
            response.setResponseData(connection.getInputStream());
            return response;
        }
        logger.log(Component.IPP, "HTTP-Statuscode: " + responseCode);
        throw new IppException("Request failed got HTTP status code " + responseCode);
    }
