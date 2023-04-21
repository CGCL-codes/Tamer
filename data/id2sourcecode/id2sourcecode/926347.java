    public void updateStats() {
        CollectAmountLabel.setText(String.valueOf(pennerStats.collectedBottles));
        SellAmountLabel.setText(String.valueOf(pennerStats.soldBottles));
        Formatter f = new Formatter();
        f.format("%6.2f", pennerStats.earnedMoney);
        EarnAmountLabel.setText(String.valueOf(f.toString()) + " �");
        StudyAmountLabel.setText(String.valueOf(pennerStats.startedStudies));
        f = new Formatter();
        f.format("%6.2f", pennerStats.paidMoney);
        PayAmountLabel.setText(String.valueOf(f.toString()) + " �");
        ReloginCountLabel.setText(String.valueOf(pennerStats.reloginCount));
        f = new Formatter();
        int gC = pennerStats.goodCaptchas;
        int lT = pennerStats.loggedInTime;
        if (gC > lT) {
            gC = lT;
        }
        if (pennerStats.valuechanged) {
            if (lT > 0) {
                newvalue = Math.round(gC * 100 / lT);
                if (lastvalue > 0) {
                    value = (lastvalue + newvalue) / 2;
                }
            } else {
                value = 0;
            }
            lastvalue = newvalue;
            pennerStats.valuechanged = false;
        }
        f.format("%3.0f", value);
        if (!pennerConnection.isServerError()) {
            pennerStats.setStatus(resbundle.getString("server_error"));
        } else if (pennerConnection.LogedIn) {
            pennerStats.setStatus(resbundle.getString("logedin"));
        }
        StatusEffectivityLabel.setText(String.valueOf(f.toString()) + " % (" + String.valueOf(gC) + "/" + String.valueOf(lT) + ")");
        StatusTextLabel.setText(pennerStats.Status);
    }
