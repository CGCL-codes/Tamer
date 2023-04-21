    private boolean checkFieldNames(String fieldname) throws Exception {
        String[] keyword = { "id", "taskid", "siteid", "channelid", "caption", "author", "source", "ispicture", "isfirst", "weight", "relationalwords", "keywords", "summary", "content", "formname", "created", "lastmodified", "readers", "writers", "owners", "owner", "flow", "state" };
        String[] htmltag = { " ", "<", ">", "[", "]", "{", "}", "'", "\"" };
        if (fieldname == null) return true;
        for (int i = 0; i < keyword.length; ++i) if (fieldname.toLowerCase().trim().equals(keyword[i])) return false;
        for (int i = 0; i < htmltag.length; ++i) if (fieldname.indexOf(htmltag[i]) >= 0) return false;
        return true;
    }
