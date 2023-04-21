        @Override
        protected String doInBackground(String... params) {
            try {
                HttpUriRequest request = genFusiontablesQuery(params[0]);
                Log.d(LOG_TAG, "Fetching: " + params[0]);
                HttpResponse response = requestHelper.execute(request);
                ByteArrayOutputStream outstream = new ByteArrayOutputStream();
                response.getEntity().writeTo(outstream);
                Log.d(LOG_TAG, "Response: " + response.getStatusLine().toString());
                return outstream.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }
