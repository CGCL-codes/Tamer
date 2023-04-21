        protected void onPostExecute(Integer paramInteger) {
            dialog.dismiss();
            if (paramInteger.intValue() == 1) MainActivity.this.okMessage("Failed to remount /system as read/write !");
            while (true) {
                MainActivity.this.okMessage("Not sure what happened here. Either the UnRoot failed or the reboot failed. Please reboot manually and see if you still have SuperUser !");
                return;
            }
        }
