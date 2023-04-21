        @Override
        public String addSwaRefAttachment(DataHandler _data) {
            String contentType = _data.getContentType();
            if (!DescriptionDataSource.CONTENT_TYPE.equals(contentType)) {
                throw new IllegalArgumentException("Content type not handled: " + contentType);
            }
            InputStream in;
            try {
                in = _data.getInputStream();
            } catch (IOException e) {
                throw new IOExceptionWrapper(e);
            }
            if (in == null) {
                return "";
            }
            String entry = "" + counter++;
            String name = _data.getName();
            if (name != null) {
                entry += "_" + name;
            }
            String zipEntryName = REQ_DESCRIPTION_ENTRY_PREFIX + entry + REQ_DESCRIPTION_ENTRY_SUFFIX;
            ZipEntry zipEntry = new ZipEntry(zipEntryName);
            try {
                zout.putNextEntry(zipEntry);
                IoHelper.copy(in, zout);
                in.close();
                zout.closeEntry();
            } catch (IOException e) {
                throw new IOExceptionWrapper(e);
            }
            return zipEntryName;
        }
