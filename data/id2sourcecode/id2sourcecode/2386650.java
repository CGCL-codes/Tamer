    public void setAdapter(ChannelAdapter adapter) {
        Log.i("ScrcoolTab", "setAdapter");
        if (null != adapter && adapter.getChannelEntity() != null) {
            View view = null;
            for (ChannelEntity entity : adapter.getChannelEntity()) {
                view = mInflater.inflate(R.layout.gallery_item_channel, this, false);
                if (view instanceof TextView) {
                    ((TextView) view).setText(entity.getCNAME());
                }
                this.addView(view);
            }
        }
    }
