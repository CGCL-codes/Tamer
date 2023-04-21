        @Override
        protected Boolean doInBackground(URL... arg0) {
            URLConnection urlConnection;
            try {
                urlConnection = arg0[0].openConnection();
                BufferedInputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String result = br.readLine();
                return result.equals("1");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Boolean.FALSE;
        }
