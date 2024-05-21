# Simple Bitcoin wallet



This repository contains source code for the client-side wallet program as a part of our project in which we attempt to implement a simplified version of the Bitcoin protocol.
The project serves mostly as an educational tool to better understand blockchain technology and how Bitcoin protocol works in detail.

In order to use the wallet you'll need to have at least one node present. The Bitcoin node server implementation can be found at 
[SimpleBitcoinNode](https://github.com/TheGreyCore/SimpleBitcoinNode) repository.

## Getting started

The simplest way to build the wallet application is by using the provided Maven wrapper. Just make sure that your computer has a JDK which at least supports
Java 17. 

Linux/MacOS:  
```sh
$ ./mvnw package
```

Windows:  
```cmd
> .\mvnw.cmd package
```

After building the application you can run it with a wrapper script provided in the repository. I.e.
on Linux/MacOS:  
```shell
$ ./simple-bitcoin-wallet create wallet.btc
```
or on Windows:  
```cmd
> .\simple-bitcoin-wallet.cmd create wallet.btc
```

After successfully creating a new wallet file, you can use either the interactive or commandline parameter mode for interacting with it.
For example let's say that we want to use an interactive mode: 
```shell
$ ./simple-bitcoin-wallet interactive wallet.btc
```
where wallet.btc is the wallet file.

For more information about the usage see:
```shell
$ ./simple-bitcoin-wallet -h
```

## Project roadmap

Please read following [documentation](docs/ROADMAP.md) for information about the project's current status and future roadmap.