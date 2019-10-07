# JSONmutator
A Java library for mutating JSON objects.

## How to add a new mutation operator
For the sake of simplicity, we will explain how to add a new mutation operator with an example. This example corresponds
to the addition of a _String_ mutation operator, but it should be replicable for any other data type (integer, boolean, etc.):

1. Create a new constant with the name of the mutation in the class
[OperatorNames](https://github.com/isa-group/JSONmutator/blob/master/src/main/java/es/us/isa/jsonmutator/util/OperatorNames.java),
like this:
```java
public class OperatorNames {
    public static final String MUTATE = "mutate";
    public static final String CHANGE_TYPE = "changeType";
    public static final String REPLACE = "replace"; // NEWLY ADDED LINE
}
```

2. Add a weight to that mutation operator in the 
[config.properties](https://github.com/isa-group/JSONmutator/blob/master/src/main/resources/config.properties) file. Note that
you must use the same name for the property that you used in the constant previously defined (in this case, "replace"):
```
operator.value.string.weight.replace=0.1
```

3. Create a new class for the mutation operator under the package ```es.us.isa.jsonmutator.value.string0.operator```. Note
that this class must _necessarily_ implement a constructor (where at least ```super()``` is called and the ```weight```
attribute is set) and the _mutate_ method:
```java
public class StringReplacementOperator extends AbstractOperator {

    private int minLength;      // Minimum length of the randomly generated string
    private int maxLength;      // Maximum length of the randomly generated string

    public StringReplacementOperator() {
        super();
        weight = Float.parseFloat(readProperty("operator.value.string.weight." + OperatorNames.REPLACE));
        minLength = Integer.parseInt(readProperty("operator.value.string.length.min"));
        maxLength = Integer.parseInt(readProperty("operator.value.string.length.max"));
    }

    public Object mutate(Object stringObject) {
        return RandomStringUtils.random(rand1.nextInt(minLength, maxLength), true, true);
    }
}
```

4. Add the mutation operator to the map of operators in the constructor of the Mutator class (in this case,
[StringMutator](https://github.com/isa-group/JSONmutator/blob/master/src/main/java/es/us/isa/jsonmutator/mutator/value/string0/StringMutator.java)):
```java
public class StringMutator extends AbstractMutator {

    public StringMutator() {
        super();
        prob = Float.parseFloat(readProperty("operator.value.string.prob"));
        operators.put(OperatorNames.MUTATE, new StringMutationOperator());
        operators.put(OperatorNames.CHANGE_TYPE, new ChangeTypeOperator(String.class));
        operators.put(OperatorNames.REPLACE, new StringReplacementOperator()); // NEWLY ADDED LINE
    }
}
```
