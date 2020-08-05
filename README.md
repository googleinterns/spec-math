# Spec Math
## Overview
A platform for performing operations on OpenAPI specifications (specs). OpenAPI is the industry-standard
format for machine-readable REST API descriptions.

## Background

Specifications play a critical role in the API lifecycle. They can enforce a contract between client
and server teams, easily generate documentation and client libraries, reduce the work of
creating a proxy for an existing service and much more. The
Spec Math project aims to afford even more power to API developers who use the OpenAPI
specification. The Spec Math Library will enable them to do the following:

- Merge two or more OpenAPI specifications into one.
- Filter an OpenAPI specification based on specified criteria.
- Overlay an OpenAPI specification.
- And more, discussed in the sections and examples below.

Let's suppose there is a complex project with several teams, each responsible for developing different
portions of an API. These teams might include marketing, inventory, analytics, and others. If all
10 teams were to work off of the same specification, it could become an extremely long and
unwieldy document. Then, if the company wants to expose a subset of functionality to specific
user groups and developer teams, they would perform a tedious, manual, and error-prone
cherry-pick of features to add to a new spec. These are both examples of where Spec Math can
come to the rescue.

### Union
One feature of Spec Math is to merge two or more specs into one. The developers could
then have each team working on their own APIs with their own respective specs. From there, the
developers can leverage the Spec Math Library to create a unified spec to describe the APIs
developed across all the teams. This could be very useful for generating documentation, but
also as a prerequisite for another key feature of Spec Math: filtering.

### Filter
The developers are now able to unify the specs across multiple teams using the union feature
of the Spec Math Library. Now, though, providers want to give specific user groups and
developer teams access to portions of the unified API. For example, the developers now want to
create a public API with all of the API endpoints across all teams that do not require
authentication. It would be a tedious and error-prone process to manually cherry-pick pieces of
the unified API and paste them into a new public API spec.
With filtering, the company can provide the unified spec, along with a list of filter criteria that
they want to match. For example, they can tag public endpoints with a “public tag” and then
apply filter. The Spec Math Library will then return an OpenAPI spec with just the relevant pieces
of the unified API. The company would also be able to use filtering to develop customized API
documentation for their web app team, for users who will only use or have access to a subset of
the monolithic API functionality. This can be done all while allowing the federated teams to
focus on their own portion of the API if Spec Math is used.

### Overlay
In the case of both filtering and merging, the resultant spec represents a different API product
than the input specs. This is why overlaying functionality will also be supported. An overlay can
be applied using a “default file” which contains metadata about the final API product that is
produced from Spec Math operations. For example, if a company wishes to build a web
application using filtered pieces of their unified API, the original spec will no longer have an
accurate “info: title” key path. The overlay will contain something like "info: title: Company Web
Application” which will be applied to the resultant specification.

### Spec Math Library
#### Features

## Directory structure
- The `library` folder contains the "Spec Math Library", an implementation of Spec Math in Java. 
- The `web` folder showcases an
example usage of the Spec Math Library through an API which is connected the frontend application in the
`app` folder.

## About this document
This document contains an overview of the Spec Math platform. For more specific documentation 
and installation instructions, please look at the respective folder in the directory structure
described above.

## Copyright

Copyright 2020, Google LLC.