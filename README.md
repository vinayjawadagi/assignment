# Message Alert Simulation

A multi-threaded Java application that simulates a message processing system with a producer, multiple senders, and a progress monitor.

## Overview

The simulation creates:
- A producer that generates random messages
- Multiple sender threads that process messages
- A monitor that reports statistics periodically
- Thread-safe message queue and statistics tracking

## Running the Simulation

The main class is `MessageAlertSim`. You can run this program with the command:<br>
<br>**make run messageCount senderCount failureRate meanDelay monitorInterval**<br>
<br>Example:  make run 1000 10 0.1 100 2

You can also use the following sequence of commands:<br>
1. javac -d out src/msg/*.java
2. jar cvfe MessageAlertSystem.jar msg.MessageAlertSim -C out .
3. java -jar MessageAlertSystem.jar **messageCount senderCount failureRate meanDelay monitorInterval**