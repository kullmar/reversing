package com.kullmar.runemar.updater.finders.classfinders;

import com.kullmar.runemar.updater.classtree.ClassCollection;

import java.util.ArrayList;
import java.util.List;

public class MasterClassFinder {
    private List<ClassFinder> analyzers = new ArrayList<>();

    public MasterClassFinder() {
        analyzers.add(new ClientFinder());
        analyzers.add(new NameFinder());
        analyzers.add(new NodeFinder());
        analyzers.add(new CacheableNodeFinder());
        analyzers.add(new RenderableFinder());
        analyzers.add(new ActorFinder());
        analyzers.add(new PlayerFinder());
        // analyzers.add(new CanvasFinder());
        analyzers.add(new GameEngineFinder());
    }

    public List<ClassFinder> getAnalyzers() {
        return analyzers;
    }

    public void run(ClassCollection classCollection) {
        for (ClassFinder analyzer : analyzers) {
            analyzer.run(classCollection);
        }
    }
}
