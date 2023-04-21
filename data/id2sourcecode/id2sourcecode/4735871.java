        private void processUPC() throws ChiPubLibBarcodeException {
            HttpURLConnection httpConnection = null;
            try {
                String upc = intent_.getStringExtra("SCAN_RESULT");
                URL url = new URL(UPC_LOOKUP_URL + "&access_token=" + upcKey_ + "&upc=" + upc);
                httpConnection = (HttpURLConnection) url.openConnection();
                int responseCode = httpConnection.getResponseCode();
                HashSet uniqueResuts = new HashSet<String>();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    CSVParser csvParser = new CSVParser(reader);
                    int descCol = getDescCol(csvParser.getLine());
                    String[] line;
                    String cleanStr;
                    while ((line = csvParser.getLine()) != null) {
                        cleanStr = line[descCol].replaceAll(CLEAN_UPC_INFO_REGEXP, "");
                        if (uniqueResuts.add(cleanStr)) {
                            results_.add(cleanStr);
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Resources r = context_.getResources();
                errorMsg_ = r.getString(R.string.upc_error_msg) + r.getString(R.string.unknown_error_msg);
                throw new ChiPubLibBarcodeException(errorMsg_);
            } catch (IOException e) {
                Resources r = context_.getResources();
                errorMsg_ = r.getString(R.string.upc_error_msg) + r.getString(R.string.network_error_msg);
                throw new ChiPubLibBarcodeException(errorMsg_);
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }
        }
