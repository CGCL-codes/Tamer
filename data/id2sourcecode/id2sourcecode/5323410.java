    void setSimStep(int s) {
        ((SelectEvent) getTS().getAg()).cleanCows();
        simStep = s;
        try {
            super.setCycleNumber(simStep);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (view != null) view.setCycle(simStep);
        if (writeStatusThread != null) writeStatusThread.go();
    }
