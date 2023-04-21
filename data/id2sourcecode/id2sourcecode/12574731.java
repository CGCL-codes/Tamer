    public StreamDescriptor getStreamDescriptor(URL dataSetID) throws DasException {
        try {
            String dsdf = dataSetID.getQuery().split("&")[0];
            URL url = new URL("http", host, port, path + "?server=dsdf&dataset=" + dsdf);
            logger.log(Level.INFO, "connecting to {0}", url);
            URLConnection connection = url.openConnection();
            connection.connect();
            String contentType = connection.getContentType();
            String[] s1 = contentType.split(";");
            contentType = s1[0];
            if (contentType.equalsIgnoreCase("text/plain")) {
                PushbackReader reader = new PushbackReader(new InputStreamReader(connection.getInputStream()), 4);
                char[] four = new char[4];
                reader.read(four);
                if (new String(four).equals("[00]")) {
                    logger.info("response is a das2Stream");
                    reader.skip(6);
                    Document header = StreamDescriptor.parseHeader(reader);
                    Element root = header.getDocumentElement();
                    if (root.getTagName().equals("stream")) {
                        return new StreamDescriptor(root);
                    } else if (root.getTagName().equals("exception")) {
                        logger.info("response is an exception");
                        String type = root.getAttribute("type");
                        StreamException se = new StreamException("stream exception: " + type);
                        throw new DasException("stream exception: " + type, se);
                    } else if (root.getTagName().equals("")) {
                        throw new DasStreamFormatException();
                    } else {
                        throw new DasStreamFormatException();
                    }
                } else {
                    logger.info("response is a legacy descriptor");
                    reader.unread(four);
                    BufferedReader in = new BufferedReader(reader);
                    StreamDescriptor result = StreamDescriptor.createLegacyDescriptor(in);
                    return result;
                }
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder message = new StringBuilder();
                for (String line = in.readLine(); line != null; line = in.readLine()) {
                    message.append(line).append('\n');
                }
                throw new IOException(message.toString());
            }
        } catch (MalformedURLException e) {
            throw new DataSetDescriptorNotAvailableException("malformed URL");
        } catch (FileNotFoundException e) {
            throw new DasServerNotFoundException(e.getMessage());
        } catch (IOException e) {
            throw new DasIOException(e.toString());
        }
    }
