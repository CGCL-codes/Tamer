    private void requestWriteSelect() {
        is_ready_for_write = false;
        if (filter != null) {
            filter.getHelper().resumeWriteSelects();
        }
    }
