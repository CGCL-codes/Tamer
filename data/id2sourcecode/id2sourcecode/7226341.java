    public static long getFile(long fid, PeerserverHost host, FileOutputStream fos) {
        URL url = null;
        Service service = new Service();
        Call call = null;
        try {
            url = new URL("https://" + host.getIp() + ":" + host.getPort() + "/services/FileWS");
            call = (Call) service.createCall();
            QName qnameAttachment = new QName("urn:FileWS", "DataHandler");
            call.registerTypeMapping(DataHandler.class, qnameAttachment, JAFDataHandlerSerializerFactory.class, JAFDataHandlerDeserializerFactory.class);
        } catch (Exception e) {
            LOG.trace(" ", e);
        }
        DataHandler result;
        try {
            call.setTargetEndpointAddress(url);
            call.setOperationName(new QName("FileWS", "getFile"));
            call.addParameter("arg1", org.apache.axis.encoding.XMLType.XSD_LONG, ParameterMode.IN);
            result = (DataHandler) call.invoke(new Object[] { new Long(fid) });
            if (result != null) {
                InputStream in_stream = result.getInputStream();
                byte[] bytes = new byte[4096];
                int read = in_stream.read(bytes);
                while (read != -1) {
                    fos.write(bytes, 0, read);
                    read = in_stream.read(bytes);
                }
                fos.flush();
                fos.close();
                in_stream.close();
            }
            return 0l;
        } catch (Exception e) {
            LOG.trace(" ", e);
        }
        return 0l;
    }
