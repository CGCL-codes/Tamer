        public void rendezvousFunction(Object[] objects) {
            int lastIdx = objects.length - 1;
            Object first = objects[0];
            for (int i = 0; i < lastIdx; ++i) objects[i] = objects[i + 1];
            objects[lastIdx] = first;
        }
