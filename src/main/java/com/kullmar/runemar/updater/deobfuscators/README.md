# Unused methods removal

## Detecting used methods and removing the remainder
Used methods are detected by iterating every instruction of every method of every class. When a method instruction is
met, it is added to the set of used methods. After this, we iterate every method and decide whether we should keep it
 or not.

## Interface methods
Interface methods must be implemented by classes, so they are included in the implementing class' bytecode. However, 
when the class is cast to the interface before calling the method, the interface method is added to the set of used 
methods. Therefore, it is necessary to check if the analyzed method belongs to an interface and see if that method 
exists in the set of used methods, if the analyzed method does not exist in the set.

## Inherited methods
Inherited methods are tricky, because they are not copied to the child class' bytecode. 

```java
public class Foo {
    public void doSomething() {
    }
}
```

```java
public class Bar extends Foo {
    
}
```

`Bar.doSomething()` is a valid command, but the function does not exist in the bytecode of `Bar`, and instead the JVM
 calls the super method. What happens then is that when we iterate method instructions and add `Bar/doSomething()V` 
 to the used methods set, we need to be careful not to remove the super method. To do this, we are checking 
 recursively all the children for the inherited method call.
 
 When a child class is cast to a parent class, the child method will still be called if overriden. To expand our 
 example, let's declare a new class `FooBar`:
 
 ```java
 public class FooBar extends Foo {
    @Override
    public void doSomething() {
        ...
    }
 }
 ```

Say we then have the following piece of code somewhere in the application
 
```java
Foo fooBar = new FooBar();
fooBar.doSomething();
```
 
 In this case, `Foo/doSomething()V` will be added to the set of used methods, when in fact `FooBar/doSomething()V` 
 should be added. As a result, we need to check if the method belongs to a parent class and check if that method is 
 called, before we can remove the method from the child class.