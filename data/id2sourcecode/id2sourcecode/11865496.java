        TrieNode getChild(char nextChar) {
            if (children == null) {
                children = (TrieNode[]) childrenList.toArray(new TrieNode[childrenList.size()]);
                childrenList = null;
                Arrays.sort(children);
            }
            int min = 0;
            int max = children.length - 1;
            int mid = 0;
            while (min < max) {
                mid = (min + max) / 2;
                if (children[mid].nodeChar == nextChar) return children[mid];
                if (children[mid].nodeChar < nextChar) min = mid + 1; else max = mid - 1;
            }
            if (min == max) if (children[min].nodeChar == nextChar) return children[min];
            return null;
        }
