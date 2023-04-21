    public File getCoverage(WCSStatus status, ICancellable cancel) throws ServerErrorException, WCSException {
        URL request = null;
        try {
            request = new URL(buildCoverageRequest(status));
            File f = Utilities.downloadFile(request, "wcsGetCoverage", cancel);
            if (f != null && Utilities.isTextFile(f)) {
                FileInputStream fis = new FileInputStream(f);
                FileChannel fc = fis.getChannel();
                byte[] data = new byte[(int) fc.size()];
                ByteBuffer bb = ByteBuffer.wrap(data);
                fc.read(bb);
                WCSException wcsEx = null;
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
                wcsEx = new WCSException(exceptionMessage);
                wcsEx.setWCSMessage(new String(data));
                Utilities.removeURL(request);
                throw wcsEx;
            }
            return f;
        } catch (IOException e) {
            e.printStackTrace();
            throw new ServerErrorException();
        }
    }
