    public void rearingNow(final boolean rearing) {
        if (rearing) {
            rearing_now = true;
            configs.rearing_thresh = (normal_rat_area + current_rat_area) / 2;
            System.out.print("Rearing threshold: " + configs.rearing_thresh + "\n");
        } else {
            rearing_now = false;
            final Thread th_rearing = new Thread(new NormalRatAreaThread());
            th_rearing.start();
            System.out.print("Rearing Training Started" + "\n");
        }
    }
