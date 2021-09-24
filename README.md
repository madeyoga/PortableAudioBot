# PortableAudioBot
[![CodeFactor Grade](https://img.shields.io/codefactor/grade/github/madeyoga/portableaudiobot?style=for-the-badge)](https://www.codefactor.io/repository/github/madeyoga/portableaudiobot)
[![contributionswelcome](https://img.shields.io/badge/contributions-welcome-brightgreen/?style=for-the-badge)](https://github.com/madeyoga/PortableAudioBot/issues)
[![discord_invite](https://img.shields.io/discord/458296099049046018?style=for-the-badge)](https://discord.gg/Y8sB4ay)

A Music Discord bot designed to be portable and easy for anyone to setup and run on their machine.

**Project is still in early access, bugs are to be expected.**


## Features
- Easy to setup and run
- Portable executable
- Cross platform
- Support many sites
- Support slash command
- Support 'search & pick to play'


## Supported sources
Supports all sources and formats supported by [lavaplayer](https://github.com/sedmelluq/lavaplayer#supported-formats):

### Sources
- YouTube
- SoundCloud
- Bandcamp
- Vimeo
- Twitch streams
- Local files
- HTTP URLs

## Command List
- join
- song
- pause
- play
- repeat
- shuffle
- skip
- stop
- search


## Getting started
Create your bot on Discord Developer Portal:
- Register and create your bot application on Discord Developer Portal https://discord.com/developers/applications
- Under OAuth2 tab, reveal Client Secret, copy and save it somewhere else. This token later will be used to login to your bot application, keep it secret.
- Under OAuth2 tab and scopes section, check `bot` option and under bot permissions, check the permission you wanna give to your bot. Use the url to invite your bot to your server.

There are 2 ways to run PortableAudioBot:
- Using executable file or
- Using the executable jar file.

## Using executable from PortableAudioBot.zip
Recommended for users that don't have Java installed.
- Download `PortableAudioBot.zip` from [release](https://github.com/madeyoga/PortableAudioBot/releases/tag/v0.0.2) or simply [click here](https://github.com/madeyoga/PortableAudioBot/releases/download/v0.0.2/PortableAudioBot.zip).
- Extract the zip file, and open the `settings.txt` file
- Replace the dummy token `xxxxxxxxx` with your bot token. You can also change the prefix, by default the prefix is `!`.
- Run the bot by simply double click the executable file `PortableBot.exe`
- If there is no error, you will see this output:
```sh
[main] INFO JDA - Login Successful!
[JDA MainWS-WriteThread] INFO WebSocketClient - Connected to WebSocket
[JDA MainWS-ReadThread] INFO JDA - Finished Loading!
```
- Go to Discord and you should see your bot online.


## Using the executable jar file
Recommended for users that already know Java or already have Java installed
- Download the `PortableAudioBot.main.jar` and the `settings.txt` file from [release](https://github.com/madeyoga/PortableAudioBot/releases/latest/).
- Put them in the same folder.
- Open up `settings.txt`. You will see a dummy token and a default prefix `!`.
  - Replace the `xxxxxxxxxxx` with your bot token.
- Next, open up command line on your bot directory & run the jar file with `java -jar PortableAudioBot.main.jar`
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
