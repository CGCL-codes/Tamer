        @Override
        protected String doInBackground(final String... args) {
            String result = null;
            DefaultHttpClient client = new DefaultHttpClient();
            HttpGet httpMethod = new HttpGet(args[0]);
            try {
                HttpResponse response = client.execute(httpMethod);
                HttpEntity entity = response.getEntity();
                result = StringUtils.inputStreamToString(entity.getContent());
            } catch (IOException e) {
                Log.e(Constants.LOG_TAG, e.getMessage(), e);
            }
            return result;
        }
