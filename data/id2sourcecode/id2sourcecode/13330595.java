    void removeGroupedInstance(String groupName, MudObject instance) {
        MudObject[] instances = (MudObject[]) groupedInstances.get(groupName);
        if (instances != null) {
            if (instances.length == 1) {
                if (instances[0].equals(instance)) {
                    groupedInstances.remove(groupName);
                }
            } else {
                int removalIndex = -1;
                for (int i = 0; i < instances.length; i++) {
                    if (instances[i].equals(instance)) {
                        removalIndex = i;
                        break;
                    }
                }
                if (removalIndex >= 0) {
                    MudObject[] trimmedInstances = new MudObject[instances.length - 1];
                    for (int i = 0; i < removalIndex; i++) {
                        trimmedInstances[i] = instances[i];
                    }
                    for (int i = removalIndex; i < trimmedInstances.length; i++) {
                        trimmedInstances[i] = instances[i + 1];
                    }
                    groupedInstances.put(groupName, trimmedInstances);
                }
            }
        }
    }
