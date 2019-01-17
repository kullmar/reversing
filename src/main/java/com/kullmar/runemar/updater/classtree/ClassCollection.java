package com.kullmar.runemar.updater.classtree;

import com.kullmar.runemar.tbd.JarUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

import java.io.IOException;
import java.util.*;

import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;

public class ClassCollection {
    private Map<String, ClassHierarchy> classFiles = new HashMap<String, ClassHierarchy>();

    public ClassCollection(String jarPath) {
        try {
            initHierarchy(jarPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initHierarchy(String jarPath) throws IOException {
        Map<String, byte[]> classMap = JarUtils.getClasses(jarPath);
        for (Map.Entry<String, byte[]> entry : classMap.entrySet()) {
            byte[] clazz = entry.getValue();
            ClassHierarchy classHierarchy = new ClassHierarchy(clazz);
            classFiles.put(classHierarchy.getName(), classHierarchy);
        }
        buildHierarchy();
    }

    private void buildHierarchy() {
        for (ClassHierarchy analyzedClass : classFiles.values()) {
            ClassNode classNode = analyzedClass.getClassNode();
            if (!classNode.interfaces.isEmpty()) {
                List<ClassHierarchy> interfaces = new ArrayList<ClassHierarchy>();
                List<String> nonJarInterfaces = new ArrayList<>();
                for (String itf : classNode.interfaces) {
                    ClassHierarchy itfClass = classFiles.get(itf);
                    if (itfClass != null) {
                        interfaces.add(itfClass);
                        List<ClassHierarchy> interfaceChildren = itfClass.getChildren();
                        interfaceChildren.add(analyzedClass);
                    }
                    else {
                        nonJarInterfaces.add(itf);
                    }
                }
                analyzedClass.setInterfaces(interfaces);
                analyzedClass.setNonJarInterfaces(nonJarInterfaces);
            }
            if (!classNode.superName.equals(Object.class.getName())) {
                ClassHierarchy parent = classFiles.get(classNode.superName);
                if (parent != null) {
                    analyzedClass.setParent(parent);
                    List<ClassHierarchy> parentChildren = parent.getChildren();
                    parentChildren.add(analyzedClass);
                }
                else {
                    analyzedClass.setNonJarParent(classNode.superName);
                }
            }
        }
    }

    public Map<String, byte[]> generateClasses() {
        Map<String, byte[]> classes = new HashMap<String, byte[]>();
        for (ClassHierarchy classHierarchy : getAllClassHierarchies()) {
            ClassNode classNode = classHierarchy.getClassNode();
            ClassWriter cw = new ClassWriter(COMPUTE_MAXS);
            classNode.accept(cw);
            ClassReader cr = new ClassReader(cw.toByteArray());
            CheckClassAdapter checkerAdapter = new CheckClassAdapter(new ClassWriter(0), true);
            cr.accept(checkerAdapter, 0);
            classes.put(classNode.name + ".class", cw.toByteArray());
        }
        return classes;
    }

    public ClassHierarchy getClassHierarchy(String className) {
        return classFiles.get(className);
    }

    public Collection<ClassHierarchy> getAllClassHierarchies() {
        return classFiles.values();
    }

    public List<ClassNode> getAllClassNodes() {
        List<ClassNode> classNodes = new ArrayList<>();
        for (ClassHierarchy classHierarchy : getAllClassHierarchies()) {
            classNodes.add(classHierarchy.getClassNode());
        }
        return classNodes;
    }

    public ClassHierarchy getClassHierarchyFromInternal(String internalMethodName) {
        String className = getClassNameFromInternal(internalMethodName);
        return classFiles.get(className);
    }

    public String getClassNameFromInternal(String internalMethodName) {
        int splitIndex = internalMethodName.lastIndexOf('/');
        return internalMethodName.substring(0, splitIndex);
    }

    public ClassHierarchy getClassHierarchyFromRealName(String realName) {
        for (ClassHierarchy classHierarchy : getAllClassHierarchies()) {
            if (classHierarchy.getRealName().equals(realName)) {
                return classHierarchy;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ClassHierarchy classHierarchy : classFiles.values()) {
            sb.append(classHierarchy);
            sb.append("\n");
        }
        sb.append("Total size: " + classFiles.size());
        return sb.toString();
    }
}
