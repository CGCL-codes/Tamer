        private void readClass() {
            if (null == services) {
                isRead = true;
                return;
            }
            Iterator<URL> iter = services.iterator();
            que = new LinkedList<String>();
            while (iter.hasNext()) {
                URL url = iter.next();
                if (null == url) {
                    throw new NullPointerException();
                }
                try {
                    reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                    String str;
                    while (true) {
                        str = reader.readLine();
                        if (null == str) {
                            break;
                        }
                        String[] strs = str.trim().split(SINGAL_SHARP);
                        if (0 != strs.length) {
                            str = strs[0].trim();
                            if (!(str.startsWith(SINGAL_SHARP) || 0 == str.length())) {
                                char[] namechars = str.toCharArray();
                                for (int i = 0; i < namechars.length; i++) {
                                    if (!(Character.isJavaIdentifierPart(namechars[i]) || namechars[i] == '.')) {
                                        throw new ServiceConfigurationError(Msg.getString("KB006", namechars[i]));
                                    }
                                }
                                if (!que.contains(str)) {
                                    que.add(str);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    throw new ServiceConfigurationError(Msg.getString("KB006", url), e);
                }
            }
            isRead = true;
        }
