package com.daffodilwoods.daffodildb.server.sql99.ddl.schemadefinition;

public interface oldornewvaluesalias extends com.daffodilwoods.daffodildb.utils.parser.StatementExecuter {

    int OLDROWTYPE = 0;

    int OLDTABLETYPE = 1;

    int NEWROWTYPE = 2;

    int NEWTABLETYPE = 3;

    int getTriggerType();
}
