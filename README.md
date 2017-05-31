[![Build Status](https://travis-ci.org/vermut/intellij-ansible.svg?branch=master)](https://travis-ci.org/vermut/intellij-ansible)

# YAML/Ansible in IntelliJ IDEA

**Current status: stable, basic functions, supports only basic YAML, not in active development**


<img src="src/test/data/image/Goto_Class.png" /><br>
<img src="src/test/data/image/Goto_role.png" /><br>
<img src="src/test/data/image/Var_resolve.PNG" /><br>
<img src="src/test/data/image/fetch_docs.png" /><br>

## Features
* Goto role (Ctrl-N)
* Goto task (Ctrl-N)
* Goto var/property (Ctrl-Alt-Shift-N)
* Role/Jinja var reference on Ctrl-Click
* Module doc lookup
* lexer, parser (internal)
* syntax highlighting 
* comment/uncomment
* bracket / parenthesis matching
* role completion


## Install
1. Go to File → Settings in IDEA/*Storm
2. Select *Plugins* on the left
3. Click *Browse Repositories* button on the bottom
4. Find *YAML/Ansible* and install it

## Configuration to enable the plugin

If the plugin doesn't look like it is working, it's probably because it reads the .yml, .yaml as `YAML`files, not as `YAML/Ansible`. To enable it do as follows :

1. Go to XXStorm → Preferences
2. Click on Editor → File Types → YAML/Ansible
3. Add the patterns `*.yaml` and `*.yml`. It will tell you that they are already used, that's right, by `YAML`, so select `Overwrite` (or something like that)

![alt tag](src/test/data/image/configuration.png)


**Note:** the plugin features may not work in IntelliJ 2016 if the `YAML support` plugin is also active. This will be fixed in future versions. See [the related github issue](https://github.com/vermut/intellij-ansible/issues/67).
