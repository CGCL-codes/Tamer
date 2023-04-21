        private void handleRequest() throws IOException {
            if (!userAuthorised()) {
                writeHeaders("text/html", "", 401, 0);
                return;
            }
            String resource = httpRequest.getResource();
            logger.verbose("Processing resource : '" + resource + "'");
            loadedPools = PoolLoader.getLoadedPools();
            if ((resource == null) || (resource.length() == 0) || resource.equals("/") || resource.equals("/index.html")) {
                writeHeaders("text/html", "", 200, 0);
                writeTop();
                writeData("<br/><h4>Welcome to the Primrose Web Console</h4>");
                for (Pool pool : loadedPools) {
                    writeData("<p>");
                    writeData("<a href=\"/showPool?poolName=" + pool.getPoolName() + "\">Show '" + pool.getPoolName() + "' Pool</a>");
                    writeData("</p>");
                }
                writeBottom();
            } else if (resource.startsWith("/showPoolConfig")) {
                writeHeaders("text/html", "", 200, 0);
                writeTop();
                String poolName = httpRequest.getParameters().get("poolName");
                Pool pool = findPoolByName(poolName);
                if (pool != null) {
                    writeData("<p>");
                    writeData("<a href=\"/showPool?poolName=" + pool.getPoolName() + "\">Show '" + pool.getPoolName() + "' Connections</a>");
                    writeData("</p>");
                    writeData("<br/><form method=\"get\" action=\"alterPoolProperties\"><table>");
                    Method[] publicMethods = PoolConfigImpl.class.getMethods();
                    for (int j = 0; j < publicMethods.length; j++) {
                        String methodName = publicMethods[j].getName();
                        if (methodName.startsWith("getClass")) {
                            continue;
                        }
                        if (methodName.startsWith("get")) {
                            try {
                                Object ret = publicMethods[j].invoke(pool, new Object[] {});
                                if (ret == null) {
                                    ret = "";
                                }
                                writeData("<tr><td>" + methodName.replaceAll("get", "") + "</td><td><input type=\"text\" name=\"" + methodName.replaceAll("get", "") + "\" size=\"60\" value=\"" + ret + "\"/></td></tr>");
                            } catch (Exception e) {
                            }
                        }
                    }
                    writeData("<tr><td colspan=\"2\"><input type=\"hidden\" value=\"" + poolName + "\"/></td></tr>");
                    writeData("<tr><td colspan=\"2\"><input type=\"submit\" value=\"Update Pool Now\"/></td></tr>");
                    writeData("</table></form>");
                    writeData("<p>If you update the pool settings, the existing pool will be restarted in order for the settings to take effect. Any running connections/SQL will NOT be affected - their jobs will finish normally, and then those connections will be retired.</p>");
                } else {
                    writeData("<p>Sorry, cannot find pool under name '" + poolName + "'</p>");
                }
                writeBottom();
            } else if (resource.equals("/webconsole.css")) {
                writeCSS();
            } else if (resource.startsWith("/poolStop")) {
                String poolName = httpRequest.getParameters().get("poolName");
                Pool pool = findPoolByName(poolName);
                try {
                    pool.stop(false);
                } catch (PoolException pe) {
                    logger.printStackTrace(pe);
                }
                writeHeaders("text/html", "/showPool?poolName=" + pool.getPoolName(), 302, 0);
            } else if (resource.startsWith("/poolStart")) {
                String poolName = httpRequest.getParameters().get("poolName");
                Pool pool = findPoolByName(poolName);
                try {
                    pool.start();
                } catch (PoolException pe) {
                    logger.printStackTrace(pe);
                }
                writeHeaders("text/html", "/showPool?poolName=" + pool.getPoolName(), 302, 0);
            } else if (resource.startsWith("/poolRestart")) {
                String poolName = httpRequest.getParameters().get("poolName");
                Pool pool = findPoolByName(poolName);
                try {
                    pool.restart(false);
                } catch (PoolException pe) {
                    logger.printStackTrace(pe);
                }
                writeHeaders("text/html", "/showPool?poolName=" + pool.getPoolName(), 302, 0);
            } else if (resource.startsWith("/showPool")) {
                String poolName = httpRequest.getParameters().get("poolName");
                String refreshRate = httpRequest.getParameters().get("refresh");
                Pool pool = findPoolByName(poolName);
                writeHeaders("text/html", "", 200, 0);
                writeTop(refreshRate);
                writeData("<table><tr><td><table><tr><td>Server date : " + new java.util.Date() + "</td></tr>");
                writeData("<tr><td>Primrose Version : " + Constants.VERSION + "</td></tr>");
                if (pool != null) {
                    int numberOfWaitingThreads = pool.getNumberOfWaitingThreads();
                    if (numberOfWaitingThreads > 0) {
                        writeData("<tr><td><span style='color:red'>Warning ! There are " + numberOfWaitingThreads + " threads waiting for a connection.</span> You may want to increase your base pool size.</td></tr>");
                    }
                    writeData("<tr><td>Total number of connections handed out : " + pool.getTotalConnectionsHandedOut() + "</td></tr>");
                    writeData("<tr><td><a href=\"/showPoolConfig?poolName=" + poolName + "\">Edit runtime config properties</a></td></tr></table></td><td>&nbsp;</td>");
                    writeData("<td><table><tr><td><a href=\"/poolStop?poolName=" + poolName + "\">Stop Pool</a></td></tr>");
                    writeData("<tr><td><a href=\"/poolStart?poolName=" + poolName + "\">Start Pool</a></td></tr>");
                    writeData("<tr><td><a href=\"/poolRestart?poolName=" + poolName + "\">Restart Pool</a></td></tr></table></td></tr></table>");
                    writeData("<p/><h5>Connection Data</h5>");
                    Vector<ConnectionHolder> connections = pool.getPoolConnections();
                    writeData("<table border=\"1\">");
                    writeData("<tr><td><h5>ID</h5></td>" + "<td><h5>Status</h5></td>" + "<td><h5>Opens</h5></td>" + "<td><h5>Closes</h5></td>" + "<td><h5>CallableStatement</h5></td>" + "<td><h5>PreparedStatement</h5></td>" + "<td><h5>Statement</h5></td>" + "<td><h5>Active For</h5></td>" + "<td><h5>Idle For</h5></td>" + "<td><h5>SQL</h5></td></tr>");
                    for (ConnectionHolder ch : connections) {
                        String status = PoolData.getStringStatus(Pool.UNKNOWN_STATUS_CODE);
                        String inusefor = "n/a";
                        String idlefor = "n/a";
                        if (ch.status == Pool.CONNECTION_ACTIVE) {
                            status = "<span style='color:red'>" + PoolData.getStringStatus(ch.status) + "</span>";
                            inusefor = ((System.currentTimeMillis() - ch.connOpenedDate) / 1000) + " secs";
                        } else if (ch.status == Pool.CONNECTION_INACTIVE) {
                            status = "<span style='color:green'>" + PoolData.getStringStatus(ch.status) + "</span>";
                            if (ch.lastUsedTimestamp > 0L) {
                                idlefor = ((System.currentTimeMillis() - ch.lastUsedTimestamp) / 1000) + " secs";
                            }
                        }
                        writeData("<tr><td>" + ch.conn.hashCode() + "</td><td>" + status + "</td>" + "<td>" + ch.numberOfOpens + "</td>" + "<td>" + ch.numberOfCloses + "</td>" + "<td>" + ch.numberOfJDBCCallableStatementsRun + "</td>" + "<td>" + ch.numberOfJDBCPreparedStatementsRun + "</td>" + "<td>" + ch.numberOfJDBCStatementsRun + "</td>" + "<td>" + inusefor + "</td>" + "<td>" + idlefor + "</td>" + "<td>" + ch.sql + "</td></tr>");
                    }
                    writeData("</table>");
                    writeData("<br/><p>" + "ID - The PoolConnection class hashCode()<br/>" + "Status - The Status of the connection<br/>" + "Opens - How many times this connection has been extracted from the pool<br/>" + "Closes - How many times this connection has been returned to the pool<br/>" + "CallableStatement - How many JDBC CallableStatement objects have been created from this connection<br/>" + "PreparedStatement - How many JDBC PreparedStatement objects have been created from this connection<br/>" + "Statement - How many JDBC Statement objects have been created from this connection<br/>" + "Active For - How many seconds this connection has been in use for(if active)<br/>" + "Idle For - How many seconds this connection has been idle for (if inactive)<br/>" + "SQL - The current executing SQL on this connection (if any)</p>");
                } else {
                    writeData("<p>Sorry, cannot find pool under name '" + poolName + "'</p>");
                }
                writeData("<p><form method=\"get\"><input type=\"text\" name=\"refresh\"/><input type=\"hidden\" name=\"poolName\" value=\"" + poolName + "\"/>&nbsp;<input type=\"submit\" value=\"Set Page Refresh Rate (seconds)\"/></form>");
                writeBottom();
            } else if (resource.startsWith("/alterPoolProperties")) {
                Pool pool = findPoolByName(httpRequest.parameters.get("PoolName"));
                Iterator<String> parametersIter = httpRequest.parameters.keySet().iterator();
                while (parametersIter.hasNext()) {
                    String key = parametersIter.next();
                    String value = httpRequest.parameters.get(key);
                    if (!key.equals("PoolName")) {
                        try {
                            logger.info("[WebConsole] alterPoolProperties() Setting " + key + "=" + value);
                            Util.callClassMethod(Pool.class, pool, "set" + key, new Object[] { value });
                        } catch (GeneralException ge) {
                            showErrorPage("Error setting property " + key + " : " + ge);
                            return;
                        }
                    }
                }
                try {
                    pool.restart(false);
                } catch (PoolException pe) {
                    showErrorPage("Error restarting pool : " + pe);
                }
                writeHeaders("text/html", "/showPool?poolName=" + pool.getPoolName(), 302, 0);
            }
        }
