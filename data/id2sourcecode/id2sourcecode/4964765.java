    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arena);
        activity = this;
        arenaroot = (LinearLayout) findViewById(R.id.arenaroot);
        HttpParams httpParams = new BasicHttpParams();
        HttpClient httpclient = new DefaultHttpClient(httpParams);
        HttpGet request;
        HttpGet request2;
        try {
            request = new HttpGet(new URI(registerUrl + "?playername=" + Engine.engine.getPlayer().getName() + "&level=" + Engine.engine.getPlayer().getLevel() + "&creatureclass=" + Engine.engine.getPlayer().getCreatureClass().toString() + "&gold=" + Engine.engine.getPlayer().getGold() + "&ip=" + getLocalIp() + "&" + json));
            request2 = new HttpGet(new URI(findOppponentUrl + "?" + json));
            try {
                httpclient.execute(request);
                HttpResponse result2 = httpclient.execute(request2);
                BufferedReader reader = new BufferedReader(new InputStreamReader(result2.getEntity().getContent()));
                String line = reader.readLine();
                try {
                    JSONObject jsonObject = new JSONObject(line);
                    JSONArray jsonArray = jsonObject.getJSONArray("return");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject player = jsonArray.getJSONObject(i);
                        ip = player.getString("ip");
                        if (ip != getLocalIp()) {
                            String creatureclass = player.getString("creatureclass");
                            String level = player.getString("level");
                            String playername = player.getString("playername");
                            TextView playernameText = new TextView(arenaroot.getContext());
                            playernameText.setText("Name: " + playername);
                            arenaroot.addView(playernameText);
                            TextView classText = new TextView(arenaroot.getContext());
                            classText.setText("Class: " + creatureclass);
                            arenaroot.addView(classText);
                            TextView levelText = new TextView(arenaroot.getContext());
                            levelText.setText("Level: " + level);
                            arenaroot.addView(levelText);
                            Button button = new Button(arenaroot.getContext());
                            button.setText("Challenge");
                            button.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    socket = null;
                                    dataOutputStream = null;
                                    try {
                                        socket = new Socket(InetAddress.getByName(ip), port);
                                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                                        dataOutputStream.writeByte(1);
                                        dataOutputStream.writeUTF(challengeRequest);
                                        dataOutputStream.flush();
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            arenaroot.addView(button);
                        }
                    }
                    serverSocket = new ServerSocket(port);
                    Thread listeningThread = new Thread(new ServerThread());
                    listeningThread.start();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        }
    }
