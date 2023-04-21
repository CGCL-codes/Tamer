        @Override
        protected List<Channel> doInBackground(Void... params) {
            try {
                return mGuidaTvService.getChannels();
            } catch (IOException e) {
                Log.d("ScheduleView", "Cannot retrieve channels", e);
                return null;
            } catch (ResourceException e) {
                Log.d("ScheduleView", "Cannot retrieve channels", e);
                return null;
            }
        }
