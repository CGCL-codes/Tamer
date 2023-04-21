    private void doSOAPRequest(String soapAction, StringBuffer envelope, XMLFilterImpl handler) throws RemoteException {
        try {
            if (debugIO) {
                debugOutputStream.println("SOAPURL: " + soapEndPoint);
                debugOutputStream.println("SoapAction: " + soapAction);
                debugOutputStream.println("SoapEnvelope:");
                debugOutputStream.println(envelope.toString());
            }
            URL url = new URL(soapEndPoint);
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            connect.setDoOutput(true);
            byte bytes[] = envelope.toString().getBytes();
            connect.setRequestProperty("SOAPAction", "\"" + soapAction + "\"");
            connect.setRequestProperty("content-type", "text/xml");
            connect.setRequestProperty("content-length", "" + bytes.length);
            OutputStream out = connect.getOutputStream();
            out.write(bytes);
            out.flush();
            int rc = connect.getResponseCode();
            InputStream stream = null;
            if (rc == HttpURLConnection.HTTP_OK) {
                stream = connect.getInputStream();
            } else if (rc == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                stream = connect.getErrorStream();
            }
            if (stream != null) {
                if (debugIO) {
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    int bt = stream.read();
                    while (bt != -1) {
                        bout.write(bt);
                        bt = stream.read();
                    }
                    debugOutputStream.println("Response:");
                    debugOutputStream.println(new String(bout.toByteArray()));
                    stream.close();
                    stream = new ByteArrayInputStream(bout.toByteArray());
                }
                String contentType = connect.getContentType();
                if (contentType.indexOf("text/xml") == -1) {
                    throw new RemoteException("Content-type not text/xml.  Instead, found " + contentType);
                }
                org.apache.xerces.parsers.SAXParser xmlreader = new org.apache.xerces.parsers.SAXParser();
                handler.setParent(xmlreader);
                xmlreader.setContentHandler(handler);
                xmlreader.parse(new InputSource(stream));
                stream.close();
            } else {
                throw new RemoteException("Communication error: " + rc + " " + connect.getResponseMessage());
            }
        } catch (RemoteException rex) {
            throw rex;
        } catch (Exception ex) {
            throw new RemoteException("Error doing soap stuff", ex);
        }
    }
