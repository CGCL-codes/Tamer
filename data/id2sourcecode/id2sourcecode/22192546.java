    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        loadPrefs();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        DDAO = this;
        chatbot.addLogger(this);
        chatbot.addListener(this);
        modules.add(new DDAOCoreModule());
        sv = (ScrollView) findViewById(R.id.scrollview);
        svl = (LinearLayout) findViewById(R.id.scrollviewlayout);
        in = (EditText) findViewById(R.id.input);
        in.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (sv.getHeight() + sv.getScrollY() >= svl.getHeight()) {
                    autoscroll = true;
                }
                in.onTouchEvent(event);
                if (autoscroll) autoScroll(sv, svl);
                return true;
            }
        });
        spin = (Spinner) findViewById(R.id.spinner);
        adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        sv.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                sv.onTouchEvent(event);
                autoscroll = false;
                return true;
            }
        });
        in.setOnKeyListener(new OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    autoscroll = true;
                    autoScroll(sv, svl);
                    String input = in.getText().toString();
                    in.setText("");
                    if (input.startsWith("/")) {
                        String[] temp = input.substring(1).split(" ");
                        String[] args = new String[temp.length - 1];
                        for (int i = 1; i < temp.length; i++) {
                            args[i - 1] = temp[i];
                        }
                        parseCommand(temp[0], args);
                        String out = "";
                        for (String part : args) {
                            out = out + part + ", ";
                        }
                    } else {
                        try {
                            String channel = getChannel();
                            if (channels.get(channel).isMute()) {
                                println(chatbot, "This channel is currently muted");
                            } else if (channels.get(channel).getID().length < 5) {
                                chatbot.sendPrivateChannelMessage(channel, input);
                            } else {
                                chatbot.sendChannelMessage(channel, input);
                            }
                        } catch (Exception e) {
                            exception(chatbot, e);
                        }
                    }
                    return true;
                }
                return false;
            }
        });
        if (autoscroll) autoScroll(sv, svl);
        try {
            thread = new Thread(this);
            thread.start();
        } catch (Exception e) {
            Log.e("System.err", "Failed to create thread", e);
        }
    }
