    private Object getScalarDeltaAbsValue(SnapshotAttributeWriteAbsValue writeAbsValue, SnapshotAttributeReadAbsValue readAbsValue, boolean manageAllTypes) {
        if (writeAbsValue == null || readAbsValue == null) {
            if (manageAllTypes) {
                if (writeAbsValue == null && readAbsValue == null) {
                    return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                } else {
                    return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                }
            } else {
                return null;
            }
        }
        switch(this.getDataType()) {
            case TangoConst.Tango_DEV_USHORT:
            case TangoConst.Tango_DEV_SHORT:
                try {
                    Short writeDouble = (Short) writeAbsValue.getValue();
                    Short readDouble = (Short) readAbsValue.getValue();
                    if (writeDouble == null || readDouble == null) {
                        if (manageAllTypes) {
                            if (writeDouble == null && readDouble == null) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    return new Short((short) Math.abs(readDouble.shortValue() - writeDouble.shortValue()));
                } catch (ClassCastException e) {
                    String writeDouble_s = "" + writeAbsValue.getValue();
                    String readDouble_s = "" + readAbsValue.getValue();
                    if ("null".equals(writeDouble_s) || writeDouble_s.equals("") || "null".equals(readDouble_s) || readDouble_s.equals("")) {
                        if (manageAllTypes) {
                            if ("null".equals(writeDouble_s) && "null".equals(readDouble_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else if ("".equals(writeDouble_s) && "".equals(readDouble_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    double readDouble = Double.parseDouble(readDouble_s);
                    double writeDouble = Double.parseDouble(writeDouble_s);
                    return new Short((short) Math.abs(readDouble - writeDouble));
                }
            case TangoConst.Tango_DEV_DOUBLE:
                try {
                    Double writeDouble = (Double) writeAbsValue.getValue();
                    Double readDouble = (Double) readAbsValue.getValue();
                    if (writeDouble == null || readDouble == null) {
                        if (manageAllTypes) {
                            if (writeDouble == null && readDouble == null) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    return new Double(Math.abs(readDouble.doubleValue() - writeDouble.doubleValue()));
                } catch (ClassCastException e) {
                    String writeDouble_s = "" + writeAbsValue.getValue();
                    String readDouble_s = "" + readAbsValue.getValue();
                    if ("null".equals(writeDouble_s) || writeDouble_s.equals("") || "null".equals(readDouble_s) || readDouble_s.equals("")) {
                        if (manageAllTypes) {
                            if ("null".equals(writeDouble_s) && "null".equals(readDouble_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else if ("".equals(writeDouble_s) && "".equals(readDouble_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    double readDouble = Double.parseDouble(readDouble_s);
                    double writeDouble = Double.parseDouble(writeDouble_s);
                    return new Double(Math.abs(readDouble - writeDouble));
                }
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                try {
                    Integer writeLong = (Integer) writeAbsValue.getValue();
                    Integer readLong = (Integer) readAbsValue.getValue();
                    if (writeLong == null || readLong == null) {
                        if (manageAllTypes) {
                            if (writeLong == null && readLong == null) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    return new Integer(Math.abs(readLong.intValue() - writeLong.intValue()));
                } catch (ClassCastException e) {
                    String writeLong_s = "" + writeAbsValue.getValue();
                    String readLong_s = "" + readAbsValue.getValue();
                    if ("null".equals(writeLong_s) || writeLong_s.equals("") || "null".equals(readLong_s) || readLong_s.equals("")) {
                        if (manageAllTypes) {
                            if ("null".equals(writeLong_s) && "null".equals(readLong_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else if ("".equals(writeLong_s) && "".equals(readLong_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    double readDouble = Double.parseDouble(readLong_s);
                    double writeDouble = Double.parseDouble(writeLong_s);
                    return new Integer((int) Math.abs(readDouble - writeDouble));
                }
            case TangoConst.Tango_DEV_FLOAT:
                try {
                    Float writeFloat = (Float) writeAbsValue.getValue();
                    Float readFloat = (Float) readAbsValue.getValue();
                    if (writeFloat == null || readFloat == null) {
                        if (manageAllTypes) {
                            if (writeFloat == null && readFloat == null) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    return new Float(Math.abs(readFloat.longValue() - writeFloat.longValue()));
                } catch (ClassCastException e) {
                    String writeFloat_s = "" + writeAbsValue.getValue();
                    String readFloat_s = "" + readAbsValue.getValue();
                    if ("null".equals(writeFloat_s) || writeFloat_s.equals("") || "null".equals(readFloat_s) || readFloat_s.equals("")) {
                        if (manageAllTypes) {
                            if ("null".equals(writeFloat_s) && "null".equals(readFloat_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else if ("".equals(writeFloat_s) && "".equals(readFloat_s)) {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                            } else {
                                return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                            }
                        } else {
                            return null;
                        }
                    }
                    float readFloat = Float.parseFloat(readFloat_s);
                    float writeFloat = Float.parseFloat(writeFloat_s);
                    return new Float(Math.abs(readFloat - writeFloat));
                }
            default:
                if (manageAllTypes) {
                    Object write = writeAbsValue.getValue();
                    Object read = readAbsValue.getValue();
                    if (write == null && read == null) {
                        if (write == null && read == null) {
                            return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else {
                            return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                        }
                    } else {
                        if (write != null && write.equals(read)) {
                            return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_SAME");
                        } else {
                            return Messages.getMessage("SNAPSHOT_COMPARE_VALUE_DIFFERENT");
                        }
                    }
                } else {
                    return null;
                }
        }
    }
