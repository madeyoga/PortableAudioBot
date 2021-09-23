# PortableAudioBot
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/madeyoga/portableaudiobot?style=for-the-badge)](https://www.codefactor.io/repository/github/madeyoga/portableaudiobot)
[![contributionswelcome](https://img.shields.io/badge/contributions-welcome-brightgreen/?style=for-the-badge)](https://github.com/madeyoga/PortableAudioBot/issues)
[![discord_invite](https://img.shields.io/discord/458296099049046018?style=for-the-badge)](https://discord.gg/Y8sB4ay)

Completely free Music Discord bot designed to be portable and easy for anyone to host on their own machine.

**Project is still in early access, bugs are to be expected.**


## Features
- Support slash command
- Portable executable
- Support 'search & pick to play'
- Supported formats: https://github.com/sedmelluq/lavaplayer#supported-formats


## Command List
- join
- song
- pause
- play
- repeat
- shuffle
- skip
- stop
- volume
- search


## Getting started
Run bot on your local machine
- Register and create your bot application on Discord Developer Portal https://discord.com/developers/applications
- Under OAuth2 tab, reveal Client Secret, copy and save it somewhere else. This token later will be used to login to your bot application, keep it secret.
- Under OAuth2 tab and scopes section, check `bot` option and under bot permissions, check the permission you wanna give to your bot. Use the url to invite your bot to your server.
- Download Portable bot from [release](https://github.com/madeyoga/PortableAudioBot/releases).
- Extract rar and run `PortableBotAudio.exe`.
- There will be a console asking you to input a bot token, paste your token there and press enter.
- If there is no error, you will see this output:
```sh
[main] INFO JDA - Login Successful!
[JDA MainWS-WriteThread] INFO WebSocketClient - Connected to WebSocket
[JDA MainWS-ReadThread] INFO JDA - Finished Loading!
```
- Go to Discord and you should see your bot online.


## Used tools
- [JDA](https://github.com/DV8FromTheWorld/JDA)
- [lavaplayer](https://github.com/sedmelluq/lavaplayer)
- [launch4J](http://launch4j.sourceforge.net/)
