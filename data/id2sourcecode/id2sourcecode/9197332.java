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
