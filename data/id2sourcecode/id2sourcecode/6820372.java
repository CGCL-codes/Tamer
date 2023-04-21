    protected Part readTillFound(final String[] id) throws org.apache.axis.AxisFault {
        if (boundaryDelimitedStream == null) {
            return null;
        }
        Part ret = null;
        try {
            if (soapStreamBDS == boundaryDelimitedStream) {
                if (!eos) {
                    java.io.ByteArrayOutputStream soapdata = new java.io.ByteArrayOutputStream(1024 * 8);
                    byte[] buf = new byte[1024 * 16];
                    int byteread = 0;
                    do {
                        byteread = soapStream.read(buf);
                        if (byteread > 0) {
                            soapdata.write(buf, 0, byteread);
                        }
                    } while (byteread > -1);
                    soapdata.close();
                    soapStream = new java.io.ByteArrayInputStream(soapdata.toByteArray());
                }
                boundaryDelimitedStream = boundaryDelimitedStream.getNextStream();
            }
            if (null != boundaryDelimitedStream) {
                do {
                    String contentType = null;
                    String contentId = null;
                    String contentTransferEncoding = null;
                    String contentLocation = null;
                    javax.mail.internet.InternetHeaders headers = new javax.mail.internet.InternetHeaders(boundaryDelimitedStream);
                    contentId = headers.getHeader("Content-Id", null);
                    if (contentId != null) {
                        contentId = contentId.trim();
                        if (contentId.startsWith("<")) {
                            contentId = contentId.substring(1);
                        }
                        if (contentId.endsWith(">")) {
                            contentId = contentId.substring(0, contentId.length() - 1);
                        }
                        contentId = contentId.trim();
                    }
                    contentType = headers.getHeader(HTTPConstants.HEADER_CONTENT_TYPE, null);
                    if (contentType != null) {
                        contentType = contentType.trim();
                    }
                    contentLocation = headers.getHeader(HTTPConstants.HEADER_CONTENT_LOCATION, null);
                    if (contentLocation != null) {
                        contentLocation = contentLocation.trim();
                    }
                    contentTransferEncoding = headers.getHeader(HTTPConstants.HEADER_CONTENT_TRANSFER_ENCODING, null);
                    if (contentTransferEncoding != null) {
                        contentTransferEncoding = contentTransferEncoding.trim();
                    }
                    java.io.InputStream decodedStream = boundaryDelimitedStream;
                    if ((contentTransferEncoding != null) && (0 != contentTransferEncoding.length())) {
                        decodedStream = MimeUtility.decode(decodedStream, contentTransferEncoding);
                    }
                    ManagedMemoryDataSource source = new ManagedMemoryDataSource(decodedStream, ManagedMemoryDataSource.MAX_MEMORY_DISK_CACHED, contentType, true);
                    DataHandler dh = new DataHandler(source);
                    AttachmentPart ap = new AttachmentPart(dh);
                    if (contentId != null) {
                        ap.setMimeHeader(HTTPConstants.HEADER_CONTENT_ID, contentId);
                    }
                    if (contentLocation != null) {
                        ap.setMimeHeader(HTTPConstants.HEADER_CONTENT_LOCATION, contentLocation);
                    }
                    for (java.util.Enumeration en = headers.getNonMatchingHeaders(new String[] { HTTPConstants.HEADER_CONTENT_ID, HTTPConstants.HEADER_CONTENT_LOCATION, HTTPConstants.HEADER_CONTENT_TYPE }); en.hasMoreElements(); ) {
                        javax.mail.Header header = (javax.mail.Header) en.nextElement();
                        String name = header.getName();
                        String value = header.getValue();
                        if ((name != null) && (value != null)) {
                            name = name.trim();
                            if (name.length() != 0) {
                                ap.addMimeHeader(name, value);
                            }
                        }
                    }
                    addPart(contentId, contentLocation, ap);
                    for (int i = id.length - 1; (ret == null) && (i > -1); --i) {
                        if ((contentId != null) && id[i].equals(contentId)) {
                            ret = ap;
                        } else if ((contentLocation != null) && id[i].equals(contentLocation)) {
                            ret = ap;
                        }
                    }
                    boundaryDelimitedStream = boundaryDelimitedStream.getNextStream();
                } while ((null == ret) && (null != boundaryDelimitedStream));
            }
        } catch (Exception e) {
            throw org.apache.axis.AxisFault.makeFault(e);
        }
        return ret;
    }
