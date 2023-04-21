    private void queryService(final GDS gds, final IscSvcHandle handle, String outputFilename) throws Exception, IOException {
        final ServiceRequestBuffer serviceRequestBuffer = gds.createServiceRequestBuffer(ISCConstants.isc_info_svc_to_eof);
        final byte[] buffer = new byte[1024];
        boolean finished = false;
        final FileOutputStream file = new FileOutputStream(outputFilename);
        while (finished == false) {
            gds.iscServiceQuery(handle, null, serviceRequestBuffer, buffer);
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buffer);
            final byte firstByte = (byte) byteArrayInputStream.read();
            int numberOfBytes = (short) ((byteArrayInputStream.read() << 0) + (byteArrayInputStream.read() << 8));
            if (numberOfBytes == 0) {
                if (byteArrayInputStream.read() != ISCConstants.isc_info_end) throw new Exception("Expect ISCConstants.isc_info_end here");
                finished = true;
            } else {
                for (; numberOfBytes >= 0; numberOfBytes--) file.write(byteArrayInputStream.read());
            }
            file.flush();
        }
    }
