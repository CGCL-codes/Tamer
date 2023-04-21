    public static Object convert(Object arg, Class destClass) {
        if (destClass == null) {
            return arg;
        }
        Class argHeldType = null;
        if (arg != null) {
            argHeldType = getHolderValueType(arg.getClass());
        }
        if (arg != null && argHeldType == null && destClass.isAssignableFrom(arg.getClass())) {
            return arg;
        }
        if (log.isDebugEnabled()) {
            String clsName = "null";
            if (arg != null) clsName = arg.getClass().getName();
            log.debug(Messages.getMessage("convert00", clsName, destClass.getName()));
        }
        Object destValue = null;
        if (arg instanceof ConvertCache) {
            destValue = ((ConvertCache) arg).getConvertedValue(destClass);
            if (destValue != null) return destValue;
        }
        Class destHeldType = getHolderValueType(destClass);
        if (arg instanceof HexBinary && destClass == byte[].class) {
            return ((HexBinary) arg).getBytes();
        } else if (arg instanceof byte[] && destClass == HexBinary.class) {
            return new HexBinary((byte[]) arg);
        }
        if (arg instanceof Calendar && destClass == Date.class) {
            return ((Calendar) arg).getTime();
        }
        if (arg instanceof Date && destClass == Calendar.class) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) arg);
            return calendar;
        }
        if (arg instanceof Calendar && destClass == java.sql.Date.class) {
            return new java.sql.Date(((Calendar) arg).getTime().getTime());
        }
        if (arg instanceof HashMap && destClass == Hashtable.class) {
            return new Hashtable((HashMap) arg);
        }
        if (isAttachmentSupported() && (arg instanceof InputStream || arg instanceof AttachmentPart || arg instanceof DataHandler)) {
            try {
                String destName = destClass.getName();
                if (destClass == String.class || destClass == OctetStream.class || destClass == byte[].class || destClass == Image.class || destClass == Source.class || destClass == DataHandler.class || destName.equals("javax.mail.internet.MimeMultipart")) {
                    DataHandler handler = null;
                    if (arg instanceof AttachmentPart) {
                        handler = ((AttachmentPart) arg).getDataHandler();
                    } else if (arg instanceof DataHandler) {
                        handler = (DataHandler) arg;
                    }
                    if (destClass == Image.class) {
                        InputStream is = handler.getInputStream();
                        if (is.available() == 0) {
                            return null;
                        } else {
                            ImageIO imageIO = ImageIOFactory.getImageIO();
                            if (imageIO != null) {
                                return getImageFromStream(is);
                            } else {
                                log.info(Messages.getMessage("needImageIO"));
                                return arg;
                            }
                        }
                    } else if (destClass == javax.xml.transform.Source.class) {
                        return new StreamSource(handler.getInputStream());
                    } else if (destClass == OctetStream.class || destClass == byte[].class) {
                        InputStream in = null;
                        if (arg instanceof InputStream) {
                            in = (InputStream) arg;
                        } else {
                            in = handler.getInputStream();
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int byte1 = -1;
                        while ((byte1 = in.read()) != -1) baos.write(byte1);
                        return new OctetStream(baos.toByteArray());
                    } else if (destClass == DataHandler.class) {
                        return handler;
                    } else {
                        return handler.getContent();
                    }
                }
            } catch (IOException ioe) {
            } catch (SOAPException se) {
            }
        }
        if (arg != null && destClass.isArray() && !destClass.getComponentType().equals(Object.class) && destClass.getComponentType().isAssignableFrom(arg.getClass())) {
            Object array = Array.newInstance(destClass.getComponentType(), 1);
            Array.set(array, 0, arg);
            return array;
        }
        if (arg != null && destClass.isArray()) {
            Object newArg = ArrayUtil.convertObjectToArray(arg, destClass);
            if (newArg == null || (newArg != ArrayUtil.NON_CONVERTABLE && newArg != arg)) {
                return newArg;
            }
        }
        if (arg != null && arg.getClass().isArray()) {
            Object newArg = ArrayUtil.convertArrayToObject(arg, destClass);
            if (newArg != null) return newArg;
        }
        if (!(arg instanceof Collection || (arg != null && arg.getClass().isArray())) && ((destHeldType == null && argHeldType == null) || (destHeldType != null && argHeldType != null))) {
            return arg;
        }
        if (destHeldType != null) {
            Object newArg = convert(arg, destHeldType);
            Object argHolder = null;
            try {
                argHolder = destClass.newInstance();
                setHolderValue(argHolder, newArg);
                return argHolder;
            } catch (Exception e) {
                return arg;
            }
        } else if (argHeldType != null) {
            try {
                Object newArg = getHolderValue(arg);
                return convert(newArg, destClass);
            } catch (HolderException e) {
                return arg;
            }
        }
        if (arg instanceof ConvertCache && ((ConvertCache) arg).getDestClass() != destClass) {
            Class hintClass = ((ConvertCache) arg).getDestClass();
            if (hintClass != null && hintClass.isArray() && destClass.isArray() && destClass.isAssignableFrom(hintClass)) {
                destClass = hintClass;
                destValue = ((ConvertCache) arg).getConvertedValue(destClass);
                if (destValue != null) return destValue;
            }
        }
        if (arg == null) {
            return arg;
        }
        int length = 0;
        if (arg.getClass().isArray()) {
            length = Array.getLength(arg);
        } else {
            length = ((Collection) arg).size();
        }
        if (destClass.isArray()) {
            if (destClass.getComponentType().isPrimitive()) {
                Object array = Array.newInstance(destClass.getComponentType(), length);
                if (arg.getClass().isArray()) {
                    for (int i = 0; i < length; i++) {
                        Array.set(array, i, Array.get(arg, i));
                    }
                } else {
                    int idx = 0;
                    for (Iterator i = ((Collection) arg).iterator(); i.hasNext(); ) {
                        Array.set(array, idx++, i.next());
                    }
                }
                destValue = array;
            } else {
                Object[] array;
                try {
                    array = (Object[]) Array.newInstance(destClass.getComponentType(), length);
                } catch (Exception e) {
                    return arg;
                }
                if (arg.getClass().isArray()) {
                    for (int i = 0; i < length; i++) {
                        array[i] = convert(Array.get(arg, i), destClass.getComponentType());
                    }
                } else {
                    int idx = 0;
                    for (Iterator i = ((Collection) arg).iterator(); i.hasNext(); ) {
                        array[idx++] = convert(i.next(), destClass.getComponentType());
                    }
                }
                destValue = array;
            }
        } else if (Collection.class.isAssignableFrom(destClass)) {
            Collection newList = null;
            try {
                if (destClass == Collection.class || destClass == List.class) {
                    newList = new ArrayList();
                } else if (destClass == Set.class) {
                    newList = new HashSet();
                } else {
                    newList = (Collection) destClass.newInstance();
                }
            } catch (Exception e) {
                return arg;
            }
            if (arg.getClass().isArray()) {
                for (int j = 0; j < length; j++) {
                    newList.add(Array.get(arg, j));
                }
            } else {
                for (Iterator j = ((Collection) arg).iterator(); j.hasNext(); ) {
                    newList.add(j.next());
                }
            }
            destValue = newList;
        } else {
            destValue = arg;
        }
        if (arg instanceof ConvertCache) {
            ((ConvertCache) arg).setConvertedValue(destClass, destValue);
        }
        return destValue;
    }
