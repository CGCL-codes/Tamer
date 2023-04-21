    private void appendStream(Fountain stream, KXmlSerializer serializer, Hash tracker) throws JSExn {
        try {
            serializer.startTag("", "value");
            serializer.startTag("", "base64");
            InputStream is = stream.getInputStream();
            byte[] buf = new byte[54];
            while (true) {
                int numread = is.read(buf, 0, 54);
                if (numread == -1) {
                    break;
                }
                byte[] writebuf = buf;
                if (numread < buf.length) {
                    writebuf = new byte[numread];
                    System.arraycopy(buf, 0, writebuf, 0, numread);
                }
                serializer.text(" " + Encode.toBase64(writebuf));
            }
            serializer.endTag("", "base64");
            serializer.endTag("", "value");
        } catch (IOException e) {
            Log.warn(XmlRpcMarshaller.class, "caught IOException while attempting to send a ByteStream via XML-RPC");
            Log.warn(XmlRpcMarshaller.class, e);
            throw new JSExn("caught IOException while attempting to send a ByteStream via XML-RPC");
        }
    }
