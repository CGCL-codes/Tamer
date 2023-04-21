    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        dm = new DataManipulator(this);
        comanda = dm.selectAllOrders();
        HttpPost request = new HttpPost(SERVICE_URI + "/json/addorder");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        String not = new String(" ");
        String[] ComandaDeTrimis = comanda.get(0);
        String ClientDeTrimis = ComandaDeTrimis[0];
        String ProdusDeTrimis = ComandaDeTrimis[1];
        String NumarBucati = ComandaDeTrimis[2];
        String Discount = ComandaDeTrimis[3];
        try {
            JSONStringer vehicle = new JSONStringer().object().key("od").object().key("ClientName").value(ClientDeTrimis).key("ProductName").value(ProdusDeTrimis).key("PiecesNumber").value(NumarBucati).key("DiscountNumber").value(Discount).key("LineOrderId").value("1000").endObject().endObject();
            StringEntity entity = new StringEntity(vehicle.toString());
            Toast.makeText(this, vehicle.toString() + "\n", Toast.LENGTH_LONG).show();
            request.setEntity(entity);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            Toast.makeText(this, response.getStatusLine().getStatusCode() + "\n", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            not = "NOT ";
        }
        Toast.makeText(this, not + " OK ! " + "\n", Toast.LENGTH_LONG).show();
    }
