        @Override
        protected StockList doInBackground(String... params) {
            try {
                URL url = new URL("http://10.0.2.2:8080/StockServerWeb/StockQuotesServlet");
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("POST");
                c.connect();
                c.getInputStream();
                StockList sL = StockList.parseFrom(c.getInputStream());
                return sL;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IDException");
                e.printStackTrace();
            }
            return null;
        }
