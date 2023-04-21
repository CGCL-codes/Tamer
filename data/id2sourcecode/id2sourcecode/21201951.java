    @Override
    protected void processResponseBody(BotContext context, HttpMethod method) {
        Header filename = method.getResponseHeader(HEADER_CONTENT_DISPOSITION);
        String filenameStr = filename.getValue().split("=")[1];
        if (context.isVerbose()) {
            logger.info("File downloaded: " + filenameStr);
        }
        if (context.isUnzip()) {
            filenameStr = filenameStr.substring(0, filenameStr.length() - 3);
        }
        File f = null;
        if (context.getOutputDiretory() != null) {
            f = new File(context.getOutputDiretory(), filenameStr);
        } else {
            f = new File(filenameStr);
        }
        OutputStream out = null;
        try {
            if (!f.createNewFile()) {
                throw new Exception("Cannot create file: " + f.getPath());
            }
            out = new BufferedOutputStream(new FileOutputStream(f));
            InputStream in = null;
            if (context.isUnzip()) {
                in = new GZIPInputStream(method.getResponseBodyAsStream());
            } else {
                in = method.getResponseBodyAsStream();
            }
            byte[] buffer = new byte[1024];
            int readSize;
            while ((readSize = in.read(buffer)) != -1) {
                out.write(buffer, 0, readSize);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
            index++;
            context.setContextAttribute("reportDatesIndex", Integer.toString(index));
        }
    }
