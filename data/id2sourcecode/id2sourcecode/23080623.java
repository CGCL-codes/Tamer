    public static RequestTask doPlainHttpRequest(RequestTask req, ProgressCanvas pb) throws Exception {
        boolean post = req.postDataLength > 0;
        HttpConnection hc = doPlainHttpRequestInternal(req, pb);
        InputStream is = req.is;
        int len = (int) hc.getLength();
        if (req.isProgressBarTask) pb.setText(HelperApp.translateWord(HelperApp.TEXT_PROGRESS_BAR_HTTP_MSG_GET_REQUEST_DOWNLOADING_DATA) + len + " Bytes");
        System.out.println("content-length:" + len);
        if (len <= 0) {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bout.write(ch);
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = is.read(buffer);
                if (read > 0) bout.write(buffer, 0, read);
            }
            req.response = bout.toByteArray();
        } else {
            if (req.isProgressBarTask) pb.setMaxValue(len);
            req.response = new byte[len];
            int bytesread = 0, actual = 0;
            while ((bytesread < len) && (actual != -1)) {
                int toread = BUFFER_SIZE;
                if (toread >= len - bytesread) toread = len - bytesread;
                actual = is.read(req.response, bytesread, toread);
                if (actual > 0) bytesread += actual;
                if (req.isProgressBarTask) pb.setValueRePaint(bytesread);
            }
        }
        HelperRMSStoreMLibera.incDataBytesDownload(req.response.length);
        System.out.println("HTTP-Returned content:" + req.receivedDataContentType);
        if (req.receivedDataContentType.indexOf("text") == 0) {
            if (req.response != null) System.out.println(new String(req.response)); else if (req.fconnFileName != null) System.out.println(new String(HelperFileIO.getFileData(req.fconnFileName, null, pb)));
        }
        System.out.println("-------------------");
        if (!post) {
            String cc = hc.getHeaderField("Cache-Control");
            if (cc == null || cc.indexOf("no-cache") < 0) HelperApp.cacheAppBinary(req.url, req.response, req.receivedDataContentType);
        }
        if (req.c != null) req.c.remove();
        req.c = null;
        return req;
    }
