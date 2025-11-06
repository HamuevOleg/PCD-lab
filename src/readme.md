Laboratory Work 4: Thread Synchronization
# 🎯 Objective
* The main goal of this laboratory work is to learn and apply thread synchronization mechanisms in Java. You will do this by implementing the classic Producer-Consumer problem to manage concurrent access to a shared resource.

# 📋 Your Task: The Producer-Consumer Problem
You must implement a Java application that simulates a "warehouse" (a shared buffer) being used by multiple Producer and Consumer threads.


1. Select Your Variant
   First, you must choose one variant from "Table 1" in the lab document. This will determine your specific parameters.


2. Define Parameters
   Based on your chosen variant, you will have the following values:


X: The total number of Producer threads.

Y: The total number of Consumer threads.


Z: The number of objects each individual consumer must consume to be "satisfied".


D: The maximum size (capacity) of the warehouse (shared buffer).


Object Type: The type of data your producers must generate (e.g., Even numbers, Odd numbers, Vowels, etc.).

F: The number of items each producer generates at one time. This is fixed at 2 items for all variants.


3. Simulation Logic
   Producers (X threads):

Continuously generate F=2 objects of your variant's specified type (e.g., two odd numbers).


Try to add these objects to the shared warehouse.


If the warehouse is full (i.e., size == D), the producer thread must wait.

You must log a message when a producer adds items.

Consumers (Y threads):

Continuously try to consume one object from the shared warehouse.


If the warehouse is empty (i.e., size == 0), the consumer thread must wait.

You must log a message when a consumer takes an item.

Each consumer thread must keep track of how many items it has consumed.

Warehouse (Shared Resource):

This will be a class (like Buffer or Store in the examples) that holds the shared data (e.g., in an ArrayList or similar collection).


Its capacity is limited to D items.

It must provide synchronized methods for put() (adding items) and get() (removing items).



It must log messages when it becomes "full" or "empty".

End Condition:

The entire simulation runs until every single one of the Y consumers has successfully consumed Z objects.

# 🛠️ Technical Requirements
✅ What You MUST Use

Java Threads: You must create and start X producer threads and Y consumer threads. (e.g., by extending Thread or implementing Runnable).


A Shared Resource Class: You must create a class for the warehouse/buffer that is shared among all threads.


synchronized Keyword: You MUST use the synchronized modifier on the put and get methods of your shared resource class. This ensures that only one thread can access the buffer at a time.




wait() Method: You must use wait() to make a thread pause and release the lock.


Call wait() in the get() method when the buffer is empty .

Call wait() in the put() method when the buffer is full .


notifyAll() Method: You must use notifyAll() to wake up all waiting threads after the state of the buffer has changed.

Call notifyAll() in the get() method after successfully removing an item (to wake up producers).


Call notifyAll() in the put() method after successfully adding items (to wake up consumers).



while Loops for Conditions: You MUST check your wait conditions (e.g., while (buffer.isEmpty())) inside a while loop, not an if statement.


# ❌ What to AVOID
No Synchronization: Do not attempt this without synchronized, wait(), and notifyAll(). Your program will fail, as shown in the example output .



Busy-Waiting (Active Waiting): Do NOT use empty loops like while (buffer.isFull()) { /* do nothing */ } . This is called "egoistic" waiting, wastes CPU, and is incorrect programming. You must use wait().




Using notify(): While the document mentions it , all correct examples use notifyAll(). In a multi-producer/multi-consumer problem, using notify() is dangerous and can lead to deadlock. Use notifyAll().



# 🏆 Grading Criteria (Summary)

For a passing grade (5-6): You must correctly implement the core logic, including creating threads and using synchronized, wait(), and notifyAll() correctly .




For a good grade (7-8): You must do all of the above, AND you must create a Graphical User Interface (GUI) for your program.



For an excellent grade (9-10): You must meet all requirements, including the GUI, and also demonstrate code optimization and correct handling of all task requirements (like the X, Y, Z, D parameters).