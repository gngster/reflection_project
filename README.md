This simple coding exercise is for me to learn more about Java Reflections as well as implement
clean coding principles with some guidance from my mentor at Zuhlke - Thomas Wilhelm. 

Draft #1 - Implement parse method in Reflection class with tests in ReflectionTest class.
-- 
Some comments given based on draft 1 - 

In `Reflection` class: 
1. Refactor parse method into clearer sub-methods instead of putting all the details in the parse method
   1. createInstanceOfClass(T clazz)
   1. parseText(text) (use a map instead of a list so, it's clearer)
   1. setValuesToFields(fieldsAndValues)


2. In the if-else block, I should have used `else-if` instead of just `if` also. 
take note of open closed principle -- open for extension, closed for modification
- don't touch existing code, because you will break code,
- factory implementation , no business logic in factory so, it's not so tragic to have a switch case to choose which implementation to use, shouldn't have biz logic in factory, just switch statements
- when you see a bigger if else block, start to think about using a design pattern (factory/strategy, etc)
- in this use case, it might be over engineering to make use of the factory pattern - instead, just extract the if-else block and make use of a switch case
- unit test that method

3. Note: don't repeat the type in the naming -- `List<String> keyValueList`, 
just put as `keyValues`, although if you name it that way, people would expect a map, so you should use a map, or either change the name...
4. `setField(field, newClass, keyValueList.get(i + 1));`  change the order of parameters passed in, from object to field to actual value (clearer to understand) or if you name it as `setValueToField()` you could possibly put value first! 



In `ReflectionTest` class:

1. not needed to use display name if the display name is describing the same thing as method name. If using display name, add more info, like given-when-then.
1. don't need to list all the exceptions in test, just throw general Exception.
1. 3 steps in a test - 
   1. initialise (data, mocks, etc) (in this case, it's simple enough, don't need the init step)
   1. execute the method (there should only be 1 execution actually)
   1. assert values are correct
1. good practice - put empty lines between the 3 steps to make it clearer for others to read, like in the `Reflection.parse` method.
1. learn to use assertj - better error message, convenient methods to check list and array such as containsInOrder


General Comments:
1. put code neatly in packages
1. separate source code implementation and tests (this is so that when you package the code into a jar file, you can separate out the source and test. customer is not interested in the test code. it also makes the packaged jar file smaller without the test code.)
1. look at the warnings in IDE and try to resolve them. if not, have a clear reason why you choose not to resolve them.
1. always think of why you make certain choices while coding. that is how you can put your points across to others. 

1. exception handling:
   1. be precise with exceptions in methods
   1. we should not log AND throw exception -> anti-pattern
   1. logging error is a way of handling the exception.
   1. throwing exception is a way of passing the problem to the method calling it.
   1. if you log error at every catch block, you may end up having multiple error logs when the exception gets propagated up to methods calling it... customer would not be happy seeing 3 error logs when actually there is only 1 error.
   1. in the catch block, either A. Handle the exception (by logging/ retrying) OR B. throw the exception (pass to others to handle)
1. about Reflection: 
   1. Spring uses reflection to find annotations, expensive to grab the annotations and initialise the beans etc.
   1. new framework called micronaut (do it at compile time instead of runtime like spring)
   1. open api makes use of reflection to analyse the code in controller annotated class and generates the yaml file based on analysed methods.
    

Testing Best Practices -
from https://phauer.com/2019/modern-best-practices-testing-java/.

In this case `parseOneStringValueToPersonClass`, we may only be interested in a certain JSON field of the payload. So we should only check the relevant field to clearly state and document the scope of the logic under test. Again, there is no need to assert all fields again, because there are not relevant here.

##### Test Behaviour, not implementation (use given-when-then to guide)
don't write tests that are dependent on implementation details for e.g. if you are using verify() to verify that an inner method is called only once/ not called at all, you are 
coupling your implementation details too tightly to your test. 
there are exceptions to writing verify() - sometimes it is needed. not always 1 or 0.
if service method returns smth, you don't have to use verify. you can just assert the output.
valid reason to use verify - publishing of events.

tests should be 
1. fast (< 100ms)
2. readable
3. actually testing something (is the test just testing your mocks???!)

if you have too many lines of initialisation in tests (too complicated), something may be wrong with your design...
certain code smells should be your trigger point to think is your solution the best. 
a new requirement should be the trigger to write a new test, not a new method. 
in this case, i should definitely test the method `parseText` as many things could go wrong with a string there. this "requirement" does not have to be a high 
level user requirement - it could be a lower level. don't test the implementation inside. just test input and output. 

await can be used in integration tests...

integration tests (if dependent on actual databases) can be run separately in the CI/CD setup so you can get a build up and running and not let your build be dependent on success of external systems.

#### Controller coding best prctices
controller should only call one service that calls other services instead of controller having all the other services in it -- 
testing will be very cluttered in controller - you just want to test that the endpoint is accessible, but now you have to end up 
mocking all the services. so you should just call one service in controller and in that service, call all the other services.

Draft #2 - Refactored into Factory pattern.
-- 
