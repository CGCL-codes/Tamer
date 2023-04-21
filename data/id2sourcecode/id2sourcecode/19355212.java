    private Object getScalarDeltaValue(SnapshotAttributeWriteValue writeValue, SnapshotAttributeReadValue readValue, boolean manageAllTypes) {
        if (writeValue == null || readValue == null) {
            if (manageAllTypes) {
                if (writeValue == null && readValue == null) {
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
                    Short writeDouble = (Short) writeValue.getValue();
                    Short readDouble = (Short) readValue.getValue();
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
                    return new Short((short) (readDouble.shortValue() - writeDouble.shortValue()));
                } catch (ClassCastException e) {
                    String writeDouble_s = "" + writeValue.getValue();
                    String readDouble_s = "" + readValue.getValue();
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
                    return new Short((short) (readDouble - writeDouble));
                }
            case TangoConst.Tango_DEV_DOUBLE:
                try {
                    Double writeDouble = (Double) writeValue.getValue();
                    Double readDouble = (Double) readValue.getValue();
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
                    return new Double(readDouble.doubleValue() - writeDouble.doubleValue());
                } catch (ClassCastException e) {
                    String writeDouble_s = "" + writeValue.getValue();
                    String readDouble_s = "" + readValue.getValue();
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
                    return new Double(readDouble - writeDouble);
                }
            case TangoConst.Tango_DEV_ULONG:
            case TangoConst.Tango_DEV_LONG:
                try {
                    Integer writeLong = (Integer) writeValue.getValue();
                    Integer readLong = (Integer) readValue.getValue();
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
                    return new Integer(readLong.intValue() - writeLong.intValue());
                } catch (ClassCastException e) {
                    String writeLong_s = "" + writeValue.getValue();
                    String readLong_s = "" + readValue.getValue();
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
                    return new Integer((int) (readDouble - writeDouble));
                }
            case TangoConst.Tango_DEV_FLOAT:
                try {
                    Float writeFloat = (Float) writeValue.getValue();
                    Float readFloat = (Float) readValue.getValue();
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
                    return new Float(readFloat.longValue() - writeFloat.longValue());
                } catch (ClassCastException e) {
                    String writeFloat_s = "" + writeValue.getValue();
                    String readFloat_s = "" + readValue.getValue();
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
                    return new Float(readFloat - writeFloat);
                }
            default:
                if (manageAllTypes) {
                    Object write = writeValue.getValue();
                    Object read = readValue.getValue();
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
