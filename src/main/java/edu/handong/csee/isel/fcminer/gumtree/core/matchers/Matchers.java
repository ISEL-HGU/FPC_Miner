package edu.handong.csee.isel.fcminer.gumtree.core.matchers;

import edu.handong.csee.isel.fcminer.gumtree.core.gen.Registry;
import edu.handong.csee.isel.fcminer.gumtree.core.tree.ITree;

public class Matchers extends Registry<String, Matcher, Register> {

    private static Matchers registry;
    private Factory<? extends Matcher> defaultMatcherFactory; // FIXME shouln't be removed and use priority instead ?

    public static Matchers getInstance() {
        if (registry == null)
            registry = new Matchers();
        return registry;
    }

    private Matchers() {
        install(CompositeMatchers.ClassicGumtree.class);
        install(CompositeMatchers.ChangeDistiller.class);
        install(CompositeMatchers.XyMatcher.class);
        install(CompositeMatchers.GumtreeTopDown.class);
    }

    private void install(Class<? extends Matcher> clazz) {
        Register a = clazz.getAnnotation(Register.class);
        if (a == null)
            throw new RuntimeException("Expecting @Register annotation on " + clazz.getName());
        if (defaultMatcherFactory == null && a.defaultMatcher())
            defaultMatcherFactory = defaultFactory(clazz, ITree.class, ITree.class, MappingStore.class);
        install(clazz, a);
    }

    public Matcher getMatcher(String id, ITree src, ITree dst) {
        return get(id, src, dst, new MappingStore());
    }

    public Matcher getMatcher(ITree src, ITree dst) {
        return defaultMatcherFactory.instantiate(new Object[]{src, dst, new MappingStore()});
    }

    protected String getName(Register annotation, Class<? extends Matcher> clazz) {
        return annotation.id();
    }

    @Override
    protected Entry newEntry(Class<? extends Matcher> clazz, Register annotation) {
        return new Entry(annotation.id(), clazz,
                defaultFactory(clazz, ITree.class, ITree.class, MappingStore.class), annotation.priority()) {

            @Override
            protected boolean handle(String key) {
                return annotation.id().equals(key); // Fixme remove
            }
        };
    }
}
