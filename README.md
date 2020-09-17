<p align="center">
  <img src="./app/src/assets/img/specmath_logo.svg" width="350" title="Spec Math logo">
</p>

---

## Spec Math
## Overview
Spec Math is a platform for performing operations on OpenAPI specifications. 
[OpenAPI](https://github.com/OAI/OpenAPI-Specification) is an industry-standard
format for machine-readable REST API descriptions.

## About this document
This document contains an overview of this repository as well as a description of Spec Math.

A goal of this document is to highlight the capabilities of Spec Math and
describe its operations and functions so that you can determine if it might be useful to your project. 

While this repository already contains a [Java implementation](library) of Spec Math, we acknowledge that
your project might have different language needs and hope that this document (along with the
[Java implementation](library)) will also provide
enough information such that you could implement Spec Math in any language
of your choosing. For more specific documentation and installation instructions, please look 
at the respective folder in the directory structure
described below.

## Directory structure
- The [library](library) folder contains the "Spec Math Library", an implementation of Spec Math in Java.
- The [web](web) folder showcases an example usage of the Spec Math Library through an API which is connected
 to the frontend application in the
[app](app) folder.

## Spec Math and the Spec Math Library

### Background
Specifications play a critical role in the API lifecycle. They can enforce a contract between client
and server teams, easily generate documentation and client libraries, reduce the work of
creating a proxy for an existing service and much more. The
Spec Math project aims to afford even more power to API developers who use the OpenAPI
specification. The [Spec Math Library](library) will enable them to do the following:

- Merge two (or more in a future update) OpenAPI specifications into one.
- Filter an OpenAPI specification into a new spec based on specified criteria.
- Overlay an OpenAPI specification by superimposing partial specs with domain-specific annotations.
- And more, discussed in the sections and examples below.

While these features are currently supported in the [Spec Math Library](library), please
note that "Spec Math" as a platform could theoretically support many other operations on OpenAPI specifications.
Contributions of new features and operations are welcome!

Below is a simple example of a use case of Spec Math.

Let's suppose there is a complex project with several teams working on it. 
Each team is responsible for developing different
portions of an API. These teams might include marketing, inventory, analytics, and others. If all
teams were to work off of the same specification, it could become an extremely long and
unwieldy document. Then, if the developers want to expose a subset of functionality to specific
user groups and developer teams, they would perform a tedious, manual, and error-prone
cherry-pick of features to add to a new spec. These are some examples of where Spec Math could
come to the rescue.

#### Union
One feature of Spec Math is to merge two or more specs into one. The developers could
then have each team working on their own APIs with their own respective specs. From there, the
developers can leverage the [Spec Math Library](library) to create a unified spec to describe the APIs
developed across all the teams.

#### Filter
The developers are now able to unify the specs across multiple teams using the union feature
of the Spec Math Library. Now, though, providers want to give specific user groups and
developer teams access to portions of the unified API. For example, the developers now want to
create a public API with all of the API endpoints across all teams that do not require
internal authentication. It would be a tedious and error-prone process to manually cherry-pick pieces of
the unified API and paste them into a new public API spec.
With filtering, the developers can provide the unified spec, along with a list of filter criteria that
they want to match, and get a new spec matching the criteria. For example, filtering would allow the developers
to generate a new spec with only the "public" tagged operations.

#### Overlay
Overlay allows developers to place OpenAPI spec fragments on top of existing specs.

For example, in the case of both filtering and merging, the resultant spec represents a different API product
than the input specs. This is why overlaying functionality will also be supported. An overlay can
be applied using a “default file” which contains metadata about the final API product that is
produced from Spec Math operations. For example, if a company wishes to build a web
application using filtered pieces of their unified API, the original spec will no longer have an
accurate `info: title` key path. The overlay will contain something like `info: title: Company Web
Application` which will be applied to the resultant specification. Another example is to have overlays
created for different stages in the lifecycle as an API moves from pre production to production.

### Overview

This section provides descriptions of Spec Math operations. See the background section for an example of how
some of these capabilities of Spec Math might be useful.   

For additional specific examples of each operation described below, please take a look at the integration
tests for the [Spec Math Library](library/src/test/java/org/specmath/library/SpecMathTest.java). Tests are
provided for some example usages of the operations discussed below. Each test contains a sample input and output.

#### Operations

##### Union

##### Union Of Two Specs
Union of two specs takes specs A and B along with an optional set of rules to create a new spec C which contains A and B.

A collision (or union conflict) occurs when two YAML key paths have different primitive values. For example, if
spec A had `info: license: name: MIT` and spec B had `info: license: name: GPL`, there would be
a conflict in the `info: license: name` key path. To resolve this, the user can provide a defaults
file as an overlay. In cases where the overlay cannot resolve the conflict, the conflicting key
paths will be reported back. The user can then resolve the conflicts by either:

- using a conflict resolution file ([sample](library/src/test/resources/conflictMerged.yaml)) passed in as a parameter to the union,
- manually resolving the specs by removing the conflict
- or updating the defaults file.

##### Union Of Multiple Specs
Union of multiple specs takes a list of specs L along with an optional set of rules to create a new spec
which contains all specs in L.

A collision (or union conflict) occurs when a YAML key path has several possible options across two
or more specs. The example from "Union of Two Specs" is the same, except that conflicts can occur
across more than just two specs. The methods to resolve conflicts are also the same.

#### Overlay
Overlay takes spec A along with a defaults file D ([sample](library/src/test/resources/metadata.yaml)) to modify spec A. D is a spec fragment
which will be placed "on top" of spec A. 
In the case of a would-be collision, the defaults file takes priority. 
An overlay operation is a specialized Union operation between A and D where D will always take priority.
Therefore, Overlay operations will not have union conflicts. 

#### Filter
Filtering takes spec A along with a filter file F to create a new spec B which contains a subset
of features from A according to F. A filter file contains a list of Filter Criteria Objects, discussed below.
Only the component references from A which are relevant to B will be included.

Below is a list of properties which can be added to a Filter Criteria Object. A filter file F is a list of
Filter Criteria Objects. For examples of F, please see the JSON files in the [filtering tests resources directory](library/src/test/resources/filtering).

- **FILTER BY TAGS**. The user can achieve fine-grained filtering functionality extremely
easily by just adding specific tags to the spec. For example, the user could mark
operations as “internal” or “external” and easily filter based on these tags.
  - Users can provide a list of tags in the `tags` property of the filter criteria.
   Only endpoints
   which contain those tags will be
kept in the resultant spec. 
  - Users can specify certain tags with the `removableTags` property
in the filter criteria, which will remove those tags in the resultant
spec. That way, published spec docs wouldn’t contain
these special tags which were only added to the spec for the purposes of
filtering. 
- **FILTER BY PATHS AND OPERATIONS**. The user can filter based on desired paths,
 as well as desired operations (e.g. get, put)
  - Paths are specified in the `path` property in the filter criteria
  - Operations are specified in the `operations` property in the filter criteria
  
In addition to a filter file, users can also specify other filtering “options” or “parameters”. Similar
to the union operation, the API product which is represented by the result of a filter operation is
quite different from the spec which was used to filter. Therefore, a useful option will be to
provide metadata about the resultant spec. 

By default, when the user provides a list of filter criteria, all matches
will be included. Future work includes the ability to provide an “exclude” option, which will exclude
paths matching the criteria in the resultant spec.

## Copyright

Copyright 2020, Google LLC.
