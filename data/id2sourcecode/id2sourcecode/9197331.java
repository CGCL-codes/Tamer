    public void showProgressDialogWaitFeaturesGame() {
        dialog = new Dialog(this, R.style.PauseGameDialogTheme);
        dialog.setContentView(R.layout.feature_view);
        Button buttonRed = (Button) dialog.findViewById(R.id.buttonRed);
        buttonRed.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (threadCommunication == null) {
                    nameAndColor.add(0, namePlayer);
                    nameAndColor.add(1, RED);
                    nameAndColor.add(2, namePlayer);
                    if (!checksStartGame()) {
                        checksStartGame();
                    }
                    showProgressDialogWaitCompleteFeatureGame();
                } else {
                    showProgressDialogWaitCompleteFeatureGame();
                    try {
                        threadCommunication.os.writeUTF("featureClient/" + namePlayer + "/" + RED + "/" + adapter.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button buttonBlue = (Button) dialog.findViewById(R.id.buttonBlue);
        buttonBlue.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (threadCommunication == null) {
                    nameAndColor.add(0, namePlayer);
                    nameAndColor.add(1, BLUE);
                    nameAndColor.add(2, namePlayer);
                    if (!checksStartGame()) {
                        checksStartGame();
                    }
                    showProgressDialogWaitCompleteFeatureGame();
                } else {
                    showProgressDialogWaitCompleteFeatureGame();
                    try {
                        threadCommunication.os.writeUTF("featureClient/" + namePlayer + "/" + BLUE + "/" + adapter.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button buttonYellow = (Button) dialog.findViewById(R.id.buttonYellow);
        buttonYellow.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (threadCommunication == null) {
                    nameAndColor.add(0, namePlayer);
                    nameAndColor.add(1, YELLOW);
                    nameAndColor.add(2, namePlayer);
                    if (!checksStartGame()) {
                        checksStartGame();
                    }
                    showProgressDialogWaitCompleteFeatureGame();
                } else {
                    showProgressDialogWaitCompleteFeatureGame();
                    try {
                        threadCommunication.os.writeUTF("featureClient/" + namePlayer + "/" + YELLOW + "/" + adapter.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Button buttonGreen = (Button) dialog.findViewById(R.id.buttonGreen);
        buttonGreen.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (threadCommunication == null) {
                    nameAndColor.add(0, namePlayer);
                    nameAndColor.add(1, GREEN);
                    nameAndColor.add(2, namePlayer);
                    if (!checksStartGame()) {
                        checksStartGame();
                    }
                    showProgressDialogWaitCompleteFeatureGame();
                } else {
                    showProgressDialogWaitCompleteFeatureGame();
                    try {
                        threadCommunication.os.writeUTF("featureClient/" + namePlayer + "/" + GREEN + "/" + adapter.getName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        dialog.show();
    }
