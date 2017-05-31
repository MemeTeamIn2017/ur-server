# Clients
The game curerntly has an development client hosted on http://urgame.freeddns.org/ur/
Though it won't be available 24/7.

# Ur Game Protocol Specification

This protocol uses JSON as a data transfer means and it inherits
all the restrictions of the JSON standard. It is designed to be
easy to develop for and expand while being adequately fast to parse.

This protocol defines a concept of _PACKET_ as an _COMPLETE_ object being
sent or received. Every packet is a valid JSON object. It MUST contain
a field `{"id" : "PACKET_ID"}` which is the **PACKET_ID** of this packet, other
fields' contents are defined by the **PACKET_ID**.

# Packets
## Client can send...
#### AUTHENTICATE - "AUTH"

This is the first packet the server receives. If any other packet is received before this one, the server will automatically ban for 1 hour (subject to change).

_**Mandatory fields**_
* name: String - the name of the player. These MUST be without `<` and `>` characters in them, and longer than 4 characters.

**Optional fields**
* locale:String - the locale (country code) that this player wants to use, it is going to be used to display a flag-icon next to their name or whatever.

Example:
```json
{
  "id": "AUTH",
  "name": "NoobSlay3r",
  "locale": "en"
}
```

#### CHALLENGE PLAYER - "CHALLENGE_PLAYER"
Challenges another player in a game of Ur.
The other Player can respond with a Yes/No. The client should wait for this response.

Example: 
```json
{
  "id": "CHALLENGE_PLAYER",
  "opponent": "Noob1337"
}
```
## Server can send...

#### AUTHENTICATION STATUS - "AUTH_STATUS"
This is the packet that the client receives after an authentication attempt.
if `successful` is `true`, `reason` is not going to be present.
Otherwise, `reason` contains the reason for the auth fail.

**Mandatory fields** 
* successful: Boolean - the status, `true` if it was successful, `false` otherwise.

**Optional fields**
* reason: String - the reason for the Auth fail.
* locale: String - if the authentication request was successful and the user had used GeoIP to determine the country
code, this field will contain the country code of the player's GeoIP location.

Possible values for `reason`:
* TAKEN - if the name is already in use.
* ILLEGAL_CHARACTERS - contains `<` or `>` 
* LENGTH - too short or too long (must be >= 4 and <= 20))
* INVALID_LOCALE - if the locale string is not a valid one.
* SUSPENDED - NYI.

*TODO add other reasons*

Example:

```json
{
  "id": "AUTH_STATUS",
  "successful": false,
  "reason": "NAME_TAKEN"
}
```
#### LIST OF PLAYERS IN THE LOBBY - "LOBBY_LIST"
This packet will be sent right after AUTH_STATUS for the newly authenticated player,
to be updated on the lobby player list. This packet is never going to be sent after that.
Instead, for each player joining or leaving there will be another packet.

* players:Array(set) - the players in the lobby.

```json
{
  "id": "LOBBY_LIST",
  "players": [
    {"name": "NoobSprayer442", "locale": "en"},
    {"name": "MasterOfDisa5ter", "locale": "bg"}
  ]
}
```

#### PLAYER JOINED LOBBY - "PLAYER_JOINED_LOBBY"
This packet will be sent to all players everytime a player joins the lobby (a.k.a is authenticated).

**Mandatory fields**
* player:Object - the player object
    * name:String - the name of the player. These MUST be without `<` and `>` characters in them.
    * locale:String - the locale of the player. This is set by the player, but the default is GeoIP.

Example:
```json
{
  "id": "PLAYER_JOINED_LOBBY",
  "player": {
    "name": "NoobMaster3311",
    "locale": "bg"
  }
}
```

#### PLAYER LEFT LOBBY - "PLAYER_LEFT_LOBBY"
This packet will be sent to all players everytime a player leaves the lobby (a.k.a is disconnected).

**Mandatory fields**

- name:String - the name of the player. These MUST be without `<` and `>` characters in them.

Example:
```json
{
  "id": "PLAYER_LEFT_LOBBY",
  "name": "NoobMaster3311",
  "locale": "bg"
}
```

#### CHALLENGE RESPONSE - "CHALLENGE_RESPONSE"
This packet is sent to a client when their opponent accepts or declines the request for a PvP game.

**Mandatory fields**
* opponent:String - the name of the opponent.
* accepted:Boolean - `true` if the opponent accepted, `false` otherwise.

Example:
```json
{
  "id": "CHALLENGE_RESPONSE",
  "opponent": "Noob1337",
  "accepted": true
}
```


# Authors
* [MemeTeam](mailto://memeteam1997@gmail.com), Memebers:
    * [Bojidar Borislavov Stoyanov (Божидар Бориславов Стоянов)](mailto://glav0r3zzz4@gmail.com)
    * [Kaloyan Ivanov Mitev (Калоян Иванов Митев)](http://todo.com)
    * [Yoanna Ivanova (Йоана Иванова)](http://todo.com)

