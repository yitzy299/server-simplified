# Server Simplified

A fabric mod that adds basic utilities for server owners
Originally developed by zeroeightysix, I (yitzy299) have continued updating the mod and tweaked it for my personal use case. Any suggestions, please make an issue.

## Original source
The original code for Server Simplified can be found at https://github.com/zeroeightysix/server-simplified

## Commands
`/feed [<target>]` Restores players' health. Will target yourself if no target is specified.   
`/heal [<target>]` Restore players' hunger. Will target yourself if no target is specified.  
`/mute <target> [<time>]` Mutes players. See [this section](#mute-time) for the formatting of `time`  
`/unmute <target>` Unmutes players.  
`/seekinv <target>` Opens the specified player's inventory.  
`/staffchat [<message>]` Toggle, or send a message to, staff chat. See [staff chat](#staff-chat)  
`/vanish [<target>]` Vanishes or appears players. Will target yourself if no target is specified.  
`/permission <target> [add|remove] [<permission>]` Lists, adds and removes permissions from players. See [permissions](#permissions)

## Mute time

When muting someone, it is possible to specify the duration of the mute. Durations are specified in this format:
```
<years>y <days>d <hours>h <minutes>m <seconds>s
```
You don't have to specify every variable, but they have to be in the same order as above. Spaces are ignored.

### Examples
One year and 5 days: `1y 5d`  
Five hours: `5h`  
1 day, 1 minute and 1 second: `1d1m 1s`

## Staff chat
Staff chat is a seperate channel of chat only staff are allowed to send to or receive messages from.
When running `/staffchat` with no other arguments, your staff chat mode will be toggled.
While in staff chat mode, all messages sent are sent to staff chat.

Using `/staffchat` with arguments (e.g. `/staffchat Hello!`) will not toggle your staff chat mode, but send your message straight to staff chat.

## Permissions
Every command has a permission bound to it. For all commands this is just the name of the command (e.g. `/heal`: `heal`)

For staffchat, the `staffchat.view` permission will allow a player to view, but not send to [staff chat](#staff-chat).  
Add the `kick` permission to allow a player to use vanilla's `kick` command  
Add the `ban` permission to allow a player to use vanilla's `ban` command

### Examples of `/permission`
List permissions of Dinnerbone: `/permission Dinnerbone`  
Add `heal` to Notch: `/permission Notch add heal`  
Remove `feed` from jeb_: `/permission jeb_ remove feed`  
