    private void disqualifyApps(List<PushApp> apps, String channel) {
        List<PushApp> remove = new ArrayList<PushApp>();
        for (PushApp local : apps) {
            if (!local.getChannels().contains(channel)) {
                remove.add(local);
            }
        }
        apps.removeAll(remove);
    }
