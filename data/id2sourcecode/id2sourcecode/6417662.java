        @Override
        public String getValue() throws FailedToGetValueOfAddress {
            try {
                final ByteArrayOutputStream bigBuffer = new ByteArrayOutputStream();
                URLConnection connection = url.openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                final InputStream inputStream = connection.getInputStream();
                try {
                    byte[] smallBuffer = new byte[1024 * 32];
                    int readedBytes = inputStream.read(smallBuffer);
                    while (readedBytes != -1) {
                        bigBuffer.write(smallBuffer, 0, readedBytes);
                        readedBytes = inputStream.read(smallBuffer);
                    }
                } finally {
                    inputStream.close();
                }
                final String text = bigBuffer.toString();
                Matcher matcher = pattern.matcher(text);
                if (!matcher.find()) {
                    throw new IOException("Obtained content does not match pattern");
                }
                if (group < 0 || group > matcher.groupCount()) {
                    final String message = String.format("The specified group %d is not in the interval [0,%d].", group, matcher.groupCount());
                    throw new IOException(message);
                }
                return matcher.group(group);
            } catch (IOException e) {
                throw new FailedToGetValueOfAddress(e);
            }
        }
