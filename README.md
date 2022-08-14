# SIMPLE PLUGIN

Greetings! This dependency simplifies the work when creating minecraft plugins. 
It has a number of functions in different parts of the functionality. List of things where it can be used:
- Config
- Text
- Items
- NMS

### IMPORT

```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```
<dependency>
	<groupId>com.github.Shimado</groupId>
	<artifactId>SimplePlugin</artifactId>
	<version>Tag</version>
</dependency>
```

### START
Most methods are static. For NMS to work, you need to set the version

```
SimplePlugin plugin = new SimplePlugin("v1_12_R1")
```


