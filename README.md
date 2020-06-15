David William Nartey

The application consists of the Server located in the server file, as well as the client,
located in the Client file. A bash file has been provided for both java files in order 
to create the class file and start the application. Two terminals need to open in order
 for the files to communicate. While in the Server directory, type in "./initServer.sh ".
While in the Client directory, type in "./initClient.sh". If this does not work, within 
the individual directories "javac Server.java" and "java Server" for the Server.
"javac Client.java" and "java Client" for the Client. The Server must be started before 
the client in order to prevent errors.

The Server will open up a port to listen for connections. The Client will randomly select
a computer out of two options and connect to the port and the server will display a 
message confirming the connection.

Authentication of the public and private key encryption works as follows:
Each folder contains a "serverRSA.txt" and "clientRSA.txt" file. These files are different
according to the folder they are in. The Client folder has access to the public key of the
Server while having access to the names of the two computers and their corresponding private
and public keys. The Server folder has access to the public keys of the clients as well as 
the private and public keys of the server.
The client randomly selects a computer to act as the current computer forming the connection.
The client encrypts the computer name according to the server public key and sends the encrypted 
name to the server. The server decrypts the message based on its own private key. The server 
checks the "clientRSA.txt" file for the current computer and its corresponding public key.
The server picks the corresponding public key to the current computer. The server sends a
message to the client that is the name of the decrypted current computer acting as the client.
The server generates a random name intended as the session ID. The server appends session ID to 
the current computer name and encrypts it using the public key of the client which is the current 
computer. The server sends the encrypted message to the client. The client receives this message 
and decrypts it according to the current computer public key that was selected. If the current 
Computer name sent by the server matches the current computer initially sent by the client, then 
the authentication has occurred and the session ID is stored. Otherwise, the authentication failed.
The server closes the connection and the client disconnects.

Test Plan

Test Connection
Server :
Waiting for the client request
Client: 
Sending request to Socket Server
Server:
Connection accepted
Test Passed

Test Encryption 
Message: "Computer2"
Encrypted Message: 124-40-15-90-1890148-587035-5-741081989-63-90-95-12788-116-1043798-1133041-15-119-87-80-67-84-2824-9412164-48-1096596-60-10680-77-5820-6519-70534-908-3-23-45-114-8-93-54-9096-38-116-11-13-68118-80-3246-50-91-128-7-75-933350-97-61-12269576-44-52575461427-57-5-5359-8567-105807761-1103-82-93114-18-7391363-2-463-90-4380-55932-94751239864-8-20-1231051241214294-1108388-787419-44-114-99-34-2-114-35-50-9-111126115112-78-8777-1936-8541849171-8154-107174611986-1941-77-37-65-73-51-3129337710298-32-294-105-15-95219928118-28-104-10-68122-375436-97-4-781-126-20-55-632846-119-38-19-1070118126-123-16-3102120112812117504787-26-912577-68-107-7846100-5785-116-693925-3311212393-93-63-1114

Test Decryption
Encrypted Message:
124-40-15-90-1890148-587035-5-741081989-63-90-95-12788-116-1043798-1133041-15-119-87-80-67-84-2824-9412164-48-1096596-60-10680-77-5820-6519-70534-908-3-23-45-114-8-93-54-9096-38-116-11-13-68118-80-3246-50-91-128-7-75-933350-97-61-12269576-44-52575461427-57-5-5359-8567-105807761-1103-82-93114-18-7391363-2-463-90-4380-55932-94751239864-8-20-1231051241214294-1108388-787419-44-114-99-34-2-114-35-50-9-111126115112-78-8777-1936-8541849171-8154-107174611986-1941-77-37-65-73-51-3129337710298-32-294-105-15-95219928118-28-104-10-68122-375436-97-4-781-126-20-55-632846-119-38-19-1070118126-123-16-3102120112812117504787-26-912577-68-107-7846100-5785-116-693925-3311212393-93-63-1114
Decrypted Message: "Computer2"

Test Sending Encryption and Decrypting
Client Sent Encrypted Message
Server Received Encrypted Message
Test Passed

Test Session ID Generator
Generate Session ID of length 8.
Result: 8H6CqNBg
Test Passes

Test Sending Encryption of Computer Name with Session ID generated
Sent: 4154-4058-101-9047-37-106-10210811511014-60111-7935-6930-858-2595-1226483-19-94-6731115139-808658-30831185066-138625-109-7-1258412-7277663656118-1212127-501960-74-14-7536-11747929-8429107-8-4036-70117-1612431619490-82357-95-75-15122-83-47-78-115579-32-2-828522-121100-121-7536124-74-5417-37-80229-114-467-48126-26-50-105-880-28-5-89-122-17-33699-12-611-10-57-75-7712017-11452-9952127-100-9428-751169819-94-86-21025-4189-20-233483-19-119-77-1081459-98-1-50-593-5663-127-96-9573-72-37772-69732125-75-897512291-5116-8221-88120-25-92169999-106-82-88-43-1286847229-25-93-8-700771668-119-4112-10950-76-70-5163176-46-614-1498-39125-97111122-3-50-491586411013424-33188

Decrypted: "Computer2 8H6CqNBg"
Test Passed

Test Confirmation of Reception
Received by the Client

Test Authentication 
Computer Name matches what was sent.
Test Passed


Computer1 encrypted by the client will be the same every time due to the same 
public key used. Same for Computer2. 


Discussion
The method used for Assymetric Encryption is far more secure than a shared secret key between 
computers. The advantage of the public and private key system is that only the private key 
may be used to decrypt a message so gaining access to the public key will have no bearing on 
the communication received by the private key owner although they may be susceptible to 
communication from anyone that has the public key. The methodology is still susceptible to 
man in the middle attacks if someone is able to intercept messages and the exchange of keys 
between to bodies. Public key encryption is also susceptible to replay attacks. If the public 
key is known, a malicious actor may encrypt malicious code with the public key to the private 
key owner. The owner would decrypt it with the private key and this would result in the computer
being compromised. Public Key Encryption is at a disadvantage in some respects due to the lack 
of built in authentication. As in this project, we were required to make our own forms of 
authentication and this can lead to user error as the code is only as secure as the person 
who made it. The inherent disadvantage is that it is possible for someone to pose as another person.