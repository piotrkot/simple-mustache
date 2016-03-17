# Simple-mustache
This is simple object-oriented Java implementation of 
[mustache templates](https://mustache.github.io/).
Although there is already available 
[one from spullara](https://github.com/spullara/mustache.java)
I personally find it difficult to extend and fix, with unclear API,
and few bugs when different tag delimiter used.

For that I propose a simpler and object-oriented alternative.

## Tag types supported

### Variables
The basic tag `{{name}}`
Example:
...

### Sections
Tag composed of two tags `{{#name}} {{/name}}` for which content within the tags
is rendered one or more times.
Example:
...

### Inverted sections
Tag composed of two tags `{{^name}} {{/name}}` which is the inversion of
the section tag. That is content is rendered only once on the inverse value 
of the tag name (tag key).
Example:
...

### Partials
It's of a form `{{>name}}` where value for the tag name is file content. 
File content can be a String, Path or InputStream. The content can contain
other Tag types except partials. Recursive partials are not supported to avoid
infinite loops.
Example:
...

## Delimiters
Arbitrarily any delimiter can be used but use it with care. Otherwise you will
end up with unexpected outcome.
Example:
...

## Limitations
There is no validation phase for content generation. Illegal template or tags
will be unrecognized without a warning.

Tag names must be composed of any word characters (`A-Za-z0-9_`) 
including dot (`.`).


Feel free to fork me on GitHub, report bugs or post comments.

For Pull Requests, please run `mvn clean package -Pqulice`, first.
