    void writeStreamResult(Node node, StreamResult sr, int outputMethod, String encoding) throws IOException {
        OutputStream out = null;
        boolean created = false;
        try {
            out = sr.getOutputStream();
            if (out == null) {
                Writer writer = sr.getWriter();
                if (writer != null) out = new WriterOutputStream(writer);
            }
            if (out == null) {
                String systemId = sr.getSystemId();
                try {
                    URL url = new URL(systemId);
                    URLConnection connection = url.openConnection();
                    connection.setDoInput(false);
                    connection.setDoOutput(true);
                    out = connection.getOutputStream();
                } catch (MalformedURLException e) {
                    out = new FileOutputStream(systemId);
                } catch (UnknownServiceException e) {
                    URL url = new URL(systemId);
                    out = new FileOutputStream(url.getPath());
                }
                created = true;
            }
            out = new BufferedOutputStream(out);
            StreamSerializer serializer = new StreamSerializer(outputMethod, encoding, null);
            if (stylesheet != null) {
                Collection celem = stylesheet.outputCdataSectionElements;
                serializer.setCdataSectionElements(celem);
            }
            serializer.serialize(node, out);
            out.flush();
        } finally {
            try {
                if (out != null && created) out.close();
            } catch (IOException e) {
            }
        }
    }
