    protected void finalize() {
        for (URL url : moduleUrls) {
            try {
                url.openConnection().setDefaultUseCaches(false);
            } catch (Exception e) {
                trace(e.getMessage());
            }
        }
    }
