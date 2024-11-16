# Entity to DTO Converter

<!-- Plugin description -->


When right-clicking an Entity class and selecting 'Generate DTO', a DTO class is automatically generated.  
Currently, the plugin recognizes DTO classes if their names include 'Dto' and identifies Entity classes by the @Entity annotation.  

1. After selecting 'Generate DTO' from the context menu on an Entity class, the corresponding DTO class is created.  

![1.png](https://raw.githubusercontent.com/jsw6701/devPlugin/main/src/main/resources/img/1.png)  

![2.png](https://raw.githubusercontent.com/jsw6701/devPlugin/main/src/main/resources/img/2.png)  

2. After selecting 'Generate Conversion Methods' from the context menu on a DTO class, methods to convert the Entity to the DTO and vice versa are generated.  

![3.png](https://raw.githubusercontent.com/jsw6701/devPlugin/main/src/main/resources/img/3.png)  

![4.png](https://raw.githubusercontent.com/jsw6701/devPlugin/main/src/main/resources/img/4.png)  


<!-- Plugin description end -->

## Installation

- Using the IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Entity to DTO Converter"</kbd> >
  <kbd>Install</kbd>
  
- Using JetBrains Marketplace:

  Go to [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID) and install it by clicking the <kbd>Install to ...</kbd> button in case your IDE is running.

  You can also download the [latest release](https://plugins.jetbrains.com/plugin/MARKETPLACE_ID/versions) from JetBrains Marketplace and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

- Manually:

  Download the [latest release](https://github.com/jsw6701/devPlugin/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
[docs:plugin-description]: https://plugins.jetbrains.com/docs/intellij/plugin-user-experience.html#plugin-description-and-presentation
