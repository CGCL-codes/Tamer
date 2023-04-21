package org.cycads.entities.refact;

import org.cycads.entities.annotation.DBToDBAnnotation;
import org.cycads.entities.annotation.dBLink.DBRecord;

public class DBToDBAnnotation extends DBAnnotation implements DBToDBAnnotation {

    private DBRecord source;

    public DBRecord getSourceRecord() {
        return source;
    }
}
