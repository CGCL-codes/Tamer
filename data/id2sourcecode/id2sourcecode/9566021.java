        public boolean isReadOnly() {
            if (readOnly == null) {
                try {
                    OutputStream stream = url.openConnection().getOutputStream();
                    readOnly = Boolean.FALSE;
                    stream.close();
                } catch (UnknownServiceException e) {
                    readOnly = Boolean.TRUE;
                } catch (IOException e) {
                }
            }
            return readOnly;
        }
