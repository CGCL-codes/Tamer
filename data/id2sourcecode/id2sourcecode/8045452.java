    public BinaryExpression(String expression, String secondaryName, boolean secondaryRandom) throws IOException {
        Stack transforms = new Stack();
        int pos;
        while ((pos = expression.indexOf(':')) != -1) {
            String transform = expression.substring(0, pos);
            transforms.push(transform);
            expression = expression.substring(pos + 1);
            if (transform.length() == 0) {
                transforms.pop();
                break;
            } else if (transform.equals("split")) {
                break;
            }
        }
        byte[] value = expression.getBytes("UTF-8");
        byte[] secondaryValue = null;
        boolean[] secondaryBits = null;
        try {
            while (!transforms.isEmpty()) {
                String transform = (String) transforms.pop();
                if (transform.equals("hex")) {
                    if (value.length % 2 != 0) throw new IllegalArgumentException("invalid hex length");
                    byte[] valueNew = new byte[value.length / 2];
                    for (int j = 0; j < valueNew.length; j++) {
                        valueNew[j] = (byte) Integer.parseInt((char) value[j * 2] + "" + (char) value[j * 2 + 1], 16);
                    }
                    value = valueNew;
                } else if (transform.equals("base64")) {
                    String base64Text = new String(value, "UTF-8");
                    value = decodeBase64(base64Text);
                } else if (transform.equals("sha1")) {
                    value = MessageDigest.getInstance("SHA-1").digest(value);
                } else if (transform.equals("md5")) {
                    value = MessageDigest.getInstance("MD5").digest(value);
                } else if (transform.equals("split")) {
                    String parts = new String(value, "UTF-8");
                    StringTokenizer st = new StringTokenizer(parts.substring(1), "" + parts.charAt(0));
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while (st.hasMoreTokens()) {
                        baos.write(parseBinaryExpression(st.nextToken()));
                    }
                    value = baos.toByteArray();
                } else if (transform.equals("unescape")) {
                    value = unescape(new String(value, "UTF-8"), "").getBytes("UTF-8");
                } else if (transform.startsWith("hash-")) {
                    value = MessageDigest.getInstance(transform.substring(5)).digest(value);
                } else if (transform.startsWith("encode-")) {
                    value = new String(value, "UTF-8").getBytes(transform.substring(7));
                } else if (transform.startsWith("map")) {
                    byte[] tmp = new byte[4096];
                    int count = 0;
                    String[] exprs = transform.substring(3).split("\\+");
                    for (int j = 0; j < exprs.length; j++) {
                        if (exprs[j].length() == 0) continue;
                        NumberExpression expr = new NumberExpression(exprs[j]);
                        for (int k = expr.getMinimum(); k <= expr.getMaximum(); k++) {
                            if (expr.matches(k)) tmp[count++] = value[k - 1];
                        }
                    }
                    value = new byte[count];
                    System.arraycopy(tmp, 0, value, 0, count);
                } else if (secondaryName != null && transform.startsWith(secondaryName)) {
                    boolean bitsUsedTemp = false;
                    byte[] tmp = new byte[4096];
                    boolean[] tmpbit = new boolean[4096];
                    int count = 0;
                    String[] exprs = transform.substring(secondaryName.length()).split("\\+");
                    for (int j = 0; j < exprs.length; j++) {
                        if (secondaryRandom && exprs[j].endsWith("r")) {
                            for (int k = 0; k < Integer.parseInt(exprs[j].substring(0, exprs[j].length() - 1)); k++) {
                                tmpbit[count++] = true;
                                bitsUsedTemp = true;
                            }
                        } else {
                            boolean optional = false;
                            if (!secondaryRandom && exprs[j].endsWith("?")) {
                                optional = true;
                                bitsUsedTemp = true;
                                exprs[j] = exprs[j].substring(0, exprs[j].length() - 1);
                            }
                            NumberExpression expr = new NumberExpression(exprs[j]);
                            for (int k = expr.getMinimum(); k <= expr.getMaximum(); k++) {
                                if (expr.matches(k)) {
                                    tmp[count] = value[k - 1];
                                    tmpbit[count] = optional;
                                    count++;
                                }
                            }
                        }
                    }
                    secondaryValue = new byte[count];
                    System.arraycopy(tmp, 0, secondaryValue, 0, count);
                    if (bitsUsedTemp) {
                        secondaryBits = new boolean[count];
                        System.arraycopy(tmpbit, 0, secondaryBits, 0, count);
                    } else {
                        secondaryBits = null;
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported transform: " + transform);
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            IOException ioex = new IOException("Cannot create algorithm");
            ioex.initCause(ex);
            throw ioex;
        }
        this.value = value;
        this.secondaryValue = secondaryValue;
        this.secondaryRandom = secondaryRandom;
        this.secondaryValid = secondaryName != null;
        this.secondaryBits = secondaryBits;
    }
