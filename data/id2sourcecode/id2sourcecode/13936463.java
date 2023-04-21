    protected synchronized String submittRequest(String request) throws NSClient4JException {
        byte[] buffer = new byte[1024];
        baos.reset();
        String result = null;
        if (!inited) {
            initSocket();
        }
        try {
            socket.setSoTimeout(socketTimeout);
            os.write(request.getBytes());
            os.flush();
            while (true) {
                int read = bis.read(buffer);
                if (read > 0) {
                    baos.write(buffer, 0, read);
                    break;
                } else {
                    break;
                }
            }
            result = baos.toString();
            testResult(result);
            return result;
        } catch (Exception ex) {
            inited = false;
            throw new NSClient4JException(ex.getMessage(), ex);
        }
    }
