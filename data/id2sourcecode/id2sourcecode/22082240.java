            public void textChanged(DocumentEvent e) {
                try {
                    String pass = new String(passwordTextField.getPassword());
                    String encoded = new String(Hex.encodeHex(digester.digest(pass.getBytes("UTF-8"))));
                    user.setPassword(encoded);
                } catch (UnsupportedEncodingException e1) {
                    throw new RuntimeException(e1);
                }
            }
