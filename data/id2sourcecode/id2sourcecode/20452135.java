        @Override
        protected String[] searchForWordsInFields(String[] words, String adapter, String specificColumnID, String generalColumnID, String columnLast, String columnFirst, String table) {
            String whereClause = "";
            for (@SuppressWarnings("unused") String word : words) {
                if (!whereClause.equals("")) {
                    whereClause += " OR ";
                }
                whereClause += "(to_tsvector('" + regconfig + "', lower(" + columnLast + ")) @@ to_tsquery( lower(?) )) OR " + "(to_tsvector('" + regconfig + "', lower(" + columnFirst + ")) @@ to_tsquery( lower(?) ))";
            }
            String[] result;
            if (words.length > 0) {
                result = new String[2 * words.length + 1];
                result[0] = "SELECT " + COLUMN_DOCUMENT_ID + " FROM (SELECT " + generalColumnID + " FROM " + table + " WHERE " + whereClause + ") AS alias0 JOIN " + adapter + " ON(" + specificColumnID + "=" + generalColumnID + ")";
                for (int i = 1; i < result.length; i = i + 2) {
                    result[i] = result[i + 1] = words[(i - 1) / 2];
                }
            } else {
                result = new String[0];
            }
            return result;
        }
