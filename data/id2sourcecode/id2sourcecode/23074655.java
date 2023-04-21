    private void dropDbIfExists() {
        if (!CDatabaseHelper.databaseExists(params.getDb())) return;
        if (params.getDropdbifexists()) dropdb(); else writer.message("database already exists and getDropdbifexists() is false - quitting");
    }
