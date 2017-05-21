Protocol Protocol Specificiation

This protocol uses JSON as a data transfer means and it inherits
all the restrictions of the JSON standart. It is designed to be
easy to develop for and expand while being adequately fast to parse.

This protocol defines a concept of PACKET as an COMPLETE object being
sent or received. Every packet is a valid JSON object. It MUST contain
a field {"id" : number} which is the *PACKET_ID* of this packet, other
fields' contents are defined by the *PACKET_ID*. 

== **AUTHENTICATE** packet

