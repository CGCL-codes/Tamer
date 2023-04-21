    @Override
    public void onClick(View arg0) {
        if (arg0 == login) {
            bindService(new Intent("android.client.MY_SERVICE"), this, 0);
            final String u = username.getText().toString();
            final String p = password.getText().toString();
            if (u != "" && p != "") {
                try {
                    this.s.connect(Settings.SERVER_ADDR);
                    boolean ret;
                    if (forcelogin.isChecked()) {
                        ret = this.s.forcelogin(u, p);
                    } else ret = this.s.login(u, p);
                    if (ret) {
                        startActivity(new Intent(android.client.FriendsList.PENDING_ACTION, getIntent().getData()));
                        finish();
                    } else {
                        AlertD.show(this, "Error", 0, "Error occurred while logging in. Check your data or try later", "BACK", false);
                    }
                } catch (Exception e) {
                    AlertD.show(this, "Error", 0, "Error occurred while connecting to server.", "BACK", false);
                }
            } else {
                AlertD.show(this, "Errore", 0, "Please fill in the username and the password field", "BACK", false);
            }
        }
        if (arg0 == register) {
            startActivity(new Intent(android.client.RegisterActivity.REGISTER_ACTION, getIntent().getData()));
        }
    }
