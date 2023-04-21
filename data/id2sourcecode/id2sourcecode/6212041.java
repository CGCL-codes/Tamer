    public boolean createdb() {
        if (CDatabaseHelper.databaseExists(params.db)) {
            if (!params.dropdbifexists) {
                writer.message("database already exists and dropdbifexists is false - quitting");
                return false;
            } else dropdb();
        }
        CDatabaseHelper.createDatabase(params.db);
        return true;
    }
