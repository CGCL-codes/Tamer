    public Object parseMessage(String messageFromServer) {
        Object result = null;
        try {
            int bytes_read = messageFromServer.length();
            char[] ch_buffer = new char[bytes_read + 1];
            for (int i = 0; i < messageFromServer.length(); i++) {
                ch_buffer[i] = messageFromServer.charAt(i);
            }
            CharArrayReader car = new CharArrayReader(ch_buffer, 0, bytes_read);
            carIS = new InputSource(car);
            if (null != logStream) {
                String strDate = "" + (new Date());
                logStream.write(strDate, 0, strDate.length());
                logStream.newLine();
                logStream.write(ch_buffer, 0, bytes_read);
                logStream.newLine();
                logStream.flush();
            }
            XMLReader parser = XMLReaderFactory.createXMLReader(parserClass);
            parser.setContentHandler(contentHandler);
            parser.setErrorHandler(errorHandler);
            parser.setFeature("http://xml.org/sax/features/validation", false);
            debug("Calling .parse! car=" + car);
            parser.parse(carIS);
            debug("Done with .parse");
            switch(msgType) {
                case JabUtil.MESSAGE:
                    result = message;
                    break;
                case JabUtil.PRESENCE:
                    result = presence;
                    break;
                case JabUtil.IQ:
                    result = iq;
                    break;
            }
        } catch (IOException e) {
            System.err.println(e);
        } catch (SAXException e) {
            System.out.println("SAXException: " + e);
            System.out.println("Could not parse messageFromServer=" + messageFromServer);
            int eCol = locator.getColumnNumber();
            System.out.println("endElement locator.getColumnNumber()=" + eCol);
            System.out.println("endElement locator.getLineNumber()=" + locator.getLineNumber());
        } finally {
            return result;
        }
    }
