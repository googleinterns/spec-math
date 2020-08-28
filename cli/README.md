# Spec Math CLI

A command line interface which exposes some operations from the Spec Math Library.

As a prerequisite, please read about Spec Math and the Spec Math operations [here](https://github.com/googleinterns/spec-math#spec-math)
and [here](https://github.com/googleinterns/spec-math/tree/master/library).

## Getting Up and Running

### For Unix Based Operating Systems

You should first clone the repository:

```
git clone https://github.com/googleinterns/spec-math.git
```

As the Spec Math Library has not yet been uploaded to Maven, you will need to navigate
to the [library folder](../library) in your terminal. Then run the following commands:

```
mvn clean install
```

Then, navigate to the [cli folder](.) in your terminal and run these commands:

```
mvn clean install
mvn package appassembler:assemble
```

After doing so, the binary for the entrypoint to the Spec Math CLI will be located at
/target/appassembler/bin/specmath in the [cli folder](.). You can run it with the help command like so:

`sh /target/appassembler/bin/specmath --help`

or

`./target/appassembler/bin/specmath --help`

you can also create an alias with the absolute path to the binary for ease of use

`alias specmath="./FULL_PATH_TO_BINARY"`

and add the above line to the end of your ~/.bashrc file so that you do not need
to recreate the alias each time you open your command line. 

### Usage

Assuming you have set the alias as described above, the main command is `specmath`. To get help, run `specmath -h`. 
Each of the Spec Math operations have their own subcommands with their own options and flags: 

- `specmath union`
- `specmath filter`
- `specmath overlay`

To get help for each of these subcommands, use the `-h` or `--help` flag to learn more (e.g. `specmath union -h`).
Also, as a prerequisite, please read about Spec Math and the Spec Math operations [here](https://github.com/googleinterns/spec-math#spec-math)
and [here](https://github.com/googleinterns/spec-math/tree/master/library).

## Running tests

Run `mvn test` to execute all of the tests.
