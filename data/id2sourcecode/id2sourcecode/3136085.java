    private static boolean writeResourceByPathAndStream(ServletResponse response, String path, InputStream inputStream) throws IOException {
        if (inputStream != null) {
            OutputStream outputStream = response.getOutputStream();
            try {
                setupContentType(path, response);
                int size = 0;
                byte buffer[] = new byte[65536];
                while (true) {
                    int readCount = inputStream.read(buffer);
                    if (readCount >= 0) {
                        outputStream.write(buffer, 0, readCount);
                        size += readCount;
                    } else {
                        break;
                    }
                }
                response.setContentLength(size);
            } finally {
                outputStream.close();
                inputStream.close();
            }
            return true;
        } else {
            return false;
        }
    }
