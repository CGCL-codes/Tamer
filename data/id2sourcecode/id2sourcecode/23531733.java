        private Accessor(Method readMethod, Method writeMethod) {
            this.readMethod = new WeakReference<Method>(readMethod);
            this.writeMethod = new WeakReference<Method>(writeMethod);
        }
