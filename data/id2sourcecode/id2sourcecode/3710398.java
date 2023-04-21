        @Override
        protected ByteArrayDataSource doInBackground() throws Exception {
            setMessage("Descargando " + uri.toString());
            byte[] fileBuffer = null;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(uri);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                fileBuffer = new byte[(int) entity.getContentLength()];
                InputStream is = entity.getContent();
                byte[] buffer = new byte[4096];
                int count = 0;
                int globalCount = 0;
                while ((count = is.read(buffer)) > 0) {
                    System.arraycopy(buffer, 0, fileBuffer, globalCount, count);
                    globalCount += count;
                    setProgress(globalCount * 100 / (int) entity.getContentLength());
                }
                is.close();
            }
            return new ByteArrayDataSource(fileBuffer);
        }
