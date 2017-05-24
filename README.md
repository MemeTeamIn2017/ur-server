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

Data fields:
* name: String - the name of the player.

Example:
```json
{
  "id": "AUTH",
  "name": "NoobSlay3r"
}
```


#### CHALLENGE PLAYER - "CHALLENGE_PLAYER"
Challenges another player in a game of Ur.
The other Player can respond with a Yes/No. The client should wait for this response. (RESPONSE_HERE)
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

* successful: Boolean - the status, `true` if it was successful, `false` otherwise.
* [optional] reason: String - the reason for the Auth fail.

Possible values for `reason`:
* NAME_TAKEN
* SUSPENDED

*TODO add other reasons*

Example:

```json
{
  "id": "AUTH_STATUS",
  "successful": false,
  "reason": "NAME_TAKEN"
}
```
#### CHALLENGE RESPONSE - "CHALLENGE_RESPONSE"
This packet is sent to a client when their opponent accepts or declines the request for a PvP game.

Fields:
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

