    protected void buildDialog() {
        final EditText gameresultUsernameValueEditText = (EditText) findViewById(R.id.gameresultUsernameValue);
        final Button gameresultUsernamePostBtn = (Button) findViewById(R.id.gameresultUsernamePostBtn);
        gameresultUsernamePostBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String value = gameresultUsernameValueEditText.getText().toString();
                if (value == null || value.trim().equals("")) {
                    Toast.makeText(getContext(), res.getString(R.string.message_username_empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                value = value.trim();
                int valueLength = value.length();
                if (valueLength > 20) {
                    Toast.makeText(getContext(), res.getString(R.string.message_username_extendmaxlength), Toast.LENGTH_SHORT).show();
                    return;
                }
                value = value.substring(0, valueLength > 20 ? 20 : valueLength);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = format.format(new Date());
                ScoreItemModel itemModel = new ScoreItemModel(value, "" + gameScore, dateStr, true);
                localScoreGroupModel.addItem(itemModel);
                SharedPreferences preferences = getContext().getSharedPreferences(Constants.PROPERTIES, Context.MODE_WORLD_WRITEABLE);
                SharedPreferences.Editor editor = preferences.edit();
                for (int i = 0; i < localScoreGroupModel.getItemCount(); i++) {
                    ScoreItemModel localScoreItemModel = localScoreGroupModel.getItem(i);
                    editor.putString(Constants.PROPERTIES_GAMEBEST_USERNAME + i, localScoreItemModel.getUsername());
                    editor.putString(Constants.PROPERTIES_GAMEBEST_SCORE + i, localScoreItemModel.getScore());
                    editor.putString(Constants.PROPERTIES_GAMEBEST_DATE + i, localScoreItemModel.getDate());
                }
                editor.commit();
                if (itemModel.equals(localScoreGroupModel.getItem(0))) {
                    String httpUrl = Constants.SERVER_URL + "/rollingcard.php?op=addnewscore&username=" + itemModel.getUsername() + "&score=" + itemModel.getScore();
                    HttpGet request = new HttpGet(httpUrl);
                    HttpClient httpClient = new DefaultHttpClient();
                    try {
                        HttpResponse response = httpClient.execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            Toast.makeText(getContext(), res.getString(R.string.message_addnewscore_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), res.getString(R.string.message_networkerror), Toast.LENGTH_SHORT).show();
                        }
                    } catch (ClientProtocolException e) {
                        Toast.makeText(getContext(), res.getString(R.string.message_networkerror), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    } catch (IOException e) {
                        Toast.makeText(getContext(), res.getString(R.string.message_networkerror), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
                renderLayoutById(R.id.gameresultScoreLayout);
            }
        });
        final Button gameresultUsernameCancelBtn = (Button) findViewById(R.id.gameresultUsernameCancelBtn);
        gameresultUsernameCancelBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                renderLayoutById(R.id.gameresultScoreLayout);
            }
        });
        final Button gameresultRestartBtn = (Button) findViewById(R.id.gameresultRestartBtn);
        gameresultRestartBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                result = Constants.RESULT_GAME_RESTART;
                dismiss();
            }
        });
        final Button gameresultCloseBtn = (Button) findViewById(R.id.gameresultExitBtn);
        gameresultCloseBtn.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                result = Constants.RESULT_GAME_CLOSE;
                dismiss();
            }
        });
    }
