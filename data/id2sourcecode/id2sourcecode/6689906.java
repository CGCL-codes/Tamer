    public boolean loadOntology(String OBOFile, String ontology_name) {
        if (this.ontologies.get(ontology_name) != null) {
            if (this.verbose) System.out.println("Error: an ontology by the name of '" + ontology_name + "' has already been loaded.");
            return false;
        }
        BufferedReader in;
        try {
            if (isURL(OBOFile)) {
                URL url = new URL(OBOFile);
                URLConnection uc = url.openConnection();
                in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            } else {
                in = new BufferedReader(new FileReader(OBOFile));
            }
        } catch (Exception e) {
            if (this.verbose) System.out.println("Error: the file at " + OBOFile + " could not be opened for reading.");
            return false;
        }
        Ontology ontology = new Ontology();
        ontology.name = ontology_name;
        ArrayList subsetdef = new ArrayList();
        OBO_Object obj = null;
        String currentObject = "";
        ArrayList alt_id = new ArrayList();
        ArrayList subset = new ArrayList();
        ArrayList synonym = new ArrayList();
        ArrayList related_synonym = new ArrayList();
        ArrayList exact_synonym = new ArrayList();
        ArrayList broad_synonym = new ArrayList();
        ArrayList narrow_synonym = new ArrayList();
        ArrayList xref_analog = new ArrayList();
        ArrayList xref_unknown = new ArrayList();
        ArrayList is_a = new ArrayList();
        ArrayList relationship = new ArrayList();
        ArrayList use_term = new ArrayList();
        ArrayList xref = new ArrayList();
        int lineNum = 0;
        String line = "";
        try {
            while (true) {
                try {
                    line = removeComment(in.readLine());
                } catch (Exception e) {
                    break;
                }
                lineNum++;
                if (line.length() == 0 || line.charAt(0) == '!') {
                    continue;
                } else if (line.matches("^\\[.*?\\]$")) {
                    if (line.equals("[Term]")) {
                        currentObject = "Term";
                    } else if (line.equals("[Typedef]")) {
                        currentObject = "Typedef";
                    } else {
                        throw new Exception("Error parsing OBO file: Unknown stanza header");
                    }
                    if (obj != null) {
                        obj = castToArrays(obj, alt_id, subset, synonym, related_synonym, exact_synonym, broad_synonym, narrow_synonym, xref_analog, xref_unknown, is_a, relationship, use_term, xref);
                        obj.information_content = -1;
                    }
                    obj = new OBO_Object();
                    obj.ontology_name = ontology_name;
                    alt_id = new ArrayList();
                    subset = new ArrayList();
                    synonym = new ArrayList();
                    related_synonym = new ArrayList();
                    exact_synonym = new ArrayList();
                    broad_synonym = new ArrayList();
                    narrow_synonym = new ArrayList();
                    xref_analog = new ArrayList();
                    xref_unknown = new ArrayList();
                    is_a = new ArrayList();
                    relationship = new ArrayList();
                    use_term = new ArrayList();
                    xref = new ArrayList();
                    continue;
                }
                while (line.charAt(line.length() - 1) == '\\') {
                    line = line.substring(0, line.length() - 1);
                    String nextLine = removeComment(in.readLine());
                    lineNum++;
                    if (nextLine.matches("[^\\\\]:")) {
                        throw new Exception("Error parsing OBO file: Unexpected end of line");
                    }
                    line += nextLine;
                }
                String tag, value;
                try {
                    tag = line.substring(0, line.indexOf(':')).trim();
                    value = line.substring(line.indexOf(':') + 1).trim();
                } catch (IndexOutOfBoundsException e) {
                    throw new Exception("Error parsing OBO file: cannot find key-terminating colon");
                }
                if (value == null || value.equals("")) {
                    throw new Exception("Error parsing OBO file: no value following tag");
                }
                if (tag.equals("format-version")) {
                    ontology.format_version = removeEscapedChars(value);
                } else if (tag.equals("typeref")) {
                    ontology.typeref = removeEscapedChars(value);
                } else if (tag.equals("version")) {
                    ontology.version = removeEscapedChars(value);
                } else if (tag.equals("date")) {
                    ontology.date = removeEscapedChars(value);
                } else if (tag.equals("saved-by")) {
                    ontology.saved_by = removeEscapedChars(value);
                } else if (tag.equals("auto-generated-by")) {
                    ontology.auto_generated_by = removeEscapedChars(value);
                } else if (tag.equals("default-namespace")) {
                    ontology.default_namespace = removeEscapedChars(value);
                } else if (tag.equals("remark")) {
                    ontology.remark = removeEscapedChars(value);
                } else if (tag.equals("subsetdef")) {
                    inspectQuotedString(value);
                    subsetdef.add(parse_subsetdef(value));
                } else if (tag.equals("id")) {
                    if (obj.id != null) {
                        if (currentObject.equals("Term")) {
                            obj = ontology.getTermByID(obj.integer_id.intValue());
                        } else {
                            obj = ontology.getTypedefByID(obj.id);
                        }
                        continue;
                    }
                    obj.id = removeEscapedChars(value);
                    if (currentObject.equals("Term")) {
                        obj.integer_id = parse_id(value);
                        ontology.terms.put(obj.integer_id, obj);
                    } else {
                        ontology.typedefs.put(obj.id, obj);
                    }
                } else if (tag.equals("alt_id")) {
                    Object alternate_id;
                    if (currentObject.equals("Term")) {
                        alternate_id = parse_id(value);
                        ontology.terms.put(alternate_id, obj);
                    } else {
                        alternate_id = removeEscapedChars(value);
                        ontology.typedefs.put(alternate_id, obj);
                    }
                    alt_id.add(alternate_id);
                } else if (tag.equals("name")) {
                    obj.name = removeEscapedChars(value);
                    if (currentObject.equals("Term")) {
                        ontology.TermNameToTerm.put(obj.name.toLowerCase(), obj);
                    }
                } else if (tag.equals("namespace")) {
                    obj.namespace = removeEscapedChars(value);
                } else if (tag.equals("def")) {
                    inspectQuotedString(value);
                    if (obj.def != null) {
                        throw new Exception("Error parsing OBO file: Multiple def tags detected");
                    }
                    obj.def = parse_def(value);
                } else if (tag.equals("comment")) {
                    if (obj.comment != null) {
                        throw new Exception("Error parsing OBO file: Multiple comment tags detected");
                    }
                    obj.comment = removeEscapedChars(value);
                } else if (tag.equals("subset")) {
                    subset.add(removeEscapedChars(value));
                } else if (tag.equals("synonym")) {
                    synonym.add(parse_synonym(value));
                } else if (tag.equals("related_synonym")) {
                    related_synonym.add(parse_synonym(value));
                } else if (tag.equals("exact_synonym")) {
                    exact_synonym.add(parse_synonym(value));
                } else if (tag.equals("broad_synonym")) {
                    broad_synonym.add(parse_synonym(value));
                } else if (tag.equals("narrow_synonym")) {
                    narrow_synonym.add(parse_synonym(value));
                } else if (tag.equals("xref")) {
                    xref.addAll(parse_dbxref(value));
                } else if (tag.equals("xref_analog")) {
                    xref_analog.addAll(parse_dbxref(value));
                } else if (tag.equals("xref_unknown")) {
                    xref_unknown.addAll(parse_dbxref(value));
                } else if (tag.equals("is_a")) {
                    is_a.add(parse_id(value));
                } else if (tag.equals("relationship")) {
                    relationship.add(parse_relationship(value));
                } else if (tag.equals("is_obsolete")) {
                    obj.is_obsolete = parse_boolean(value);
                } else if (tag.equals("use_term")) {
                    use_term.add(parse_id(value));
                } else if (tag.equals("domain")) {
                    obj.domain = parse_id(value);
                } else if (tag.equals("range")) {
                    obj.range = parse_id(value);
                } else if (tag.equals("is_transitive")) {
                    obj.is_transitive = parse_boolean(value);
                } else if (tag.equals("is_cyclic")) {
                    obj.is_cyclic = parse_boolean(value);
                } else if (tag.equals("is_symmetric")) {
                    obj.is_symmetric = parse_boolean(value);
                } else {
                    if (this.verbose) System.out.println("Line " + Integer.toString(lineNum) + ": Warning parsing OBO file: Unrecognized tag\n  --> contents of line: " + line);
                }
                if (currentObject.equals("Term") && obj.namespace == null && ontology.default_namespace != null) {
                    obj.namespace = ontology.default_namespace;
                }
            }
            obj = castToArrays(obj, alt_id, subset, synonym, related_synonym, exact_synonym, broad_synonym, narrow_synonym, xref_analog, xref_unknown, is_a, relationship, use_term, xref);
            Subsetdef[] subset_subsetdef = new Subsetdef[subsetdef.size()];
            ontology.subsetdef = (Subsetdef[]) subsetdef.toArray(subset_subsetdef);
            in.close();
        } catch (IOException e) {
            if (this.verbose) System.out.println("Error reading file");
            try {
                in.close();
            } catch (Exception e1) {
            }
            return false;
        } catch (Exception e) {
            if (this.verbose) System.out.println("Line " + Integer.toString(lineNum) + ": " + e.getMessage() + "\n  --> contents of line: " + line);
            try {
                in.close();
            } catch (Exception e1) {
            }
            return false;
        }
        HashSet allTerms = new HashSet();
        Iterator it = ontology.terms.values().iterator();
        while (it.hasNext()) {
            obj = (OBO_Object) it.next();
            allTerms.add(obj);
        }
        it = allTerms.iterator();
        while (it.hasNext()) {
            OBO_Object obj_orig = (OBO_Object) it.next();
            Integer[] parents = obj_orig.is_a;
            Integer child_id = obj_orig.integer_id;
            for (int i = 0; i < parents.length; i++) {
                try {
                    obj = (OBO_Object) ontology.terms.get(parents[i]);
                    if (obj.children == null) {
                        obj.children = new Integer[1];
                        obj.children[0] = child_id;
                    } else {
                        Integer[] updated = new Integer[obj.children.length + 1];
                        for (int j = 0; j < obj.children.length; j++) {
                            updated[j] = obj.children[j];
                        }
                        updated[updated.length - 1] = child_id;
                        obj.children = updated;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            Relationship[] relationships = obj_orig.relationship;
            if (relationships == null) {
                continue;
            }
            ArrayList part_of_parents = new ArrayList();
            for (int i = 0; i < relationships.length; i++) {
                if (relationships[i].relationship.equals("part_of")) {
                    part_of_parents.add(relationships[i].integer_id);
                }
            }
            for (int i = 0; i < part_of_parents.size(); i++) {
                try {
                    obj = (OBO_Object) ontology.terms.get(part_of_parents.get(i));
                    if (obj.children == null) {
                        obj.children = new Integer[1];
                        obj.children[0] = child_id;
                    } else {
                        Integer[] updated = new Integer[obj.children.length + 1];
                        for (int j = 0; j < obj.children.length; j++) {
                            updated[j] = obj.children[j];
                        }
                        updated[updated.length - 1] = child_id;
                        obj.children = updated;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        this.ontologies.put(ontology_name, ontology);
        return true;
    }
