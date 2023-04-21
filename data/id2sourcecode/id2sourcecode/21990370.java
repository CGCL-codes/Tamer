    protected LocationMarkerSource getDefaultLocationMarkerSource() {
        LocationMarkerSource default_source = null;
        try {
            if (resources_.getBoolean(KEY_LOCATION_MARKER_USE_DB)) {
                String database_type = resources_.getString(KEY_LOCATION_MARKER_DB_TYPE);
                String key_prefix = KEY_LOCATION_MARKER_DB_PREFIX + database_type + ".";
                String jdbc_driver = resources_.getString(key_prefix + KEY_LOCATION_MARKER_DB_JDBCDRIVER_POSTFIX);
                String jdbc_url = resources_.getString(key_prefix + KEY_LOCATION_MARKER_DB_URL_POSTFIX);
                String jdbc_username = resources_.getString(KEY_LOCATION_MARKER_DB_USER);
                String jdbc_password = resources_.getString(KEY_LOCATION_MARKER_DB_PASSWORD, "");
                JDBCUtil jdbc_util = new JDBCUtil(jdbc_driver, jdbc_url, jdbc_username, jdbc_password);
                try {
                    jdbc_util.setDebug(false);
                    boolean could_open = false;
                    try {
                        jdbc_util.open();
                        could_open = true;
                    } catch (SQLException e) {
                        logger_.warn("WARNING: could not open the database: " + e.getMessage());
                    }
                    if (!could_open || !jdbc_util.tableExists("MARKERS")) {
                        int result = JOptionPane.showConfirmDialog(null, resources_.getString(KEY_LOCALIZE_MESSAGE_CREATE_DATABASE_MESSAGE), resources_.getString(KEY_LOCALIZE_MESSAGE_INFO_TITLE), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                        if (result == JOptionPane.YES_OPTION) {
                            java.net.URL url = resources_.getURL(key_prefix + KEY_LOCATION_MARKER_DB_CREATE_DB_SCRIPT_URL_POSTFIX);
                            boolean ask_admin_username_password = resources_.getBoolean(key_prefix + KEY_LOCATION_MARKER_DB_CREATE_DB_ASK_USERNAME_PASSWORD_POSTFIX);
                            logger_.info("Database does not exist, creating it...");
                            logger_.info("using the script at URL " + url);
                            Reader reader = new InputStreamReader(url.openStream());
                            String jdbc_create_db_url = resources_.getString(key_prefix + KEY_LOCATION_MARKER_DB_CREATE_DB_JDBC_URL_POSTFIX);
                            String jdbc_admin_username;
                            String jdbc_admin_password;
                            if (!ask_admin_username_password) {
                                jdbc_admin_username = resources_.getString(key_prefix + KEY_LOCATION_MARKER_DB_CREATE_DB_USER_POSTFIX);
                                jdbc_admin_password = resources_.getString(key_prefix + KEY_LOCATION_MARKER_DB_CREATE_DB_PASSWORD_POSTFIX);
                                logger_.info("logging into '" + jdbc_create_db_url + "' as user '" + jdbc_admin_username + "'");
                                jdbc_util = new JDBCUtil(jdbc_driver, jdbc_create_db_url, jdbc_admin_username, jdbc_admin_password);
                                jdbc_util.open();
                                jdbc_util.executeSQL(reader);
                            } else {
                                String[] messages = new String[] { "Database Administrator Username/Password for Database Url:", jdbc_create_db_url };
                                could_open = false;
                                while (!could_open) {
                                    LoginDialog login_dialog = new LoginDialog(dialog_owner_frame_, "Username/Password", messages);
                                    result = login_dialog.getValue();
                                    if (result == LoginDialog.OK_OPTION) {
                                        jdbc_admin_username = login_dialog.getUsername();
                                        jdbc_admin_password = login_dialog.getPassword();
                                        jdbc_util = new JDBCUtil(jdbc_driver, jdbc_create_db_url, jdbc_admin_username, jdbc_admin_password);
                                        try {
                                            jdbc_util.open();
                                            jdbc_util.executeSQL(reader);
                                            could_open = true;
                                        } catch (SQLException e) {
                                            JOptionPane.showMessageDialog(dialog_owner_frame_, "SQL Error occured: " + e.getMessage(), resources_.getString(KEY_LOCALIZE_MESSAGE_ERROR_TITLE), JOptionPane.ERROR_MESSAGE);
                                            e.printStackTrace();
                                        }
                                    } else {
                                        could_open = true;
                                    }
                                }
                            }
                        }
                    } else {
                        if (resources_.getBoolean(KEY_LOCATION_MARKER_DB_OPTIMIZE_DB_ON_START)) {
                            logger_.info("try to optimize database...");
                            JDialog dialog = new JDialog(dialog_owner_frame_, resources_.getString(KEY_LOCALIZE_MESSAGE_INFO_TITLE), false);
                            dialog.getContentPane().add(new JLabel(resources_.getString(KEY_LOCALIZE_MESSAGE_OPTIMIZE_DATABASE_MESSAGE)));
                            dialog.pack();
                            dialog.setVisible(true);
                            Dimension screen_dim = Toolkit.getDefaultToolkit().getScreenSize();
                            int x = (int) ((screen_dim.getWidth() - dialog.getWidth()) / 2);
                            int y = (int) ((screen_dim.getHeight() - dialog.getHeight()) / 2);
                            dialog.setLocation(x, y);
                            jdbc_util.optimizeDatabase();
                            dialog.setVisible(false);
                            dialog.dispose();
                            dialog = null;
                            resources_.setBoolean(KEY_LOCATION_MARKER_DB_OPTIMIZE_DB_ON_START, false);
                        }
                    }
                    jdbc_util.close();
                } catch (Exception e) {
                    logger_.error("ERROR: orruced during testing/creating the location marker database:");
                    e.printStackTrace();
                }
                default_source = new JDBCLocationMarkerSource(jdbc_driver, jdbc_url, jdbc_username, jdbc_password, resources_);
                ((JDBCLocationMarkerSource) default_source).open();
            } else {
                String dirname = FileUtil.getAbsolutePath(resources_.getString(KEY_FILE_MAINDIR), resources_.getString(KEY_FILE_LOCATION_DIR));
                File dir = new File(dirname);
                if (!dir.isDirectory()) {
                    logger_.error("Directory '" + dirname + "' does not exist, creating it.");
                    dir.mkdirs();
                }
                String marker_file = dirname + File.separator + resources_.getString(KEY_FILE_LOCATION_FILENAME);
                default_source = new FileLocationMarkerSource(resources_, marker_file);
                ((FileLocationMarkerSource) default_source).setNameColumn(0);
                ((FileLocationMarkerSource) default_source).setLatitudeColumn(1);
                ((FileLocationMarkerSource) default_source).setLongitudeColumn(2);
                ((FileLocationMarkerSource) default_source).setCategoryColumn(3);
                ((FileLocationMarkerSource) default_source).setDelimiter(',');
                ((FileLocationMarkerSource) default_source).initialize();
            }
        } catch (LocationMarkerSourceException lmse) {
            if (lmse.getCause() instanceof FileNotFoundException) {
                logger_.warn("WARNING: Location Marker file could not be found: " + lmse.getCause().getMessage() + " - create a location marker to create the file.");
            } else lmse.printStackTrace();
        }
        return (default_source);
    }
