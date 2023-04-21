    public File getLegendGraphic(WMSStatus status, String layerName, ICancellable cancel) throws ServerErrorException, WMSException {
        URL request = null;
        try {
            request = new URL(buildGetLegendGraphicRequest(status, layerName));
            System.out.println(request);
            File f = Utilities.downloadFile(request, "wmsGetLegendGraphic", cancel);
            if (f == null) return null;
            if (Utilities.isTextFile(f)) {
                FileInputStream fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel();
                byte[] data = new byte[(int) fc.size()];
                ByteBuffer bb = ByteBuffer.wrap(data);
                fc.read(bb);
                WMSException wmsEx = null;
                String exceptionMessage = parseException(data);
                if (exceptionMessage == null) {
                    String error = new String(data);
                    int pos = error.indexOf("<?xml");
                    if (pos != -1) {
                        String xml = error.substring(pos, error.length());
                        exceptionMessage = parseException(xml.getBytes());
                    }
                    if (exceptionMessage == null) exceptionMessage = new String(data);
                }
                wmsEx = new WMSException(exceptionMessage);
                wmsEx.setWMSMessage(new String(data));
                Utilities.removeURL(request);
                throw wmsEx;
            }
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerErrorException();
        }
    }
