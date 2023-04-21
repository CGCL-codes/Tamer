    public PatternFormat(final Properties properties) {
        final List<FormatElement> patternList = new ArrayList<FormatElement>();
        char[] patternChars = new char[0];
        int tmpArrayDepth = DEFAULT_ARRAY_DEPTH;
        String patternStr = properties.getProperty("pattern");
        if (patternStr != null) {
            patternChars = patternStr.toCharArray();
        }
        try {
            tmpArrayDepth = Integer.valueOf(properties.getProperty("arrayDepth"));
        } catch (NumberFormatException e) {
        }
        this.arrayDepth = tmpArrayDepth;
        traceThrowable = Boolean.valueOf(properties.getProperty("traceThrowable"));
        final AtomicBoolean leftJustifyRef = new AtomicBoolean(false);
        final AtomicInteger maxLengthRef = new AtomicInteger(Integer.MAX_VALUE);
        final AtomicInteger minLengthRef = new AtomicInteger(0);
        final AtomicInteger precisionRef = new AtomicInteger(0);
        final StringBuilder builder = new StringBuilder();
        boolean literal = true;
        int i = 0;
        while (i < patternChars.length) {
            if (literal) {
                builder.setLength(0);
                while (patternChars[i] != '%') {
                    builder.append(patternChars[i]);
                    i++;
                }
                if (builder.length() > 0) {
                    patternList.add(new FormatElement() {

                        private final String string = builder.toString();

                        public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                            message.append(string);
                        }
                    });
                }
                literal = false;
            } else {
                char[] tempArr;
                int startIndex = i;
                leftJustifyRef.set(false);
                minLengthRef.set(0);
                maxLengthRef.set(Integer.MAX_VALUE);
                if (patternChars[i] == '-') {
                    leftJustifyRef.set(true);
                    startIndex++;
                    i++;
                }
                while (Arrays.binarySearch(DIGITS, patternChars[i]) >= 0) {
                    i++;
                }
                if (i > startIndex) {
                    int length = i - startIndex;
                    tempArr = new char[length];
                    System.arraycopy(patternChars, startIndex, tempArr, 0, length);
                    minLengthRef.set(Integer.parseInt(new String(tempArr)));
                }
                if (patternChars[i] == '.' && Arrays.binarySearch(DIGITS, patternChars[++i]) >= 0) {
                    startIndex = i;
                    while (Arrays.binarySearch(DIGITS, patternChars[i]) >= 0) {
                        i++;
                    }
                    int length = i - startIndex;
                    tempArr = new char[length];
                    System.arraycopy(patternChars, startIndex, tempArr, 0, length);
                    maxLengthRef.set(Integer.parseInt(new String(tempArr)));
                }
                switch(patternChars[i]) {
                    case '%':
                        patternList.add(new FormatElement() {

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                message.append('%');
                            }
                        });
                        break;
                    case 'c':
                        builder.setLength(0);
                        if (patternChars[i + 1] == '{') {
                            i = i + 2;
                            while (patternChars[i] != '}') {
                                builder.append(patternChars[i]);
                                i++;
                            }
                        }
                        precisionRef.set(0);
                        try {
                            precisionRef.set(Integer.parseInt(builder.toString()));
                        } catch (NumberFormatException e) {
                        }
                        if (precisionRef.get() < 1) {
                            patternList.add(new FormatElement() {

                                private final int maxLength = maxLengthRef.get();

                                private final int minLength = minLengthRef.get();

                                private final boolean leftJustify = leftJustifyRef.get();

                                public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                    message.append(justify(logName, maxLength, minLength, leftJustify));
                                }
                            });
                        } else {
                            patternList.add(new FormatElement() {

                                private final int maxLength = maxLengthRef.get();

                                private final int minLength = minLengthRef.get();

                                private final int precision = precisionRef.get();

                                private final boolean leftJustify = leftJustifyRef.get();

                                public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                    int count = 0;
                                    int idx = 0;
                                    for (int i = logName.length() - 1; i > 0; i--) {
                                        if ('.' == logName.charAt(i)) {
                                            count++;
                                            if (count == precision) {
                                                idx = i + 1;
                                                break;
                                            }
                                        }
                                    }
                                    message.append(justify(logName.substring(idx), maxLength, minLength, leftJustify));
                                }
                            });
                        }
                        break;
                    case 'd':
                        builder.setLength(0);
                        if (patternChars[i + 1] == '{') {
                            i = i + 2;
                            while (patternChars[i] != '}') {
                                builder.append(patternChars[i]);
                                i++;
                            }
                        } else {
                            builder.append("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                        }
                        patternList.add(new FormatElement() {

                            private final DateTimeFormatter format = DateTimeFormat.forPattern(builder.toString());

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                message.append(justify(format.print(System.currentTimeMillis()), maxLength, minLength, leftJustify));
                            }
                        });
                        break;
                    case 'n':
                        patternList.add(new FormatElement() {

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                message.append(EOL);
                            }
                        });
                        break;
                    case 'p':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                message.append(justify(levelName, maxLength, minLength, leftJustify));
                            }
                        });
                        break;
                    case 't':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                message.append(justify(Thread.currentThread().getName(), maxLength, minLength, leftJustify));
                            }
                        });
                        break;
                    case 'm':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            private void append(final StringBuilder message, final int arrayDepth, final Object object) {
                                if (object == null) {
                                    message.append("null");
                                } else if (traceThrowable && object instanceof Throwable) {
                                    final Throwable throwable = (Throwable) object;
                                    message.append(throwable.getMessage());
                                    message.append(EOL);
                                    for (final StackTraceElement element : throwable.getStackTrace()) {
                                        message.append('\t');
                                        message.append(element.toString());
                                        message.append(EOL);
                                    }
                                } else if (arrayDepth > 0 && object.getClass().isArray()) {
                                    final Class componentType = object.getClass().getComponentType();
                                    message.append("{ ");
                                    if (Object.class.isAssignableFrom(componentType)) {
                                        for (final Object value : (Object[]) object) {
                                            append(message, arrayDepth - 1, value);
                                            message.append(", ");
                                        }
                                    } else {
                                        if (boolean.class == componentType) {
                                            for (final boolean value : (boolean[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (byte.class == componentType) {
                                            for (final byte value : (byte[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (char.class == componentType) {
                                            for (final char value : (char[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (short.class == componentType) {
                                            for (final short value : (short[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (int.class == componentType) {
                                            for (final int value : (int[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (long.class == componentType) {
                                            for (final long value : (long[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (float.class == componentType) {
                                            for (final float value : (float[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        } else if (double.class == componentType) {
                                            for (final double value : (double[]) object) {
                                                append(message, 0, value);
                                                message.append(", ");
                                            }
                                        }
                                    }
                                    message.setCharAt(message.length() - 2, ' ');
                                    message.setCharAt(message.length() - 1, '}');
                                } else {
                                    message.append(object.toString());
                                }
                            }

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... objects) {
                                if (objects == null) {
                                    message.append(justify("null", maxLength, minLength, leftJustify));
                                    return;
                                }
                                final StringBuilder builder = new StringBuilder();
                                int lastIndex = objects.length - 1;
                                for (int i = 0; i < objects.length; i++) {
                                    append(builder, arrayDepth, objects[i]);
                                    if (i < lastIndex) {
                                        builder.append(' ');
                                    }
                                }
                                message.append(justify(builder.toString(), maxLength, minLength, leftJustify));
                            }
                        });
                        break;
                    case 'C':
                        builder.setLength(0);
                        if (patternChars[i + 1] == '{') {
                            i = i + 2;
                            while (patternChars[i] != '}') {
                                builder.append(patternChars[i]);
                                i++;
                            }
                        }
                        precisionRef.set(0);
                        try {
                            precisionRef.set(Integer.parseInt(builder.toString()));
                        } catch (NumberFormatException e) {
                        }
                        if (precisionRef.get() < 1) {
                            patternList.add(new FormatElement() {

                                private final int maxLength = maxLengthRef.get();

                                private final int minLength = minLengthRef.get();

                                private final boolean leftJustify = leftJustifyRef.get();

                                public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                    final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                                    int caller = 3;
                                    for (int i = 3; i < stackTrace.length; i++) {
                                        if (LOGGER_CLASS_NAME.equals(stackTrace[i].getClassName())) {
                                            caller = i + 1;
                                        }
                                    }
                                    if (caller >= stackTrace.length) {
                                        caller = stackTrace.length - 1;
                                    }
                                    message.append(justify(stackTrace[caller].getClassName(), maxLength, minLength, leftJustify));
                                }
                            });
                        } else {
                            patternList.add(new FormatElement() {

                                private final int maxLength = maxLengthRef.get();

                                private final int minLength = minLengthRef.get();

                                private final int precision = precisionRef.get();

                                private final boolean leftJustify = leftJustifyRef.get();

                                public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                    final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                                    int caller = 3;
                                    for (int i = 3; i < stackTrace.length; i++) {
                                        if (LOGGER_CLASS_NAME.equals(stackTrace[i].getClassName())) {
                                            caller = i + 1;
                                        }
                                    }
                                    if (caller >= stackTrace.length) {
                                        caller = stackTrace.length - 1;
                                    }
                                    final String className = stackTrace[caller].getClassName();
                                    int count = 0;
                                    int idx = 0;
                                    for (int i = className.length() - 1; i > 0; i--) {
                                        if ('.' == className.charAt(i)) {
                                            count++;
                                            if (count == precision) {
                                                idx = i + 1;
                                                break;
                                            }
                                        }
                                    }
                                    message.append(justify(className.substring(idx), maxLength, minLength, leftJustify));
                                }
                            });
                        }
                        break;
                    case 'F':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                                int caller = 3;
                                for (int i = 3; i < stackTrace.length; i++) {
                                    if (LOGGER_CLASS_NAME.equals(stackTrace[i].getClassName())) {
                                        caller = i + 1;
                                    }
                                }
                                if (caller < stackTrace.length) {
                                    message.append(justify(stackTrace[caller].getFileName(), maxLength, minLength, leftJustify));
                                }
                            }
                        });
                        break;
                    case 'L':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                                int caller = 3;
                                for (int i = 3; i < stackTrace.length; i++) {
                                    if (LOGGER_CLASS_NAME.equals(stackTrace[i].getClassName())) {
                                        caller = i + 1;
                                    }
                                }
                                if (caller < stackTrace.length) {
                                    message.append(justify(Integer.toString(stackTrace[caller].getLineNumber()), maxLength, minLength, leftJustify));
                                }
                            }
                        });
                        break;
                    case 'M':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                                int caller = 3;
                                for (int i = 3; i < stackTrace.length; i++) {
                                    if (LOGGER_CLASS_NAME.equals(stackTrace[i].getClassName())) {
                                        caller = i + 1;
                                    }
                                }
                                if (caller < stackTrace.length) {
                                    message.append(justify(stackTrace[caller].getMethodName(), maxLength, minLength, leftJustify));
                                }
                            }
                        });
                        break;
                    case 'l':
                        patternList.add(new FormatElement() {

                            private final int maxLength = maxLengthRef.get();

                            private final int minLength = minLengthRef.get();

                            private final boolean leftJustify = leftJustifyRef.get();

                            public final void formatElement(final StringBuilder message, final String logName, final String levelName, final Object... object) {
                                final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                                int caller = 3;
                                for (int i = 3; i < stackTrace.length; i++) {
                                    if (LOGGER_CLASS_NAME.equals(stackTrace[i].getClassName())) {
                                        caller = i + 1;
                                    }
                                }
                                if (caller < stackTrace.length) {
                                    message.append(justify(stackTrace[caller].toString(), maxLength, minLength, leftJustify));
                                }
                            }
                        });
                }
                literal = true;
            }
            i++;
        }
        pattern = patternList.toArray(new FormatElement[patternList.size()]);
    }
