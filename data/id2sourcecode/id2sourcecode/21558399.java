        public void parseResponse(InputStream input) throws IppException, IOException {
            DataInputStream stream = new DataInputStream(input);
            short version = stream.readShort();
            status_code = stream.readShort();
            request_id = stream.readInt();
            if (VERSION != version) throw new IppException("Version mismatch - " + "implementation does not support other versions than IPP 1.1");
            logger.log(Component.IPP, "Statuscode: " + Integer.toHexString(status_code) + " Request-ID: " + request_id);
            byte tag = 0;
            boolean proceed = true;
            HashMap tmp;
            while (proceed) {
                if (tag == 0) tag = stream.readByte();
                logger.log(Component.IPP, "DelimiterTag: " + Integer.toHexString(tag));
                switch(tag) {
                    case IppDelimiterTag.END_OF_ATTRIBUTES_TAG:
                        proceed = false;
                        break;
                    case IppDelimiterTag.OPERATION_ATTRIBUTES_TAG:
                        tmp = new HashMap();
                        tag = parseAttributes(tmp, stream);
                        operationAttributes.add(tmp);
                        break;
                    case IppDelimiterTag.JOB_ATTRIBUTES_TAG:
                        tmp = new HashMap();
                        tag = parseAttributes(tmp, stream);
                        jobAttributes.add(tmp);
                        break;
                    case IppDelimiterTag.PRINTER_ATTRIBUTES_TAG:
                        tmp = new HashMap();
                        tag = parseAttributes(tmp, stream);
                        printerAttributes.add(tmp);
                        break;
                    case IppDelimiterTag.UNSUPPORTED_ATTRIBUTES_TAG:
                        System.out.println("Called");
                        tmp = new HashMap();
                        tag = parseAttributes(tmp, stream);
                        unsupportedAttributes.add(tmp);
                        break;
                    default:
                        throw new IppException("Unknown tag with value " + Integer.toHexString(tag) + " occured.");
                }
            }
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte[] readbuf = new byte[2048];
            int len = 0;
            while ((len = stream.read(readbuf)) > 0) byteStream.write(readbuf, 0, len);
            byteStream.flush();
            data = byteStream.toByteArray();
        }
