# Avalon

[![discord invite](https://img.shields.io/badge/discord-%237289DA.svg?&style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/t5bMCmQMjD)
[![docker](https://img.shields.io/badge/docker-%232496ED.svg?&style=for-the-badge&logo=docker&logoColor=white)](https://hub.docker.com/repository/docker/jjoeldaniel/avalon-discord)

> A multi-purpose Discord bot  that includes music, moderation, utility, and more!

## **Table of Contents**

- [FAQ](#faq)
- [Commands List](#commands-list)
- [Building From Source](#building-from-source)

## **FAQ**

### **1) What is the command list?**

   Use `/help` for the complete commands list.

### **2) What functions does Avalon provide?**

   Avalon currently supports Music (Spotify and YouTube), Translation (only to English, sorry!), Moderation commands (purge , broadcast), Utility commands (ping, avatar, poll) and Fun commands (8ball, truth or dare).

### **3) What permissions does Avalon require?**

Avalon requires various permissions (all previewed when inviting) and for its role to be moved up as high as possible in the role hierarchy.

## **Commands List**

Go to:
[General](#general),
[Reminders](#reminders),
[Configuration](#configuration),
[Moderation](#moderation),
[Music](#music)

### General

| Command                                               | Description                                  |
|-------------------------------------------------------|----------------------------------------------|
| `/help`                                               | List commands                                |
| `/ping`                                               | Pings bot                                    |
| `/coinflip`                                           | Flips a coin                                 |
| `/truth`                                              | Requests truth                               |
| `/dare`                                               | Requests dare                                |
| `/avatar`                                             | Retrieves user (or target) profile picture   |
| `/8ball`                                              | Asks the magic 8ball a question              |
| `/confess`                                            | Sends anonymous confession                   |
| `/report confession`                                  | Report a confession by its confession number |
| `/join`                                               | Request for bot to join VC                   |
| `/leave`                                              | Request for bot to leave VC                  |

### Reminders

| Command                                               | Description                                  |
|-------------------------------------------------------|----------------------------------------------|
| `/reminder new`                                       | Add a new reminder                           |
| `/reminder list`                                      | Lists all stored reminders                   |
| `/reminder reset`                                     | Resets all stored reminders                  |
| `/reminder delete`                                    | Delete a stored reminder                     |
| `/reminder toggle`                                    | Toggles notifications                        |

### Configuration

| Command                                           | Description                  |
|---------------------------------------------------|------------------------------|
| `/config`                                         | Configure server settings    |
| `/config_view`                                    | View server settings         |
| `/star`                                           | Configure starboard settings |
| `/toggle`                                         | Toggles Avalon features      |

### Moderation

| Command                                              | Description                 |
|------------------------------------------------------|-----------------------------|
| `/purge`                                             | Purges messages (up to 100) |
| `/poll`                                              | Submits poll to be voted on |
| `/broadcast`                                         | Sends message as Avalon     |

### Music

| Command                                           | Description                                           |
|---------------------------------------------------|-------------------------------------------------------|
| `/play`                                           | Plays Youtube and Spotify                             |
| `/pause`                                          | Pauses playback                                       |
| `/resume`                                         | Resumes playback                                      |
| `/clear`                                          | Clears queue                                          |
| `/queue`                                          | Displays song queue                                   |
| `/playing`                                        | Displays currently playing song                       |
| `/volume`                                         | Sets volume                                           |
| `/loop`                                           | Loops the currently playing song                      |
| `/shuffle`                                        | Shuffles music queue                                  |
| `/skip`                                           | Skips song with an optional song number specific skip |
| `/seek`                                           | Seeks song position                                   |

## **Building From Source**

1) Clone the repo

   ```console
   git clone https://github.com/jjoeldaniel/Avalon.git
   ```

2) Build with Maven

   ```console
   mvn
   ```

3) Place your env text file in /target/ alongside the jar file

   - Follow the template provided in [example-env.txt](https://github.com/jjoeldaniel/Avalon/blob/main/example-env.txt)

4) Run jar

   ```console
   java -jar avalon-v1-jar-with-dependencies.jar
   ```
