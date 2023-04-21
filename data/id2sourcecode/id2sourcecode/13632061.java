    public void run() {
        try {
            byte[] byteBuf = new byte[20000];
            boolean tryAgain = false;
            while (manager.notifyThreadReady(this, (int) (currentPosition - startPosition))) {
                do {
                    try {
                        tryAgain = false;
                        lastMeasuredPosition = currentPosition;
                        HttpResponse response = request.execute(currentPosition, endPosition - 1, 1000);
                        InputStream in = response.getInputStream();
                        int bytesRead = -2;
                        while (currentPosition < endPosition && !manager.isFinished() && (bytesRead = in.read(byteBuf)) >= 0) {
                            buffer.write(byteBuf, bytesRead, currentPosition);
                            currentPosition += bytesRead;
                        }
                        response.close();
                    } catch (SocketTimeoutException e) {
                        log.debug("Dl thread trying again after timeout startPosition: " + startPosition + " currentPosition: " + currentPosition);
                        tryAgain = true;
                    }
                } while (tryAgain);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
