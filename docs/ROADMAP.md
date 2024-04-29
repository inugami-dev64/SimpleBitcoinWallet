# Roadmap and current project status

## Stage 1 results

By the end of stage one not much development had been done on the wallet program and as such there wasn't really a usable program available.
Features that were implemented in the first stage were mostly related to asymmetric cryptography.

## Stage 2 results

By the end of stage two the wallet program has a nice terminal user interface made with `jline3` and `picocli`. Although the user interface doesn't provide
much functionality yet, a solid groundwork has been made to do this in the third stage. As an example we started using guice - 
a lightweight inversion of control library for Java. In addition, we have also built services and abstraction classes for tasks such as wallet file encryption and
network node communication.

## Stage 3 goals

The goal of the stage three is to add functionality to already existing commands and finalizing the communication between the node server
and wallet.