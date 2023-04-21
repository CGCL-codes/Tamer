    public T invoke() throws InvokeException {
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) new URL(url).openConnection();
            setHeader(con);
            setCookies(con);
            int streamSize = 0;
            for (DataSource ds : dataSource) {
                streamSize += (STREAM_SIZE + ds.getName().getBytes().length + ds.getSize());
            }
            int streamLength = streamSize + bOutput.size() + END.length;
            if (streamLength < 0) {
                con.setChunkedStreamingMode(BUFFER_SIZE);
            } else {
                con.setFixedLengthStreamingMode(streamLength);
            }
            con.connect();
            OutputStream out = con.getOutputStream();
            bOutput.writeTo(out);
            byte[] b = new byte[BUFFER_SIZE];
            int len = 0;
            for (DataSource ds : dataSource) {
                out.write(startStream(DATASOURCE_NAME, ds.getName()));
                if (observer != null) {
                    observer.reset();
                    observer.setDataSource(ds);
                }
                InputStream in = ds.getInputStream();
                while ((len = in.read(b)) != -1) {
                    if (len > 0) {
                        out.write(b, 0, len);
                        out.flush();
                        if (observer != null) observer.upload(len);
                    }
                }
                in.close();
                out.write("\r\n".getBytes());
            }
            out.write(END);
            out.close();
            log.debug("{} : {} : {}", new Object[] { con.getResponseCode(), con.getResponseMessage(), con.getContentType() });
            if (HttpURLConnection.HTTP_OK == con.getResponseCode()) {
                return handleInputStream(con);
            } else {
                log.error(con.getResponseCode() + " : " + System.getProperty("line.separator"));
                if (HttpURLConnection.HTTP_INTERNAL_ERROR == con.getResponseCode()) {
                    log.error(new String(Base64.decode(con.getResponseMessage())));
                } else {
                    log.error(con.getResponseMessage());
                }
                return null;
            }
        } catch (IOException e) {
            throw new InvokeException(e);
        } finally {
            if (con != null) {
                storeCookies(con);
            }
            bOutput.reset();
            dataSource.clear();
        }
    }
