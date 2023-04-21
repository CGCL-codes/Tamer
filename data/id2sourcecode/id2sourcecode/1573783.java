    public long SaveUnivClassEvent(UnivClassEvent event) {
        long returnVal = -1;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            if (event.ParentUnivClassId < 0) {
                Log.e("DATABASE", "SaveUnivClassEvent: the event passed in doesn't have a parent univ class id");
                return (long) -1;
            }
            String cols[] = { "univ_class_event_id", "due_date" };
            String sWhere = "parent_univ_class_id = " + event.ParentUnivClassId;
            sWhere += " AND name = '" + event.name + "' ";
            Cursor result = db.query(DB_TABLE_UNIV_CLASS_EVENT, cols, sWhere, null, null, null, null);
            if (result.getCount() > 1) {
                Log.e("DATABASE", "SaveUnivClassEvent: multiple events with the same name: " + event.name);
            } else if (result.getCount() == 1) {
                Log.d("DATABASE", "SaveUnivClassEvent: event is already in db: " + event.name);
                result.moveToFirst();
                returnVal = result.getLong(0);
                SimpleDateFormat s = new SimpleDateFormat();
                s.applyPattern(FORMAT_DATE);
                Date dateInDB = s.parse(result.getString(1));
                if (dateInDB.compareTo(event.dueDate) != 0) {
                    Log.d("DATABASE", "SaveUnivClassEvent: event due_date is different, so updating it.");
                    ContentValues values = new ContentValues();
                    values.put("due_date", s.format(event.dueDate));
                    db.update(DB_TABLE_UNIV_CLASS_EVENT, values, "univ_class_event_id = " + returnVal, null);
                }
            } else {
                SimpleDateFormat s = new SimpleDateFormat();
                s.applyPattern(FORMAT_DATE);
                ContentValues insertValues = new ContentValues();
                insertValues.put("parent_univ_class_id", event.ParentUnivClassId);
                insertValues.put("name", event.name);
                insertValues.put("recurring_type", event.RecurringType);
                insertValues.put("due_date", s.format(event.dueDate));
                returnVal = db.insert("univ_class_event", null, insertValues);
                if (returnVal == -1) {
                    Log.e("DATABASE", "SaveUnivClassEvent: error inserting the event: " + event.name);
                } else {
                    Log.d("DATABASE", "SaveUnivClassEvent: successfully saved event id: " + String.valueOf(returnVal) + " name: " + event.name + " due: " + s.format(event.dueDate));
                }
            }
        } catch (Exception e) {
            Log.e("DATABASE", "SaveUnivClassEvent error: " + e.getMessage() + e.toString());
        }
        return returnVal;
    }
