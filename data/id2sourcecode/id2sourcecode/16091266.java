        private Double getMtGoxUsdBuy() {
            try {
                URL url = new URL("https://mtgox.com/api/0/data/ticker.php");
                HttpURLConnection connection;
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(60000);
                connection.setRequestMethod("GET");
                connection.connect();
                int status = connection.getResponseCode();
                if (status != 200) {
                    return null;
                }
                String ticker = StreamReader.readFully(connection.getInputStream());
                int buyIndex = ticker.indexOf("\"buy\":");
                int sellIndex = ticker.indexOf(",\"sell\"");
                if (buyIndex == -1 || sellIndex == -1 || buyIndex >= sellIndex) {
                    return null;
                }
                String buyString = ticker.substring(buyIndex + 6, sellIndex);
                return Double.parseDouble(buyString);
            } catch (Exception e) {
                return null;
            }
        }
