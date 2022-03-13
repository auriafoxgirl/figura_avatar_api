# figura_avatar_api

A simple figura api extention that allow you to change your avatar, or upload it with script.

to use custom api add it to your mods folder

using api in script:

all apis are stored in one table like all other figura apis, that table is named "avatar"

reload avatar:
```lua
avatar.reload()
```
returns: **nil**

check if avatar is local (not uploaded to backend):
```lua
avatar.isLocal()
```
returns: **boolean** ( true if its local and false if its from backend )

load avatar from backend:
```lua
avatar.loadFromBackend()
```
returns: **nil**

upload your avatar to backend (there is 2.5 seconds delay until you will be able to upload it again):
```lua
avatar.uploadToBackend()
```
returns: **boolean** ()

set local avatar:
```lua
avatar.set( avatar_dir )
```
requires: string that is: path to folder or zip of figura avatar (ex. "myAvatar.zip")
returns: **nil**



used template: https://github.com/Kingdom-of-Moon/Figura-Entrypoint-Example
