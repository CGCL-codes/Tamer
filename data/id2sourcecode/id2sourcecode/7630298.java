    String getHtml(int maxLength, String truncationMessage) throws IOException {
        if (maxLength >= size) {
            try {
                String retVal = (String) htmlHandler.getContent();
                return retVal;
            } catch (UnsupportedEncodingException uee) {
                InputStream is = htmlHandler.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int b;
                while ((b = is.read()) != -1) bos.write(b);
                byte[] barray = bos.toByteArray();
                return new String(barray, Pooka.getProperty("Pooka.defaultCharset", "iso-8859-1"));
            }
        } else {
            int written = 0;
            InputStream decodedIS = null;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            decodedIS = getInputStream();
            int b = 0;
            byte[] buf = new byte[16384];
            b = decodedIS.read(buf);
            while (b != -1 && written < maxLength) {
                if (b <= (maxLength - written)) {
                    outStream.write(buf, 0, b);
                    written = written + b;
                } else {
                    outStream.write(buf, 0, (maxLength - written));
                    written = maxLength;
                }
                b = decodedIS.read(buf);
            }
            byte[] barray = outStream.toByteArray();
            String content;
            try {
                content = new String(barray, Pooka.getProperty("Pooka.defaultCharset", "iso-8859-1"));
            } catch (UnsupportedEncodingException uee) {
                content = new String(barray, Pooka.getProperty("Pooka.defaultCharset", "iso-8859-1"));
            }
            return content + "\n" + truncationMessage + "\n";
        }
    }
