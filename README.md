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
```java
new Mustache("Hello {{name}}").supply(ImmutableMap.of("name", "John"));
```
returns `Hello John`.

### Sections
Tag composed of two tags `{{#name}} {{/name}}` for which content within the tags
is rendered one or more times.
Example:
```java
new Mustache("Greetings to{{#friends}} {{name}}{{/friends}}").supply(
  ImmutableMap.of(
    "friends", ImmutableList.of(
      ImmutableMap.of("name", "John"),
      ImmutableMap.of("name", "Mark"),
      ImmutableMap.of("name", "Ann")
    )
  )
);
```
returns `Greetings to John Mark Ann`.

### Inverted sections
Tag composed of two tags `{{^name}} {{/name}}` which is the inversion of
the section tag. That is content is rendered only once on the inverse value 
of the tag name (tag key).
Example:
```java
new Mustache("Greetings in return{{^greets}} - none{{/greets}}").supply(
  ImmutableMap.of("greets", Collections.emptyList())
);
```
returns `Greetings in return - none`.

### Partials
It's of a form `{{>name}}` where value for the tag name is file content. 
File content can be a String, Path or InputStream. The content can contain
other Tag types except partials. Recursive partials are not supported to avoid
infinite loops.
Example:
```java
new Mustache("Hope to get a {{>surprise}}").supply(
  ImmutableMap.of("surprise", new ByteArrayInputStream("gift!".getBytes()))
);
```
returns `Hope to get a gift!`.

## Delimiters
Arbitrarily any delimiter can be used but use it with care. Otherwise you will
end up with unexpected outcome.
Example:
```java
public final class SquareMustache extends AbstractMustache {
    public SquareMustache(final String content) {
        super(content);
    }
    @Override
    public String start() {
        return "[[";
    }
    @Override
    public String end() {
        return "]]";
    }
}
```

## Limitations
There is no validation phase for content generation. Illegal template or tags
will be unrecognized without a warning.

Tag names must be composed of any word characters (`A-Za-z0-9_`) 
including dot (`.`).

Within tags spaces are allowed as long as they do not split the tag names.

Feel free to fork me on GitHub, report bugs or post comments.

For Pull Requests, please run `mvn clean package -Pqulice`, first.
