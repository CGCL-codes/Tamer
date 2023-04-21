        protected Object parse(String path, URL url, Object extra) throws Exception {
            InputStream is = url.openStream();
            if (is != null) is = new BufferedInputStream(is);
            try {
                return parse0(is, Interpreter.getContentType(url.getPath()));
            } catch (Exception ex) {
                if (log.debugable()) log.realCauseBriefly("Failed to parse " + url, ex); else log.error("Failed to parse " + url + "\nCause: " + Exceptions.getMessage(ex));
                return null;
            } finally {
                Files.close(is);
            }
        }
