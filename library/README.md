# Spec Math Library

A library for performing operations on OpenAPI specifications. 

## About this document
This document contains information about using the Spec Math Library. 
For a description of Spec Math itself, please see the README at the
[root of the repository](https://github.com/googleinterns/spec-math#spec-math-and-the-spec-math-library).

## Running tests

Run `mvn test` to execute all of the tests.

## Usage

This library supports performing [Spec Math Operations](https://github.com/googleinterns/spec-math#operations)
on OpenAPI Specifications represented as YAML strings. The `SpecMath` class contains static functions
which can be called on OpenAPI specifications. The most common use case of this library will be to call the functions provided in the `SpecMath` class,
although more advanced usage is possible by using the underlying classes.
For some examples of inputs and outputs to this class, please see [The tests for the SpecMath class](src/test/java/org/specmath/library/SpecMathTest.java)
or the guide below.

### Performing Spec Math Operations

For descriptions of these operations, please see the [root of the repository](https://github.com/googleinterns/spec-math#spec-math-and-the-spec-math-library).

#### Union

##### Of Two or More Specs

The `union` function can be called on a list of specs or spec fragments. For example:

```java
String spec1 = Files.readString(Path.of("src/test/resources/noConflict1.yaml"));
String spec2 = Files.readString(Path.of("src/test/resources/noConflict2.yaml"));
String spec3 = Files.readString(Path.of("src/test/resources/noConflict3.yaml"));

var listOfSpecs = new ArrayList<String>();
listOfSpecs.add(spec1);
listOfSpecs.add(spec2);
listOfSpecs.add(spec3);

return SpecMath.union(listOfSpecTrees);
```

There is also a convenient function provided for performing `union` on just two specs:

```java
String spec1 = Files.readString(Path.of("src/test/resources/noConflict1.yaml"));
String spec2 = Files.readString(Path.of("src/test/resources/noConflict2.yaml"));

SpecMath.union(spec1, spec2)
```


##### With UnionOptions

`UnionOptions` is a way to provide additional options to perform during the union. It is
built using the desired options and then provided to the `union` function as a parameter.

Once you have built your `UnionOptions` (see the following sections for more details), 
simply supply it as a second argument to `union` like so:

```java
UnionOptions unionOptions = //... see following sections for more details
return SpecMath.union(listOfSpecs, unionOptions);
```

You can use all or some of the options:

**defaults**

Please feel free to read more about defaults files [here](https://github.com/googleinterns/spec-math#overlay).

To build the `UnionOptions` with your defaults file:

```java
String defaults = Files.readString(Path.of("src/test/resources/conflictDefaults.yaml"));

UnionOptions unionOptions =
    UnionOptions.builder()
        .defaults(defaults)
        .build();
```

**conflictResolutions**

If conflicts occurred during the process of the union, there are several ways to resolve them as further
described [here](https://github.com/googleinterns/spec-math#operations).
One method is to provide a `conflictResolutions` string [(sample)](src/test/resources/conflictMerged.yaml) through `UnionOptions`.
If a conflict arises, a `UnionConflictException` (`e`) will be thrown. At that point, you may inspect
the conflicts with `e.getConflicts()`. A `conflictResolutions` file template may be created by simply serializing
the result of `e.getConflicts()`:

```java
import com.fasterxml.jackson.databind.ObjectMapper;

ObjectMapper mapper = new ObjectMapper();

mapper
  .writerWithDefaultPrettyPrinter()
  .writeValue(new File(conflictsFilename), e.getConflicts());
```

You will then need to modify the `resolvedValue` properties for each conflict in the file you just created
to indicate your conflict resolutions. 

Note: you can also modify the `resolvedValue`s by setting them programmatically in the conflict objects
themselves.

Finally, to build the `UnionOptions` with your conflict resolutions string:

```java
String conflictResolutions = Files.readString(Path.of(conflictsFilename));

UnionOptions unionOptions =
    UnionOptions.builder()
        .conflictResolutions(conflictResolutions)
        .build();
```

**Using multiple options**

You may add multiple options by chaining them like so:

```java
String defaults = Files.readString(Path.of("src/test/resources/conflictDefaults.yaml"));
String conflictResolutions = Files.readString(Path.of("src/test/resources/conflictMerged.yaml"));

UnionOptions unionOptions =
    UnionOptions.builder()
        .defaults(defaults)
        .conflictResolutions(conflictResolutions)
        .build();
```

#### Overlay

To apply an overlay on top of a spec, just call the `applyOverlay` function like so:

```java
String spec1String = Files.readString(Path.of("src/test/resources/petstoreMarketing.yaml"));
String overlay = Files.readString(Path.of("src/test/resources/petstoreMetadata.yaml"));
return SpecMath.applyOverlay(overlay, spec1String);
```

Note that if you would like to overlay a set of metadata to a result spec after a union or a filter,
you may also consider using a defaults file through either `UnionOptions` or `FilterOptions` instead, discussed
above and below.

#### Filter

The `filter` operation requires a spec and a filter (either as a file or a list of `FilterCriteria` objects), with optional `FilterOptions`.

A filter file contains a list of objects which have some or all of the properties in the `FilterCriteria` class.
The properties to filter on which are currently supported are also described [here](https://github.com/googleinterns/spec-math#filter-1).
You may provide all or some of the properties for each list item of the filter file. Each list item
will independently produce a subset of the input spec, and the union of all of these subsets will be returned.
For some small examples of filter files, please see the JSON files in [this directory](src/test/resources/filtering).

```java
String specString =
    Files.readString(Path.of("src/test/resources/filtering/filteringMonolithicSpec.yaml"));
String filterCriteria =
    Files.readString(Path.of("src/test/resources/filtering/specificPathFilterCriteria.json"));
return SpecMath.filter(specString, filterCriteria);
```

Note: instead of having to create a file to perform the `filter`, you may also provide a list of `FilterCriteria` objects
instead.

##### With FilterOptions

`FilterOptions` is a way to provide additional options to perform during or after the filter. It is
built using the desired options and then provided to the `filter` function as a parameter.

Once you have built your `FilterOptions` (see the following sections for more details), 
simply supply it as a third argument to `filter` like so:

```java
FilterOptions filterOptions = //... see following sections for more details
return SpecMath.filter(specString, filterFile, filterOptions);
```

You can use the following option:

**defaults**

Please feel free to read more about defaults files [here](https://github.com/googleinterns/spec-math#overlay).

To build the `FilterOptions` with your defaults file:

```java
String defaults = Files.readString(Path.of("src/test/resources/petstoreMetadata.yaml"));
FilterOptions filterOptions = FilterOptions.builder().defaults(defaults).build();
```








