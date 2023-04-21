    public void read(InputStream in) throws IOException, Pack200Exception {
        AttributeLayoutMap attributeDefinitionMap = segment.getAttrDefinitionBands().getAttributeDefinitionMap();
        int classCount = header.getClassCount();
        long[][] methodFlags = segment.getClassBands().getMethodFlags();
        int bcCaseCountCount = 0;
        int bcByteCount = 0;
        int bcShortCount = 0;
        int bcLocalCount = 0;
        int bcLabelCount = 0;
        int bcIntRefCount = 0;
        int bcFloatRefCount = 0;
        int bcLongRefCount = 0;
        int bcDoubleRefCount = 0;
        int bcStringRefCount = 0;
        int bcClassRefCount = 0;
        int bcFieldRefCount = 0;
        int bcMethodRefCount = 0;
        int bcIMethodRefCount = 0;
        int bcThisFieldCount = 0;
        int bcSuperFieldCount = 0;
        int bcThisMethodCount = 0;
        int bcSuperMethodCount = 0;
        int bcInitRefCount = 0;
        int bcEscCount = 0;
        int bcEscRefCount = 0;
        AttributeLayout abstractModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_ABSTRACT, AttributeLayout.CONTEXT_METHOD);
        AttributeLayout nativeModifier = attributeDefinitionMap.getAttributeLayout(AttributeLayout.ACC_NATIVE, AttributeLayout.CONTEXT_METHOD);
        methodByteCodePacked = new byte[classCount][][];
        int bcParsed = 0;
        List switchIsTableSwitch = new ArrayList();
        wideByteCodes = new ArrayList();
        for (int c = 0; c < classCount; c++) {
            int numberOfMethods = methodFlags[c].length;
            methodByteCodePacked[c] = new byte[numberOfMethods][];
            for (int m = 0; m < numberOfMethods; m++) {
                long methodFlag = methodFlags[c][m];
                if (!abstractModifier.matches(methodFlag) && !nativeModifier.matches(methodFlag)) {
                    ByteArrayOutputStream codeBytes = new ByteArrayOutputStream();
                    byte code;
                    while ((code = (byte) (0xff & in.read())) != -1) codeBytes.write(code);
                    methodByteCodePacked[c][m] = codeBytes.toByteArray();
                    bcParsed += methodByteCodePacked[c][m].length;
                    int[] codes = new int[methodByteCodePacked[c][m].length];
                    for (int i = 0; i < codes.length; i++) {
                        codes[i] = methodByteCodePacked[c][m][i] & 0xff;
                    }
                    for (int i = 0; i < methodByteCodePacked[c][m].length; i++) {
                        int codePacked = 0xff & methodByteCodePacked[c][m][i];
                        switch(codePacked) {
                            case 16:
                            case 188:
                                bcByteCount++;
                                break;
                            case 17:
                                bcShortCount++;
                                break;
                            case 18:
                            case 19:
                                bcStringRefCount++;
                                break;
                            case 234:
                            case 237:
                                bcIntRefCount++;
                                break;
                            case 235:
                            case 238:
                                bcFloatRefCount++;
                                break;
                            case 197:
                                bcByteCount++;
                            case 233:
                            case 236:
                            case 187:
                            case 189:
                            case 192:
                            case 193:
                                bcClassRefCount++;
                                break;
                            case 20:
                                bcLongRefCount++;
                                break;
                            case 239:
                                bcDoubleRefCount++;
                                break;
                            case 169:
                                bcLocalCount++;
                                break;
                            case 167:
                            case 168:
                            case 200:
                            case 201:
                                bcLabelCount++;
                                break;
                            case 170:
                                switchIsTableSwitch.add(new Boolean(true));
                                bcCaseCountCount++;
                                bcLabelCount++;
                                break;
                            case 171:
                                switchIsTableSwitch.add(new Boolean(false));
                                bcCaseCountCount++;
                                bcLabelCount++;
                                break;
                            case 178:
                            case 179:
                            case 180:
                            case 181:
                                bcFieldRefCount++;
                                break;
                            case 182:
                            case 183:
                            case 184:
                                bcMethodRefCount++;
                                break;
                            case 185:
                                bcIMethodRefCount++;
                                break;
                            case 202:
                            case 203:
                            case 204:
                            case 205:
                            case 209:
                            case 210:
                            case 211:
                            case 212:
                                bcThisFieldCount++;
                                break;
                            case 206:
                            case 207:
                            case 208:
                            case 213:
                            case 214:
                            case 215:
                                bcThisMethodCount++;
                                break;
                            case 216:
                            case 217:
                            case 218:
                            case 219:
                            case 223:
                            case 224:
                            case 225:
                            case 226:
                                bcSuperFieldCount++;
                                break;
                            case 220:
                            case 221:
                            case 222:
                            case 227:
                            case 228:
                            case 229:
                                bcSuperMethodCount++;
                                break;
                            case 132:
                                bcLocalCount++;
                                bcByteCount++;
                                break;
                            case 196:
                                int nextInstruction = 0xff & methodByteCodePacked[c][m][i + 1];
                                wideByteCodes.add(new Integer(nextInstruction));
                                if (nextInstruction == 132) {
                                    bcLocalCount++;
                                    bcShortCount++;
                                } else if (endsWithLoad(nextInstruction) || endsWithStore(nextInstruction) || nextInstruction == 169) {
                                    bcLocalCount++;
                                } else {
                                    segment.log(Segment.LOG_LEVEL_VERBOSE, "Found unhandled " + ByteCode.getByteCode(nextInstruction));
                                }
                                i++;
                                break;
                            case 230:
                            case 231:
                            case 232:
                                bcInitRefCount++;
                                break;
                            case 253:
                                bcEscRefCount++;
                                break;
                            case 254:
                                bcEscCount++;
                                break;
                            default:
                                if (endsWithLoad(codePacked) || endsWithStore(codePacked)) {
                                    bcLocalCount++;
                                } else if (startsWithIf(codePacked)) {
                                    bcLabelCount++;
                                }
                        }
                    }
                }
            }
        }
        bcCaseCount = decodeBandInt("bc_case_count", in, Codec.UNSIGNED5, bcCaseCountCount);
        int bcCaseValueCount = 0;
        for (int i = 0; i < bcCaseCount.length; i++) {
            boolean isTableSwitch = ((Boolean) switchIsTableSwitch.get(i)).booleanValue();
            if (isTableSwitch) {
                bcCaseValueCount += 1;
            } else {
                bcCaseValueCount += bcCaseCount[i];
            }
        }
        bcCaseValue = decodeBandInt("bc_case_value", in, Codec.DELTA5, bcCaseValueCount);
        for (int index = 0; index < bcCaseCountCount; index++) {
            bcLabelCount += bcCaseCount[index];
        }
        bcByte = decodeBandInt("bc_byte", in, Codec.BYTE1, bcByteCount);
        bcShort = decodeBandInt("bc_short", in, Codec.DELTA5, bcShortCount);
        bcLocal = decodeBandInt("bc_local", in, Codec.UNSIGNED5, bcLocalCount);
        bcLabel = decodeBandInt("bc_label", in, Codec.BRANCH5, bcLabelCount);
        bcIntRef = decodeBandInt("bc_intref", in, Codec.DELTA5, bcIntRefCount);
        bcFloatRef = decodeBandInt("bc_floatref", in, Codec.DELTA5, bcFloatRefCount);
        bcLongRef = decodeBandInt("bc_longref", in, Codec.DELTA5, bcLongRefCount);
        bcDoubleRef = decodeBandInt("bc_doubleref", in, Codec.DELTA5, bcDoubleRefCount);
        bcStringRef = decodeBandInt("bc_stringref", in, Codec.DELTA5, bcStringRefCount);
        bcClassRef = decodeBandInt("bc_classref", in, Codec.UNSIGNED5, bcClassRefCount);
        bcFieldRef = decodeBandInt("bc_fieldref", in, Codec.DELTA5, bcFieldRefCount);
        bcMethodRef = decodeBandInt("bc_methodref", in, Codec.UNSIGNED5, bcMethodRefCount);
        bcIMethodRef = decodeBandInt("bc_imethodref", in, Codec.DELTA5, bcIMethodRefCount);
        bcThisField = decodeBandInt("bc_thisfield", in, Codec.UNSIGNED5, bcThisFieldCount);
        bcSuperField = decodeBandInt("bc_superfield", in, Codec.UNSIGNED5, bcSuperFieldCount);
        bcThisMethod = decodeBandInt("bc_thismethod", in, Codec.UNSIGNED5, bcThisMethodCount);
        bcSuperMethod = decodeBandInt("bc_supermethod", in, Codec.UNSIGNED5, bcSuperMethodCount);
        bcInitRef = decodeBandInt("bc_initref", in, Codec.UNSIGNED5, bcInitRefCount);
        bcEscRef = decodeBandInt("bc_escref", in, Codec.UNSIGNED5, bcEscRefCount);
        bcEscRefSize = decodeBandInt("bc_escrefsize", in, Codec.UNSIGNED5, bcEscRefCount);
        bcEscSize = decodeBandInt("bc_escsize", in, Codec.UNSIGNED5, bcEscCount);
        bcEscByte = decodeBandInt("bc_escbyte", in, Codec.BYTE1, bcEscSize);
    }
