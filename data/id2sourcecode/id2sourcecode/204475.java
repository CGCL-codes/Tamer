    public void buildReferenceMaps(VM_Method method, int[] byte2machine, VM_ReferenceMaps referenceMaps, VM_BuildBB buildBB) {
        int gcPointCount = buildBB.gcPointCount;
        short byteToBlockMap[] = buildBB.byteToBlockMap;
        VM_BasicBlock basicBlocks[] = buildBB.basicBlocks;
        int jsrCount = buildBB.numJsrs;
        byte bbMaps[][];
        int[] blockStkTop;
        int currBBNum;
        byte currBBMap[];
        int currBBStkTop;
        int currBBStkEmpty;
        int paramCount;
        VM_PendingRETInfo bbPendingRETs[] = null;
        VM_PendingRETInfo currPendingRET;
        VM_JSRSubroutineInfo JSRSubs[] = null;
        short workStk[];
        boolean blockSeen[];
        VM_ExceptionHandlerMap exceptions;
        int tryStartPC[];
        int tryEndPC[];
        int tryHandlerPC[];
        int tryHandlerLength;
        int reachableHandlerBBNums[];
        int reachableHandlersCount;
        boolean handlerProcessed[];
        boolean handlersAllDone;
        byte bytecodes[];
        int i;
        short brBBNum;
        VM_Class declaringClass;
        paramCount = method.getParameterWords();
        if (!method.isStatic()) paramCount++;
        currBBStkEmpty = method.getLocalWords() - 1;
        bytecodes = method.getBytecodes();
        declaringClass = method.getDeclaringClass();
        bbMaps = new byte[VM_BasicBlock.getNumberofBlocks() + 1][];
        blockStkTop = new int[bbMaps.length];
        blockSeen = new boolean[bbMaps.length];
        exceptions = method.getExceptionHandlerMap();
        if (exceptions != null) {
            tryStartPC = exceptions.getStartPC();
            tryEndPC = exceptions.getEndPC();
            tryHandlerPC = exceptions.getHandlerPC();
            tryHandlerLength = tryHandlerPC.length;
            reachableHandlerBBNums = new int[tryStartPC.length];
            handlerProcessed = new boolean[tryStartPC.length];
            if (jsrCount > 0) {
                JSRSubs = new VM_JSRSubroutineInfo[jsrCount];
                JSRSubNext = 0;
                bbPendingRETs = new VM_PendingRETInfo[bbMaps.length];
            }
            handlersAllDone = (tryHandlerLength == 0);
        } else {
            tryHandlerLength = 0;
            handlersAllDone = true;
            tryStartPC = null;
            tryEndPC = null;
            tryHandlerPC = null;
            reachableHandlerBBNums = null;
            handlerProcessed = null;
        }
        reachableHandlersCount = 0;
        referenceMaps.startNewMaps(gcPointCount, jsrCount, paramCount);
        if (VM.ReferenceMapsStatistics) referenceMaps.bytecount = referenceMaps.bytecount + bytecodes.length;
        workStk = new short[10 + tryHandlerLength];
        workStkTop = 0;
        workStk[workStkTop] = byteToBlockMap[0];
        currBBMap = new byte[method.getOperandWords() + currBBStkEmpty + 1];
        VM_Type[] parameterTypes = method.getParameterTypes();
        int paramStart;
        if (!method.isStatic()) {
            currBBMap[0] = REFERENCE;
            paramStart = 1;
        } else paramStart = 0;
        int n = parameterTypes.length;
        for (i = 0; i < n; i++, paramStart++) {
            VM_Type parameterType = parameterTypes[i];
            currBBMap[paramStart] = parameterType.isReferenceType() ? REFERENCE : NON_REFERENCE;
            if (parameterType.getStackWords() == DOUBLEWORD) paramStart++;
        }
        currBBStkTop = currBBStkEmpty;
        bbMaps[byteToBlockMap[0]] = currBBMap;
        blockStkTop[byteToBlockMap[0]] = currBBStkTop;
        referenceMaps.recordStkMap(0, currBBMap, currBBStkTop, false);
        currBBMap = new byte[currBBMap.length];
        while (workStkTop > -1) {
            currBBNum = workStk[workStkTop];
            workStkTop--;
            boolean inJSRSub = false;
            if (bbMaps[currBBNum] != null) {
                currBBStkTop = blockStkTop[currBBNum];
                for (int k = 0; k <= currBBStkTop; k++) currBBMap[k] = bbMaps[currBBNum][k];
                if (jsrCount > 0 && basicBlocks[currBBNum].isInJSR()) {
                    inJSRSub = true;
                }
            } else {
                VM.sysWrite("VM_BuildReferenceMaps, error: found a block on work stack with");
                VM.sysWrite(" no starting map. The block number is ");
                VM.sysWrite(basicBlocks[currBBNum].blockNumber);
                VM.sysWrite("\n");
                VM.sysFail("VM_BuildReferenceMaps work stack failure");
            }
            int start = basicBlocks[currBBNum].getStart();
            int end = basicBlocks[currBBNum].getEnd();
            if (jsrCount > 0 && inJSRSub) {
                currPendingRET = bbPendingRETs[currBBNum];
                if (basicBlocks[currBBNum].isTryStart()) {
                    for (int k = 0; k < tryHandlerLength; k++) {
                        if (tryStartPC[k] == start) {
                            int handlerBBNum = byteToBlockMap[tryHandlerPC[k]];
                            bbPendingRETs[handlerBBNum] = new VM_PendingRETInfo(currPendingRET);
                        }
                    }
                }
            } else currPendingRET = null;
            boolean inTryBlock;
            if (basicBlocks[currBBNum].isTryBlock()) {
                inTryBlock = true;
                reachableHandlersCount = 0;
                for (i = 0; i < tryHandlerLength; i++) if (tryStartPC[i] <= start && tryEndPC[i] >= end) {
                    reachableHandlerBBNums[reachableHandlersCount] = byteToBlockMap[tryHandlerPC[i]];
                    reachableHandlersCount++;
                    if (tryStartPC[i] == start) {
                        int handlerBBNum = byteToBlockMap[tryHandlerPC[i]];
                        if (bbMaps[handlerBBNum] == null) {
                            bbMaps[handlerBBNum] = new byte[currBBMap.length];
                            for (int k = 0; k <= currBBStkEmpty; k++) bbMaps[handlerBBNum][k] = currBBMap[k];
                            bbMaps[handlerBBNum][currBBStkEmpty + 1] = REFERENCE;
                            blockStkTop[handlerBBNum] = currBBStkEmpty + 1;
                        }
                    }
                }
            } else inTryBlock = false;
            boolean processNextBlock = true;
            for (i = start; i <= end; ) {
                int opcode = ((int) bytecodes[i]) & 0x000000FF;
                int opLength = JBC_length[opcode];
                switch(opcode) {
                    case JBC_nop:
                        {
                            break;
                        }
                    case JBC_aconst_null:
                    case JBC_aload_0:
                    case JBC_aload_1:
                    case JBC_aload_2:
                    case JBC_aload_3:
                    case JBC_aload:
                        {
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = REFERENCE;
                            break;
                        }
                    case JBC_iconst_m1:
                    case JBC_iconst_0:
                    case JBC_iconst_1:
                    case JBC_iconst_2:
                    case JBC_iconst_3:
                    case JBC_iconst_4:
                    case JBC_iconst_5:
                    case JBC_fconst_0:
                    case JBC_fconst_1:
                    case JBC_fconst_2:
                    case JBC_iload_0:
                    case JBC_iload_1:
                    case JBC_iload_2:
                    case JBC_iload_3:
                    case JBC_fload_0:
                    case JBC_fload_1:
                    case JBC_fload_2:
                    case JBC_fload_3:
                    case JBC_bipush:
                    case JBC_iload:
                    case JBC_fload:
                    case JBC_sipush:
                    case JBC_i2l:
                    case JBC_i2d:
                    case JBC_f2l:
                    case JBC_f2d:
                        {
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_lconst_0:
                    case JBC_lconst_1:
                    case JBC_dconst_0:
                    case JBC_dconst_1:
                    case JBC_lload_0:
                    case JBC_lload_1:
                    case JBC_lload_2:
                    case JBC_lload_3:
                    case JBC_dload_0:
                    case JBC_dload_1:
                    case JBC_dload_2:
                    case JBC_dload_3:
                    case JBC_ldc2_w:
                    case JBC_lload:
                    case JBC_dload:
                        {
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = NON_REFERENCE;
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_ldc:
                        {
                            int cpindex = ((int) bytecodes[i + 1]) & 0xFF;
                            currBBStkTop++;
                            if (declaringClass.getLiteralDescription(cpindex) == VM_Statics.STRING_LITERAL) currBBMap[currBBStkTop] = REFERENCE; else currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_ldc_w:
                        {
                            int cpindex = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            currBBStkTop++;
                            if (declaringClass.getLiteralDescription(cpindex) == VM_Statics.STRING_LITERAL) currBBMap[currBBStkTop] = REFERENCE; else currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_istore:
                    case JBC_fstore:
                        {
                            int index = ((int) bytecodes[i + 1]) & 0xFF;
                            index = index;
                            if (!inJSRSub) currBBMap[index] = NON_REFERENCE; else currBBMap[index] = SET_TO_NONREFERENCE;
                            if (inTryBlock) setHandlersMapsNonRef(index, ONEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop--;
                            break;
                        }
                    case JBC_lstore:
                    case JBC_dstore:
                        {
                            int index = ((int) bytecodes[i + 1]) & 0xFF;
                            index = index;
                            if (!inJSRSub) {
                                currBBMap[index] = NON_REFERENCE;
                                currBBMap[index + 1] = NON_REFERENCE;
                            } else {
                                currBBMap[index] = SET_TO_NONREFERENCE;
                                currBBMap[index + 1] = SET_TO_NONREFERENCE;
                            }
                            if (inTryBlock) setHandlersMapsNonRef(index, DOUBLEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop = currBBStkTop - 2;
                            break;
                        }
                    case JBC_astore:
                        {
                            int index = ((int) bytecodes[i + 1]) & 0xFF;
                            currBBMap[index] = currBBMap[currBBStkTop];
                            if (inJSRSub) {
                                if (currBBMap[index] == RETURN_ADDRESS) currPendingRET.updateReturnAddressLocation(index);
                                if (inTryBlock) {
                                    if (currBBMap[index] == REFERENCE) setHandlersMapsRef(index, reachableHandlerBBNums, reachableHandlersCount, bbMaps); else setHandlersMapsReturnAddress(index, reachableHandlerBBNums, reachableHandlersCount, bbMaps);
                                }
                            }
                            currBBStkTop--;
                            break;
                        }
                    case JBC_istore_0:
                    case JBC_fstore_0:
                        {
                            if (!inJSRSub) currBBMap[0] = NON_REFERENCE; else currBBMap[0] = SET_TO_NONREFERENCE;
                            if (inTryBlock) setHandlersMapsNonRef(0, ONEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop--;
                            break;
                        }
                    case JBC_istore_1:
                    case JBC_fstore_1:
                        {
                            if (!inJSRSub) currBBMap[1] = NON_REFERENCE; else currBBMap[1] = SET_TO_NONREFERENCE;
                            if (inTryBlock) setHandlersMapsNonRef(1, ONEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop--;
                            break;
                        }
                    case JBC_istore_2:
                    case JBC_fstore_2:
                        {
                            if (!inJSRSub) currBBMap[2] = NON_REFERENCE; else currBBMap[2] = SET_TO_NONREFERENCE;
                            if (inTryBlock) setHandlersMapsNonRef(2, ONEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop--;
                            break;
                        }
                    case JBC_istore_3:
                    case JBC_fstore_3:
                        {
                            if (!inJSRSub) currBBMap[3] = NON_REFERENCE; else currBBMap[3] = SET_TO_NONREFERENCE;
                            if (inTryBlock) setHandlersMapsNonRef(3, ONEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop--;
                            break;
                        }
                    case JBC_lstore_0:
                    case JBC_dstore_0:
                        {
                            if (inJSRSub) {
                                currBBMap[0] = NON_REFERENCE;
                                currBBMap[1] = NON_REFERENCE;
                            } else {
                                currBBMap[0] = SET_TO_NONREFERENCE;
                                currBBMap[1] = SET_TO_NONREFERENCE;
                            }
                            if (inTryBlock) setHandlersMapsNonRef(0, DOUBLEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop = currBBStkTop - 2;
                            break;
                        }
                    case JBC_lstore_1:
                    case JBC_dstore_1:
                        {
                            if (!inJSRSub) {
                                currBBMap[1] = NON_REFERENCE;
                                currBBMap[2] = NON_REFERENCE;
                            } else {
                                currBBMap[1] = SET_TO_NONREFERENCE;
                                currBBMap[2] = SET_TO_NONREFERENCE;
                            }
                            if (inTryBlock) setHandlersMapsNonRef(1, DOUBLEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop = currBBStkTop - 2;
                            break;
                        }
                    case JBC_lstore_2:
                    case JBC_dstore_2:
                        {
                            if (!inJSRSub) {
                                currBBMap[2] = NON_REFERENCE;
                                currBBMap[3] = NON_REFERENCE;
                            } else {
                                currBBMap[2] = SET_TO_NONREFERENCE;
                                currBBMap[3] = SET_TO_NONREFERENCE;
                            }
                            if (inTryBlock) setHandlersMapsNonRef(2, DOUBLEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop = currBBStkTop - 2;
                            break;
                        }
                    case JBC_lstore_3:
                    case JBC_dstore_3:
                        {
                            if (!inJSRSub) {
                                currBBMap[3] = NON_REFERENCE;
                                currBBMap[4] = NON_REFERENCE;
                            } else {
                                currBBMap[3] = SET_TO_NONREFERENCE;
                                currBBMap[4] = SET_TO_NONREFERENCE;
                            }
                            if (inTryBlock) setHandlersMapsNonRef(3, DOUBLEWORD, reachableHandlerBBNums, reachableHandlersCount, inJSRSub, bbMaps);
                            currBBStkTop = currBBStkTop - 2;
                            break;
                        }
                    case JBC_astore_0:
                        {
                            currBBMap[0] = currBBMap[currBBStkTop];
                            if (inJSRSub) {
                                if (currBBMap[0] == RETURN_ADDRESS) currPendingRET.updateReturnAddressLocation(0);
                                if (inTryBlock) {
                                    if (currBBMap[0] == REFERENCE) setHandlersMapsRef(0, reachableHandlerBBNums, reachableHandlersCount, bbMaps); else setHandlersMapsReturnAddress(0, reachableHandlerBBNums, reachableHandlersCount, bbMaps);
                                }
                            }
                            currBBStkTop--;
                            break;
                        }
                    case JBC_astore_1:
                        {
                            currBBMap[1] = currBBMap[currBBStkTop];
                            if (inJSRSub) {
                                if (currBBMap[1] == RETURN_ADDRESS) currPendingRET.updateReturnAddressLocation(1);
                                if (inTryBlock) {
                                    if (currBBMap[1] == REFERENCE) setHandlersMapsRef(1, reachableHandlerBBNums, reachableHandlersCount, bbMaps); else setHandlersMapsReturnAddress(1, reachableHandlerBBNums, reachableHandlersCount, bbMaps);
                                }
                            }
                            currBBStkTop--;
                            break;
                        }
                    case JBC_astore_2:
                        {
                            currBBMap[2] = currBBMap[currBBStkTop];
                            if (inJSRSub) {
                                if (currBBMap[2] == RETURN_ADDRESS) currPendingRET.updateReturnAddressLocation(2);
                                if (inTryBlock) {
                                    if (currBBMap[2] == REFERENCE) setHandlersMapsRef(2, reachableHandlerBBNums, reachableHandlersCount, bbMaps); else setHandlersMapsReturnAddress(2, reachableHandlerBBNums, reachableHandlersCount, bbMaps);
                                }
                            }
                            currBBStkTop--;
                            break;
                        }
                    case JBC_astore_3:
                        {
                            currBBMap[3] = currBBMap[currBBStkTop];
                            if (inJSRSub) {
                                if (currBBMap[3] == RETURN_ADDRESS) currPendingRET.updateReturnAddressLocation(3);
                                if (inTryBlock) {
                                    if (currBBMap[3] == REFERENCE) setHandlersMapsRef(3, reachableHandlerBBNums, reachableHandlersCount, bbMaps); else setHandlersMapsReturnAddress(3, reachableHandlerBBNums, reachableHandlersCount, bbMaps);
                                }
                            }
                            currBBStkTop--;
                            break;
                        }
                    case JBC_dup:
                        {
                            currBBMap[currBBStkTop + 1] = currBBMap[currBBStkTop];
                            currBBStkTop++;
                            break;
                        }
                    case JBC_dup2:
                        {
                            currBBMap[currBBStkTop + 1] = currBBMap[currBBStkTop - 1];
                            currBBMap[currBBStkTop + 2] = currBBMap[currBBStkTop];
                            currBBStkTop = currBBStkTop + 2;
                            break;
                        }
                    case JBC_dup_x1:
                        {
                            currBBMap[currBBStkTop + 1] = currBBMap[currBBStkTop];
                            currBBMap[currBBStkTop] = currBBMap[currBBStkTop - 1];
                            currBBMap[currBBStkTop - 1] = currBBMap[currBBStkTop + 1];
                            currBBStkTop++;
                            break;
                        }
                    case JBC_dup2_x1:
                        {
                            currBBMap[currBBStkTop + 2] = currBBMap[currBBStkTop];
                            currBBMap[currBBStkTop + 1] = currBBMap[currBBStkTop - 1];
                            currBBMap[currBBStkTop] = currBBMap[currBBStkTop - 2];
                            currBBMap[currBBStkTop - 1] = currBBMap[currBBStkTop + 2];
                            currBBMap[currBBStkTop - 2] = currBBMap[currBBStkTop + 1];
                            currBBStkTop = currBBStkTop + 2;
                            break;
                        }
                    case JBC_dup_x2:
                        {
                            currBBMap[currBBStkTop + 1] = currBBMap[currBBStkTop];
                            currBBMap[currBBStkTop] = currBBMap[currBBStkTop - 1];
                            currBBMap[currBBStkTop - 1] = currBBMap[currBBStkTop - 2];
                            currBBMap[currBBStkTop - 2] = currBBMap[currBBStkTop + 1];
                            currBBStkTop++;
                            break;
                        }
                    case JBC_dup2_x2:
                        {
                            currBBMap[currBBStkTop + 2] = currBBMap[currBBStkTop];
                            currBBMap[currBBStkTop + 1] = currBBMap[currBBStkTop - 1];
                            currBBMap[currBBStkTop] = currBBMap[currBBStkTop - 2];
                            currBBMap[currBBStkTop - 1] = currBBMap[currBBStkTop - 3];
                            currBBMap[currBBStkTop - 2] = currBBMap[currBBStkTop + 2];
                            currBBMap[currBBStkTop - 3] = currBBMap[currBBStkTop + 1];
                            currBBStkTop = currBBStkTop + 2;
                            break;
                        }
                    case JBC_swap:
                        {
                            byte temp;
                            temp = currBBMap[currBBStkTop];
                            currBBMap[currBBStkTop] = currBBMap[currBBStkTop - 1];
                            currBBMap[currBBStkTop - 1] = temp;
                            break;
                        }
                    case JBC_pop:
                    case JBC_iadd:
                    case JBC_fadd:
                    case JBC_isub:
                    case JBC_fsub:
                    case JBC_imul:
                    case JBC_fmul:
                    case JBC_fdiv:
                    case JBC_frem:
                    case JBC_ishl:
                    case JBC_ishr:
                    case JBC_iushr:
                    case JBC_lshl:
                    case JBC_lshr:
                    case JBC_lushr:
                    case JBC_iand:
                    case JBC_ior:
                    case JBC_ixor:
                    case JBC_l2i:
                    case JBC_l2f:
                    case JBC_d2i:
                    case JBC_d2f:
                    case JBC_fcmpl:
                    case JBC_fcmpg:
                        {
                            currBBStkTop--;
                            break;
                        }
                    case JBC_irem:
                    case JBC_idiv:
                        {
                            currBBStkTop = currBBStkTop - 2;
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop++;
                            break;
                        }
                    case JBC_ladd:
                    case JBC_dadd:
                    case JBC_lsub:
                    case JBC_dsub:
                    case JBC_lmul:
                    case JBC_dmul:
                    case JBC_ddiv:
                    case JBC_drem:
                    case JBC_land:
                    case JBC_lor:
                    case JBC_lxor:
                    case JBC_pop2:
                        {
                            currBBStkTop = currBBStkTop - 2;
                            break;
                        }
                    case JBC_lrem:
                    case JBC_ldiv:
                        {
                            currBBStkTop = currBBStkTop - 4;
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop = currBBStkTop + 2;
                            break;
                        }
                    case JBC_ineg:
                    case JBC_lneg:
                    case JBC_fneg:
                    case JBC_dneg:
                    case JBC_iinc:
                    case JBC_i2f:
                    case JBC_l2d:
                    case JBC_f2i:
                    case JBC_d2l:
                    case JBC_int2byte:
                    case JBC_int2char:
                    case JBC_int2short:
                        {
                            break;
                        }
                    case JBC_lcmp:
                    case JBC_dcmpl:
                    case JBC_dcmpg:
                        {
                            currBBStkTop = currBBStkTop - 3;
                            break;
                        }
                    case JBC_ifeq:
                    case JBC_ifne:
                    case JBC_iflt:
                    case JBC_ifge:
                    case JBC_ifgt:
                    case JBC_ifle:
                        {
                            short offset = (short) (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF));
                            if (offset < 0) {
                                if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            }
                            currBBStkTop--;
                            if (offset < 0) {
                                short fallThruBBNum = byteToBlockMap[i + 3];
                                workStk = processBranchBB(fallThruBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                                processNextBlock = false;
                            }
                            brBBNum = byteToBlockMap[i + offset];
                            workStk = processBranchBB(brBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            break;
                        }
                    case JBC_if_icmpeq:
                    case JBC_if_icmpne:
                    case JBC_if_icmplt:
                    case JBC_if_icmpge:
                    case JBC_if_icmpgt:
                    case JBC_if_icmple:
                    case JBC_if_acmpeq:
                    case JBC_if_acmpne:
                        {
                            short offset = (short) (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF));
                            if (offset < 0) {
                                if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            }
                            currBBStkTop = currBBStkTop - 2;
                            if (offset < 0) {
                                short fallThruBBNum = byteToBlockMap[i + 3];
                                workStk = processBranchBB(fallThruBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                                processNextBlock = false;
                            }
                            brBBNum = byteToBlockMap[i + offset];
                            workStk = processBranchBB(brBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            break;
                        }
                    case JBC_ifnull:
                    case JBC_ifnonnull:
                        {
                            short offset = (short) (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF));
                            if (offset < 0) {
                                if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            }
                            currBBStkTop--;
                            if (offset < 0) {
                                short fallThruBBNum = byteToBlockMap[i + 3];
                                workStk = processBranchBB(fallThruBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                                processNextBlock = false;
                            }
                            brBBNum = byteToBlockMap[i + offset];
                            workStk = processBranchBB(brBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            break;
                        }
                    case JBC_goto:
                        {
                            int offset = (int) ((short) (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)));
                            if (offset < 0) {
                                if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            }
                            brBBNum = byteToBlockMap[i + offset];
                            workStk = processBranchBB(brBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            processNextBlock = false;
                            break;
                        }
                    case JBC_goto_w:
                        {
                            int offset = getIntOffset(i, bytecodes);
                            if (offset < 0) {
                                if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            }
                            brBBNum = byteToBlockMap[i + offset];
                            workStk = processBranchBB(brBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            processNextBlock = false;
                            break;
                        }
                    case JBC_tableswitch:
                        {
                            int j = i;
                            opLength = 0;
                            currBBStkTop--;
                            i = i + 1;
                            i = (((i + 3) / 4) * 4);
                            int def = getIntOffset(i - 1, bytecodes);
                            workStk = processBranchBB(byteToBlockMap[j + def], currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            i = i + 4;
                            int low = getIntOffset(i - 1, bytecodes);
                            i = i + 4;
                            int high = getIntOffset(i - 1, bytecodes);
                            i = i + 4;
                            for (int k = 0; k < (high - low + 1); k++) {
                                int l = i + k * 4;
                                int offset = getIntOffset(l - 1, bytecodes);
                                workStk = processBranchBB(byteToBlockMap[j + offset], currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            }
                            processNextBlock = false;
                            i = i + (high - low + 1) * 4;
                            break;
                        }
                    case JBC_lookupswitch:
                        {
                            int j = i;
                            opLength = 0;
                            currBBStkTop--;
                            i = i + 1;
                            i = (((i + 3) / 4) * 4);
                            int def = getIntOffset(i - 1, bytecodes);
                            workStk = processBranchBB(byteToBlockMap[j + def], currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            i = i + 4;
                            int npairs = getIntOffset(i - 1, bytecodes);
                            i = i + 4;
                            for (int k = 0; k < npairs; k++) {
                                int l = i + k * 8 + 4;
                                int offset = getIntOffset(l - 1, bytecodes);
                                workStk = processBranchBB(byteToBlockMap[j + offset], currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
                            }
                            processNextBlock = false;
                            i = i + (npairs) * 8;
                            break;
                        }
                    case JBC_jsr:
                        {
                            processNextBlock = false;
                            short offset = (short) (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF));
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkEmpty, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkEmpty, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = RETURN_ADDRESS;
                            workStk = processJSR(byteToBlockMap[i], i + offset, byteToBlockMap[i + offset], byteToBlockMap[i + 3], bbMaps, currBBStkTop, currBBMap, currBBStkEmpty, blockStkTop, bbPendingRETs, currPendingRET, JSRSubs, workStk);
                            break;
                        }
                    case JBC_jsr_w:
                        {
                            processNextBlock = false;
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkEmpty, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkEmpty, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = RETURN_ADDRESS;
                            int offset = getIntOffset(i, bytecodes);
                            workStk = processJSR(byteToBlockMap[i], i + offset, byteToBlockMap[i + offset], byteToBlockMap[i + 5], bbMaps, currBBStkTop, currBBMap, currBBStkEmpty, blockStkTop, bbPendingRETs, currPendingRET, JSRSubs, workStk);
                            break;
                        }
                    case JBC_ret:
                        {
                            int index = ((int) bytecodes[i + 1]) & 0xFF;
                            currBBMap[index] = SET_TO_NONREFERENCE;
                            processNextBlock = false;
                            int subStart = currPendingRET.JSRSubStartByteIndex;
                            int k;
                            for (k = 0; k < JSRSubNext; k++) {
                                if (JSRSubs[k].subroutineByteCodeStart == subStart) {
                                    JSRSubs[k].newEndMaps(currBBMap, currBBStkTop);
                                    break;
                                }
                            }
                            boolean JSRisinJSRSub = bbPendingRETs[currPendingRET.JSRBBNum] != null;
                            workStk = computeJSRNextMaps(currPendingRET.JSRNextBBNum, currBBMap.length, k, JSRisinJSRSub, bbMaps, blockStkTop, JSRSubs, currBBStkEmpty, workStk);
                            if (JSRisinJSRSub && bbPendingRETs[currPendingRET.JSRNextBBNum] == null) bbPendingRETs[currPendingRET.JSRNextBBNum] = new VM_PendingRETInfo(bbPendingRETs[currPendingRET.JSRBBNum]);
                            break;
                        }
                    case JBC_invokevirtual:
                    case JBC_invokespecial:
                    case JBC_invokeinterface:
                        {
                            int index = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            VM_Method calledMethod = declaringClass.getMethodRef(index);
                            currBBStkTop = processInvoke(calledMethod, i, currBBStkTop, currBBMap, false, inJSRSub, referenceMaps, currPendingRET, blockSeen[currBBNum], currBBStkEmpty);
                            break;
                        }
                    case JBC_invokestatic:
                        {
                            int index = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            VM_Method calledMethod = declaringClass.getMethodRef(index);
                            currBBStkTop = processInvoke(calledMethod, i, currBBStkTop, currBBMap, true, inJSRSub, referenceMaps, currPendingRET, blockSeen[currBBNum], currBBStkEmpty);
                            break;
                        }
                    case JBC_ireturn:
                    case JBC_lreturn:
                    case JBC_freturn:
                    case JBC_dreturn:
                    case JBC_areturn:
                    case JBC_return:
                        {
                            if (VM.UseEpilogueYieldPoints || method.isSynchronized()) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]);
                            processNextBlock = false;
                            break;
                        }
                    case JBC_getstatic:
                        {
                            int index = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            VM_Type fieldType = declaringClass.getFieldRef(index).getType();
                            currBBMap[++currBBStkTop] = fieldType.isReferenceType() ? REFERENCE : NON_REFERENCE;
                            if (fieldType.getStackWords() == 2) currBBMap[++currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_putstatic:
                        {
                            int index = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            VM_Type fieldType = declaringClass.getFieldRef(index).getType();
                            currBBStkTop--;
                            if (fieldType.getStackWords() == 2) currBBStkTop--;
                            break;
                        }
                    case JBC_getfield:
                        {
                            int index = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            VM_Type fieldType = declaringClass.getFieldRef(index).getType();
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop--;
                            currBBMap[++currBBStkTop] = fieldType.isReferenceType() ? REFERENCE : NON_REFERENCE;
                            if (fieldType.getStackWords() == 2) currBBMap[++currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_putfield:
                        {
                            int index = (((int) bytecodes[i + 1]) << 8 | (((int) bytecodes[i + 2]) & 0xFF)) & 0xFFFF;
                            VM_Type fieldType = declaringClass.getFieldRef(index).getType();
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop -= 2;
                            if (fieldType.getStackWords() == 2) currBBStkTop--;
                            break;
                        }
                    case JBC_checkcast:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            break;
                        }
                    case JBC_instanceof:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_new:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop++;
                            currBBMap[currBBStkTop] = REFERENCE;
                            break;
                        }
                    case JBC_iaload:
                    case JBC_faload:
                    case JBC_baload:
                    case JBC_caload:
                    case JBC_saload:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop--;
                            currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_laload:
                    case JBC_daload:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBMap[currBBStkTop - 1] = NON_REFERENCE;
                            break;
                        }
                    case JBC_aaload:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop--;
                            break;
                        }
                    case JBC_iastore:
                    case JBC_fastore:
                    case JBC_aastore:
                    case JBC_bastore:
                    case JBC_castore:
                    case JBC_sastore:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop = currBBStkTop - 3;
                            break;
                        }
                    case JBC_lastore:
                    case JBC_dastore:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop = currBBStkTop - 4;
                            break;
                        }
                    case JBC_newarray:
                    case JBC_anewarray:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBMap[currBBStkTop] = REFERENCE;
                            break;
                        }
                    case JBC_multianewarray:
                        {
                            short dim = (short) (((int) bytecodes[i + 3]) & 0xFF);
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop = currBBStkTop - dim + 1;
                            currBBMap[currBBStkTop] = REFERENCE;
                            break;
                        }
                    case JBC_arraylength:
                        {
                            currBBMap[currBBStkTop] = NON_REFERENCE;
                            break;
                        }
                    case JBC_athrow:
                        {
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            currBBStkTop = currBBStkEmpty + 1;
                            currBBMap[currBBStkTop] = REFERENCE;
                            processNextBlock = false;
                            break;
                        }
                    case JBC_monitorenter:
                    case JBC_monitorexit:
                        {
                            currBBStkTop--;
                            if (!inJSRSub) referenceMaps.recordStkMap(i, currBBMap, currBBStkTop, blockSeen[currBBNum]); else referenceMaps.recordJSRSubroutineMap(i, currBBMap, currBBStkTop, currPendingRET.returnAddressLocation, blockSeen[currBBNum]);
                            break;
                        }
                    case JBC_wide:
                        {
                            int wopcode = ((int) bytecodes[i + 1]) & 0xFF;
                            opLength = JBC_length[wopcode];
                            opLength += opLength;
                            if (wopcode != JBC_iinc) {
                                int index = (((int) bytecodes[i + 2]) << 8 | (((int) bytecodes[i + 3]) & 0xFF)) & 0xFFFF;
                                switch(wopcode) {
                                    case JBC_iload:
                                    case JBC_fload:
                                        {
                                            currBBStkTop++;
                                            currBBMap[currBBStkTop] = NON_REFERENCE;
                                            break;
                                        }
                                    case JBC_lload:
                                    case JBC_dload:
                                        {
                                            currBBStkTop++;
                                            currBBMap[currBBStkTop] = NON_REFERENCE;
                                            currBBStkTop++;
                                            currBBMap[currBBStkTop] = NON_REFERENCE;
                                            break;
                                        }
                                    case JBC_aload:
                                        {
                                            currBBStkTop++;
                                            currBBMap[currBBStkTop] = REFERENCE;
                                            break;
                                        }
                                    case JBC_istore:
                                    case JBC_fstore:
                                        {
                                            if (!inJSRSub) currBBMap[index] = NON_REFERENCE; else currBBMap[index] = SET_TO_NONREFERENCE;
                                            currBBStkTop--;
                                            break;
                                        }
                                    case JBC_lstore:
                                    case JBC_dstore:
                                        {
                                            if (!inJSRSub) {
                                                currBBMap[index] = NON_REFERENCE;
                                                currBBMap[index + 1] = NON_REFERENCE;
                                            } else {
                                                currBBMap[index] = SET_TO_NONREFERENCE;
                                                currBBMap[index + 1] = SET_TO_NONREFERENCE;
                                            }
                                            currBBStkTop = currBBStkTop - 2;
                                            break;
                                        }
                                    case JBC_astore:
                                        {
                                            currBBMap[index] = currBBMap[currBBStkTop];
                                            currBBStkTop--;
                                            break;
                                        }
                                }
                            }
                            break;
                        }
                    default:
                        {
                            System.out.println("Unknown opcode:" + opcode);
                            System.exit(10);
                        }
                }
                i = i + opLength;
            }
            blockSeen[currBBNum] = true;
            if (processNextBlock) {
                short fallThruBBNum = byteToBlockMap[i];
                workStk = processBranchBB(fallThruBBNum, currBBStkTop, currBBMap, currBBStkEmpty, inJSRSub, bbMaps, blockStkTop, currPendingRET, bbPendingRETs, workStk);
            }
            if ((workStkTop == -1) && !handlersAllDone) {
                for (i = 0; i < tryHandlerLength; i++) {
                    if (handlerProcessed[i] || bbMaps[byteToBlockMap[tryHandlerPC[i]]] == null) continue; else break;
                }
                if (i == tryHandlerLength) handlersAllDone = true; else {
                    int considerIndex = i;
                    while (i != tryHandlerLength) {
                        int tryStart = tryStartPC[considerIndex];
                        int tryEnd = tryEndPC[considerIndex];
                        for (i = 0; i < tryHandlerLength; i++) if (!handlerProcessed[i] && tryStart <= tryHandlerPC[i] && tryHandlerPC[i] < tryEnd && bbMaps[byteToBlockMap[tryHandlerPC[i]]] != null) break;
                        if (i != tryHandlerLength) considerIndex = i;
                    }
                    short blockNum = byteToBlockMap[tryHandlerPC[considerIndex]];
                    handlerProcessed[considerIndex] = true;
                    workStk = addToWorkStk(blockNum, workStk);
                }
            }
        }
        referenceMaps.recordingComplete();
        return;
    }
