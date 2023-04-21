    @RequestMapping("/ajax/weightSD/{measureDate}/{childId}/{weightKG}")
    @ResponseBody
    public String calculateWeightSD(@PathVariable String measureDate, @PathVariable String childId, @PathVariable String weightKG) {
        try {
            Child child = Child.findChild(Long.parseLong(childId));
            if (child != null) {
                Date dobDate = child.getDateOfBirth();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date measDate = dateFormat.parse(measureDate);
                Float age = ClinicalRecord.calculateAge(measDate, dobDate);
                Float exactAgeMonths = ClinicalRecord.calculateExactAgeMonths(age);
                Float weightKGValue = Float.parseFloat(weightKG);
                Float weightSD = ClinicalRecord.calculateWeightSD(exactAgeMonths, child.getSex(), weightKGValue);
                return (weightSD != null ? DecimalUtil.format(weightSD) : "");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }
