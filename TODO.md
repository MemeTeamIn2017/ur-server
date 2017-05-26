# TODO
1. Implement packets with data classes and use the [jackson-kotlin-module](https://github.com/FasterXML/jackson-module-kotlin)
for serialization/deserialization.
    * In order to check invalid packets and such, try-catch clauses
    * ClassCastExceptions an such, also, jackson probably handles `null` properly, so 
    optional fields won't be a problem.

