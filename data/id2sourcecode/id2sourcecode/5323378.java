    @Override
    public void stopAg() {
        if (view != null) view.dispose();
        if (writeStatusThread != null) writeStatusThread.interrupt();
        super.stopAg();
    }
