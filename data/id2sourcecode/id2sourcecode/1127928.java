        if (!found) return found;
        DateTime srchtm = (DateTime) sectionTime.clone();
        srchtm.add(Calendar.SECOND, SectionLength);
