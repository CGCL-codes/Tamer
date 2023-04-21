package org.horen.core.db;

import java.sql.SQLException;
import org.horen.task.BasePriority;
import org.horen.task.Label;
import org.horen.task.PriorityModel;
import org.horen.task.Type;

public class OracleHelper extends DataBaseHelper {

    @Override
    public void driverLoader() throws NoConnectionException {
        try {
            Class.forName("oracle.jdbc.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new NoConnectionException("No Driver found", e);
        }
    }

    @Override
    public void init() throws SQLException {
        int iSchemaVersion = getSchemaVersion();
        DataBase db = DataBase.getInstance();
        switch(iSchemaVersion) {
            case 0:
                db.sendUpdate("CREATE TABLE config (" + "name VARCHAR2(64) NOT NULL, " + "value VARCHAR2(255), " + "PRIMARY KEY (name))");
                setSchemaVersion(1);
            case 1:
                db.sendUpdate("CREATE TABLE task (" + "task_id NUMBER," + "short_desc VARCHAR2(255) NOT NULL," + "long_desc CLOB NULL," + "create_ts TIMESTAMP NOT NULL," + "start_ts TIMESTAMP NULL," + "finish_ts TIMESTAMP NULL," + "type_id NUMBER NULL," + "base_priority NUMBER NULL," + "priority_model NUMBER NULL," + "parent_task NUMBER NULL," + "template_id NUMBER NULL," + "PRIMARY KEY(task_id)," + "FOREIGN KEY(parent_task)" + " REFERENCES task(task_id)" + " ON DELETE SET NULL" + ")");
                db.sendUpdate("create sequence task_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger task_trigger " + "before insert on task " + "for each row " + "begin " + "select task_seq.nextval into :new.task_id from dual; end; ");
                setSchemaVersion(2);
            case 2:
                db.sendUpdate("CREATE TABLE label (" + "label_id NUMBER, " + "name VARCHAR2(128) NULL," + "PRIMARY KEY(label_id))");
                db.sendUpdate("create sequence label_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger label_trigger " + "before insert on label " + "for each row " + "begin " + "select label_seq.nextval into :new.label_id from dual; end; ");
                setSchemaVersion(3);
            case 3:
                db.sendUpdate("CREATE TABLE task_label_map (" + "label_id NUMBER NOT NULL," + "task_id NUMBER NOT NULL," + "PRIMARY KEY(label_id, task_id)," + "FOREIGN KEY(label_id)" + " REFERENCES label(label_id) ON DELETE CASCADE," + "FOREIGN KEY(task_id)" + " REFERENCES task(task_id) ON DELETE CASCADE" + ")");
                setSchemaVersion(4);
            case 4:
                db.sendUpdate("ALTER TABLE task ADD modify_ts TIMESTAMP NOT NULL");
                setSchemaVersion(5);
            case 5:
                db.sendUpdate("CREATE TABLE workingtime (" + "workingtime_id NUMBER," + "start_ts NUMBER NOT NULL," + "time NUMBER NOT NULL," + "task_id NUMBER NOT NULL," + "PRIMARY KEY(workingtime_id)," + "FOREIGN KEY(task_id)" + " REFERENCES task(task_id)" + " ON DELETE CASCADE)");
                db.sendUpdate("create sequence workingtime_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger workingtime_trigger " + "before insert on workingtime " + "for each row " + "begin " + "select workingtime_seq.nextval into :new.workingtime_id from dual; end; ");
                setSchemaVersion(6);
            case 6:
                db.sendUpdate("ALTER TABLE task ADD deadline_ts TIMESTAMP NULL");
                setSchemaVersion(7);
            case 7:
                db.sendUpdate("CREATE TABLE type (" + "type_id NUMBER," + "name VARCHAR2(128) NULL," + "image BLOB NULL," + "default_priority_model NUMBER NULL," + "PRIMARY KEY(type_id)" + ")");
                db.sendUpdate("create sequence type_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger type_trigger " + "before insert on type " + "for each row " + "begin " + "select type_seq.nextval into :new.type_id from dual; end; ");
                setSchemaVersion(8);
            case 8:
                db.sendUpdate("ALTER TABLE label ADD modify_ts TIMESTAMP NULL");
                setSchemaVersion(9);
            case 9:
                db.sendUpdate("ALTER TABLE type ADD modify_ts TIMESTAMP NULL");
                setSchemaVersion(10);
            case 10:
                db.sendUpdate("ALTER TABLE workingtime ADD modify_ts TIMESTAMP NULL");
                setSchemaVersion(11);
            case 11:
                db.sendUpdate("CREATE TABLE token (" + "token_id INTEGER NOT NULL," + "time TIMESTAMP NOT NULL," + "PRIMARY KEY(token_id)" + ")");
                db.sendUpdate("create sequence token_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger token_trigger " + "before insert on token " + "for each row " + "begin " + "select token_seq.nextval into :new.token_id from dual; end; ");
                setSchemaVersion(12);
            case 12:
                db.sendUpdate("ALTER TABLE task " + "ADD token_id NUMBER");
                setSchemaVersion(13);
            case 13:
                db.sendUpdate("ALTER TABLE task " + "ADD CONSTRAINT task_token FOREIGN KEY (token_id)" + " REFERENCES token(token_id)" + " ON DELETE SET NULL");
                setSchemaVersion(14);
            case 14:
                db.sendUpdate("ALTER TABLE label " + "ADD token_id NUMBER");
                setSchemaVersion(15);
            case 15:
                db.sendUpdate("ALTER TABLE label " + "ADD CONSTRAINT label_token FOREIGN KEY (token_id)" + " REFERENCES token(token_id)" + " ON DELETE SET NULL");
                setSchemaVersion(16);
            case 16:
                db.sendUpdate("ALTER TABLE workingtime " + "ADD token_id NUMBER");
                setSchemaVersion(17);
            case 17:
                db.sendUpdate("ALTER TABLE workingtime " + "ADD CONSTRAINT working_token FOREIGN KEY (token_id)" + " REFERENCES token(token_id)" + " ON DELETE SET NULL");
                setSchemaVersion(18);
            case 18:
                db.sendUpdate("ALTER TABLE type " + "ADD token_id NUMBER");
                setSchemaVersion(19);
            case 19:
                db.sendUpdate("ALTER TABLE type " + "ADD CONSTRAINT type_token FOREIGN KEY (token_id)" + " REFERENCES token(token_id)" + " ON DELETE SET NULL");
                setSchemaVersion(20);
            case 20:
                db.sendUpdate("ALTER TABLE task " + "ADD is_deleted NUMBER DEFAULT '0' NOT NULL");
                setSchemaVersion(21);
            case 21:
                db.sendUpdate("CREATE TABLE priority_model (" + "pm_id NUMBER NOT NULL," + "token_id NUMBER NULL," + "name VARCHAR2(128) NOT NULL," + "a NUMBER DEFAULT '0' NOT NULL," + "m NUMBER DEFAULT '1' NOT NULL," + "n NUMBER DEFAULT '1' NOT NULL," + "o NUMBER DEFAULT '1' NOT NULL," + "modify_ts TIMESTAMP NOT NULL," + "PRIMARY KEY(pm_id)," + "FOREIGN KEY(token_id) " + "REFERENCES token(token_id)" + " ON DELETE SET NULL" + ")");
                db.sendUpdate("create sequence pmodel_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger pmodel_trigger " + "before insert on priority_model " + "for each row " + "begin " + "select pmodel_seq.nextval into :new.pm_id from dual; end; ");
                setSchemaVersion(22);
            case 22:
                db.sendUpdate("ALTER TABLE task " + "ADD CONSTRAINT pmodel_token FOREIGN KEY (priority_model)" + " REFERENCES priority_model(pm_id)" + " ON DELETE SET NULL");
                setSchemaVersion(23);
            case 23:
                db.sendUpdate("ALTER TABLE task " + "ADD planed_time NUMBER DEFAULT 0 NOT NULL");
                setSchemaVersion(24);
            case 24:
                db.sendUpdate("ALTER TABLE task " + "ADD calced_time NUMBER DEFAULT 0 NOT NULL");
                setSchemaVersion(25);
            case 25:
                db.sendUpdate("CREATE TABLE base_priority (" + "priority INTEGER NOT NULL," + "name VARCHAR(32) NULL," + "PRIMARY KEY(priority)" + ")");
                setSchemaVersion(26);
                BasePriority.init();
            case 26:
                db.sendUpdate("CREATE TABLE dependencies (" + "task_1 NUMBER NOT NULL," + "task_2 NUMBER NOT NULL," + "PRIMARY KEY(task_1,task_2)," + "FOREIGN KEY(task_1)" + " REFERENCES task(task_id)" + " ON DELETE CASCADE," + "FOREIGN KEY(task_2)" + " REFERENCES task(task_id)" + " ON DELETE CASCADE" + ")");
                setSchemaVersion(27);
            case 27:
                db.sendUpdate("ALTER TABLE priority_model ADD is_deleted NUMBER DEFAULT 0 NOT NULL");
                setSchemaVersion(28);
            case 28:
                db.sendUpdate("ALTER TABLE type ADD is_deleted NUMBER DEFAULT 0 NOT NULL");
                setSchemaVersion(29);
            case 29:
                db.sendUpdate("CREATE TABLE template (" + "template_id NUMBER NOT NULL," + "current_task NUMBER NULL," + "max_more_tasks NUMBER NULL," + "latest_deadline_ts TIMESTAMP NULL," + "interval_desc VARCHAR2(16) NULL," + "interval_count NUMBER NULL," + "modify_ts TIMESTAMP NULL," + "PRIMARY KEY(template_id)," + "FOREIGN KEY(current_task) " + "REFERENCES task(task_id)" + " ON DELETE SET NULL" + ")");
                db.sendUpdate("create sequence template_seq " + "start with 1 " + "increment by 1 " + "nomaxvalue");
                db.sendUpdate("create trigger template_trigger " + "before insert on template " + "for each row " + "begin " + "select template_seq.nextval into :new.template_id from dual; end");
                setSchemaVersion(30);
            case 30:
                db.sendUpdate("ALTER TABLE task " + "ADD CONSTRAINT task_template FOREIGN KEY (template_id)" + " REFERENCES template(template_id)" + " ON DELETE SET NULL");
                setSchemaVersion(31);
            case 31:
                db.sendUpdate("ALTER TABLE workingtime DROP COLUMN start_ts");
                db.sendUpdate("ALTER TABLE workingtime ADD start_ts TIMESTAMP NOT NULL");
                setSchemaVersion(32);
            case 32:
                db.sendUpdate("ALTER TABLE template " + "ADD token_id NUMBER");
                setSchemaVersion(33);
            case 33:
                Type.createDefault();
                setSchemaVersion(34);
            case 34:
                PriorityModel.createDefault();
                setSchemaVersion(35);
            case 35:
                Label.createDefault();
                setSchemaVersion(36);
        }
    }
}
