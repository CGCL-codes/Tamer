        @Override
        public void exec() {
            try {
                if (mData[mI] == null) {
                    String url = mPrefix + mI + mPostfix;
                    InputStream is = new URL(url).openStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[16384];
                    int count;
                    while ((count = is.read(buffer)) > 0) out.write(buffer, 0, count);
                    out.flush();
                    is.close();
                    mData[mI] = out.toByteArray();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
