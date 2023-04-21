            public InputSource resolveEntity(String public_id, String system_id) {
                if (dtd_symbol != null && system_id.endsWith(dtd_symbol)) {
                    try {
                        Reader reader = new InputStreamReader(dtd_url.openStream());
                        return new InputSource(reader);
                    } catch (Exception e) {
                        XRepository.getLogger().error(this, "An error occured while trying to resolve the main DTD!");
                        XRepository.getLogger().error(this, e);
                        return null;
                    }
                } else return null;
            }
