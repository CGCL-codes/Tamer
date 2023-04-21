    private static void addInsertUpdateDeleteQueryBodyWithCollection(CodeEmitter co, QueryObjectGenerator generator, Mapper mapper) {
        MethodParameterInfo[] collectionParameterInfos = mapper.getMethod().getCollectionParameterInfos();
        int numParameterCollections = collectionParameterInfos.length;
        Class<?> returnType = mapper.getMethod().getReturnInfo().getType();
        if (!(returnType == Void.TYPE) && !(returnType.isArray() && returnType.getComponentType() == Integer.TYPE)) {
            throw new ValidationException("Only int[] or void is allowed as return type");
        }
        if (numParameterCollections > 1) {
            Label labelException = co.make_label();
            Label labelNoException = co.make_label();
            for (int i = 0; i < numParameterCollections - 1; i++) {
                co.load_arg(collectionParameterInfos[i].getIndex());
                co.invoke_interface(TYPE_Collection, SIG_size);
                co.load_arg(collectionParameterInfos[i + 1].getIndex());
                co.invoke_interface(TYPE_Collection, SIG_size);
                co.if_icmp(CodeEmitter.NE, labelException);
            }
            co.goTo(labelNoException);
            co.mark(labelException);
            co.throw_exception(TYPE_SQLException, EXCEPTION_COLLECTIONS_DIFFERENT_SIZE);
            co.mark(labelNoException);
        }
        co.load_arg(collectionParameterInfos[0].getIndex());
        co.invoke_interface(TYPE_Collection, SIG_size);
        Label labelNotZero = co.make_label();
        co.if_jump(CodeEmitter.NE, labelNotZero);
        if (returnType.isArray()) {
            co.push(0);
            co.newarray(TYPE_int);
        }
        co.return_value();
        co.mark(labelNotZero);
        Local localConnection = co.make_local(TYPE_Connection);
        Local localPreparedStatement = co.make_local(TYPE_PreparedStatement);
        Local localException = co.make_local(TYPE_Throwable);
        EmitUtils.emitGetConnection(co, generator, localConnection);
        Block tryBlockConnection = co.begin_block();
        co.load_local(localConnection);
        pushSql(co, mapper, mapper.getSql());
        co.invoke_interface(TYPE_Connection, SIG_prepareStatement);
        co.store_local(localPreparedStatement);
        Block tryBlockStatement = co.begin_block();
        Local localResult = null;
        Local localPartResult = null;
        Local localIndex = null;
        if (returnType.isArray()) {
            localResult = co.make_local(TYPE_intArray);
            localPartResult = co.make_local(TYPE_intArray);
            co.load_arg(collectionParameterInfos[0].getIndex());
            co.invoke_interface(TYPE_Collection, SIG_size);
            co.newarray(TYPE_int);
            co.store_local(localResult);
            localIndex = co.make_local(TYPE_int);
            co.push(0);
            co.store_local(localIndex);
        }
        Local localCounter = co.make_local(TYPE_int);
        co.push(0);
        co.store_local(localCounter);
        Local[] localIterators = new Local[numParameterCollections];
        Local[] localObjects = new Local[numParameterCollections];
        for (int i = 0; i < numParameterCollections; i++) {
            localIterators[i] = co.make_local(TYPE_Iterator);
            co.load_arg(collectionParameterInfos[i].getIndex());
            co.invoke_interface(TYPE_Collection, SIG_iterator);
            co.store_local(localIterators[i]);
            localObjects[i] = co.make_local(Type.getType(collectionParameterInfos[i].getCollectionElementType()));
        }
        Label labelBeginWhile = co.make_label();
        Label labelEndWhile = co.make_label();
        co.mark(labelBeginWhile);
        co.load_local(localIterators[0]);
        co.invoke_interface(TYPE_Iterator, SIG_hasNext);
        co.if_jump(CodeEmitter.EQ, labelEndWhile);
        for (int i = 0; i < numParameterCollections; i++) {
            co.load_local(localIterators[i]);
            co.invoke_interface(TYPE_Iterator, SIG_iterator_next);
            co.checkcast(Type.getType(collectionParameterInfos[i].getCollectionElementType()));
            co.store_local(localObjects[i]);
        }
        co.iinc(localCounter, 1);
        Local localParameterIndexOffset = null;
        if (mapper.usesArray()) {
            localParameterIndexOffset = co.make_local(TYPE_int);
            co.push(0);
            co.store_local(localParameterIndexOffset);
        }
        ParameterMappingGenerator pmg = new ParameterMappingGenerator(co, localPreparedStatement, localObjects, collectionParameterInfos, localParameterIndexOffset);
        mapper.acceptParameterMappers(pmg);
        co.load_this();
        co.getfield(FIELD_NAME_BATCH_SIZE);
        Label labelNoBatching = co.make_label();
        co.if_jump(CodeEmitter.LE, labelNoBatching);
        co.load_local(localPreparedStatement);
        co.invoke_interface(TYPE_PreparedStatement, SIG_addBatch);
        co.load_local(localCounter);
        co.load_this();
        co.getfield(FIELD_NAME_BATCH_SIZE);
        Label labelAfter = co.make_label();
        co.if_icmp(CodeEmitter.LT, labelAfter);
        co.load_local(localPreparedStatement);
        co.invoke_interface(TYPE_PreparedStatement, SIG_executeBatch);
        if (returnType.isArray()) {
            co.store_local(localPartResult);
            co.load_local(localPartResult);
            co.push(0);
            co.load_local(localResult);
            co.load_local(localIndex);
            co.load_local(localPartResult);
            co.arraylength();
            co.invoke_static(TYPE_System, SIG_arraycopy);
            co.load_local(localIndex);
            co.load_local(localPartResult);
            co.arraylength();
            co.math(CodeEmitter.ADD, TYPE_int);
            co.store_local(localIndex);
        } else {
            co.pop();
        }
        co.push(0);
        co.store_local(localCounter);
        co.mark(labelAfter);
        co.goTo(labelBeginWhile);
        co.mark(labelNoBatching);
        if (returnType.isArray()) {
            co.load_local(localResult);
            co.load_local(localIndex);
            co.iinc(localIndex, 1);
            co.load_local(localPreparedStatement);
            co.invoke_interface(TYPE_PreparedStatement, SIG_executeUpdate);
            co.array_store(TYPE_int);
        } else {
            co.load_local(localPreparedStatement);
            co.invoke_interface(TYPE_PreparedStatement, SIG_executeUpdate);
            co.pop();
        }
        co.goTo(labelBeginWhile);
        co.mark(labelEndWhile);
        Label labelAfter2 = co.make_label();
        co.load_local(localCounter);
        co.if_jump(CodeEmitter.LE, labelAfter2);
        co.load_this();
        co.getfield(FIELD_NAME_BATCH_SIZE);
        co.if_jump(CodeEmitter.LE, labelAfter2);
        co.load_local(localPreparedStatement);
        co.invoke_interface(TYPE_PreparedStatement, SIG_executeBatch);
        if (returnType.isArray()) {
            co.store_local(localPartResult);
            co.load_local(localPartResult);
            co.push(0);
            co.load_local(localResult);
            co.load_local(localIndex);
            co.load_local(localPartResult);
            co.arraylength();
            co.invoke_static(TYPE_System, SIG_arraycopy);
        } else {
            co.pop();
        }
        co.mark(labelAfter2);
        tryBlockStatement.end();
        EmitUtils.emitClose(co, localPreparedStatement);
        tryBlockConnection.end();
        EmitUtils.emitUngetConnection(co, generator, localConnection);
        if (returnType.isArray()) {
            co.load_local(localResult);
        }
        co.return_value();
        EmitUtils.emitCatchException(co, tryBlockStatement, null);
        Block tryBlockStatement2 = co.begin_block();
        co.store_local(localException);
        EmitUtils.emitClose(co, localPreparedStatement);
        co.load_local(localException);
        co.athrow();
        tryBlockStatement2.end();
        EmitUtils.emitCatchException(co, tryBlockConnection, null);
        EmitUtils.emitCatchException(co, tryBlockStatement2, null);
        co.store_local(localException);
        EmitUtils.emitUngetConnection(co, generator, localConnection);
        co.load_local(localException);
        co.athrow();
    }
