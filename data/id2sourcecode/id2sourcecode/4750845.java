    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dm = new DataManipulator(this);
        ordersToSend = dm.selectAllOrders();
        String isNot = new String(" ");
        HttpPost request = new HttpPost(SERVICE_URI + "/json/addorder");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        for (String[] orderToSend : ordersToSend) {
            String idLineToSend = orderToSend[0];
            String clientToSend = orderToSend[1];
            String productToSend = orderToSend[2];
            String piecesToSend = orderToSend[3];
            String discountToSend = orderToSend[4];
            try {
                JSONStringer jsonOrderToSend = new JSONStringer().object().key("od").object().key("ClientName").value(clientToSend).key("ProductName").value(productToSend).key("PiecesNumber").value(piecesToSend).key("DiscountNumber").value(discountToSend).key("LineOrderId").value(idLineToSend).endObject().endObject();
                StringEntity entity = new StringEntity(jsonOrderToSend.toString());
                Log.d(" OrderLine  ", jsonOrderToSend.toString() + "\n");
                request.setEntity(entity);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Log.d("WebInvoke", " OK if 200 = " + response.getStatusLine().getStatusCode());
            } catch (Exception e) {
                isNot = "NOT ";
            }
        }
        Toast.makeText(this, isNot + " OK ! " + "\n", Toast.LENGTH_LONG).show();
    }
