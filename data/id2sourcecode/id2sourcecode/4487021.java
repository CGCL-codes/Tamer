    public int findUserProjectId(String email, Project project) {
        int boincId = -1;
        DataBaseManager dbm = DataBaseManager.getInstance();
        SQLiteDatabase db = dbm.openDataBase(false);
        Set<String> cpidSet = new HashSet<String>();
        Cursor query = db.query(true, getTableName(UserTable.class), new String[] { UserTable.CPID.getColumn() }, UserTable.Email.getColumn() + "='" + email + "'", null, null, null, null, null);
        while (query.moveToNext()) {
            cpidSet.add(query.getString(0));
        }
        dbm.release(query);
        if (cpidSet.size() == 0) {
            throw new BOINCException(String.format("No users matching '%s' where found in the database.", email));
        }
        InputStream stream = null;
        for (String cpid : cpidSet) {
            try {
                URL url = new URL("http://boinc.netsoft-online.com/get_user.php?cpid=" + cpid);
                URLConnection urlConnection = url.openConnection();
                stream = urlConnection.getInputStream();
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                Document document = documentBuilder.parse(stream);
                Element root = document.getDocumentElement();
                List<Element> projectElements = DOMUtils.getChildren(root, "project");
                String projectName;
                for (Element e : projectElements) {
                    projectName = DOMUtils.getTextOrNull(e, "name");
                    if (projectName != null && project.toString().equals(projectName)) {
                        String tmp = DOMUtils.getTextOrNull(e, "id");
                        if (tmp != null) {
                            boincId = Integer.parseInt(tmp);
                            break;
                        }
                    }
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (SAXException e) {
                Log.e(TAG, e.getMessage(), e);
            } catch (ParserConfigurationException e) {
                Log.e(TAG, e.getMessage(), e);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return boincId;
    }
