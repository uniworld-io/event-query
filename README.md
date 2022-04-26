#  Unichain Event Query Service
unichainEventQuery is implemented with unichain's new event subscribe model.   	
It uses same query interface with unichain-Grid. Users can also subscribe block trigger, transaction trigger, contract log trigger, and contract event trigger.   	
UnichainEvent is independent of a particular branch of java-unichain, the new event subscribes model has already be released on version 3.5 of java-unichain.	

 For more information of unichain event subscribe model, please refer to https://github.com/unichainprotocol/TIPs/issues/12.	

 ## Download sourcecode	
git clone https://github.com/unichainprotocol/unichain-eventquery.git        	

cd unichaineventquery	

## Build	
**mvn package**  	

 After the build command is executed successfully, unichaineventquery jar to release will be generated under unichaineventquery/target directory. 	
Configuration of mongodb "config.conf" should be created for storing mongodb configuration, such as database name, username, password, and so on. We provided an example in sourcecode, which is " unichaineventquery/config.conf ". Replace with your specified configuration if needed.	

 **Note**: 	
Make sure the relative path of config.conf and unichaineventquery jar. The config.conf 's path is the parent of unichaineventquery jar.	

  - mongo.host=IP 	
 - mongo.port=27017 	
 - mongo.dbname=eventlog	
 - mongo.username=unichain	
 - mongo.password=123456	
 - mongo.connectionsPerHost=8	
 - mongo.threadsAllowedToBlockForConnectionMultiplier=4	

 Any configuration could be modified except **mongo.dbname**, "**eventlog**" is the specified database name for event subscribe.	

 ## Run	
- unichaineventquery/deploy.sh is used to deploy unichaineventquery	
- unichaineventquery/insertIndex.sh is used to setup mongodb index to speedup query. 	
(make sure run insertIndex before create collecions)	

 ## Delete expire data	
- unichaineventquery/deleteData.sh is used to delete expire data
- using crontable delete regularly mongodb expire data(if not delete, the database will be too big)


## What is the main HTTP service?
baseUrl: https://api.unichainex.io

## Main HTTP Service  
Function: get transaction list
```
subpath: $baseUrl/transactions

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
block: start block number, default is 0

Example: https://api.unichainex.io/transactions?limit=1&sort=-timeStamp&start=2&block=0
```

Function: get transaction by hash
```
subpath: $baseUrl/transactions/{hash}

parameters   
hash: transaction id

Example: https://api.unichainex.io/9a4f096700672d7420889cd76570ea47bfe9ef815bb2137b0d4c71b3d23309e9
```
Function: get transfers list
```
subpath: $baseUrl/transfers	

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
from: from address, default is ""
to: to address, default is ""
token: tokenName, default is ""

Example: https://api.unichainex.io/transfers?token=trx&limit=1&sort=timeStamp&start=2&block=0&from=TJ7yJNWS8RmvpXcAyXBhvFDfGpV9ZYc3vt&to=TAEcoD8J7P5QjWT32r31gat8L7Sga2qUy8
```
Function: get transfers by transactionId
```
subpath: $baseUrl/transfers/{hash}

parameters   
hash: transfer hash

Example: https://api.unichainex.io/transfers/70d655a17e04d6b6b7ee5d53e7f37655974f4e71b0edd6bcb311915a151a4700
```
Function: get events list
```
subpath: $baseUrl/events

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
since: start time of event occurrence, timeStamp >= since will be shown
start: start page, default is 1
block: block number, block number >= block will be shown

Example: https://api.unichainex.io/events?limit=1&sort=timeStamp&since=0&block=0&start=0
```
Function: get events by transactionId
```
subpath: $baseUrl/events/transaction/{transactionId}

parameters   
transactionId

Example: https://api.unichainex.io/events/transaction/cd402e64cad7e69c086649401f6427f5852239f41f51a100abfc7beaa8aa0f9c
```
Function: get events by contract address
```
subpath: $baseUrl/events/{contractAddress}

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
since: start time of event occurrence, timeStamp >= since will be shown
block: block number, block number >= block will be shown
contractAddress: contract address
start: start page, default is 1

Example: https://api.unichainex.io/events/TMYcx6eoRXnePKT1jVn25ZNeMNJ6828HWk?limit=1&sort=-timeStamp&since=0&block=0&start=4
```
Function: get events by contract address and event name
```
subpath: $baseUrl/events/contract/{contractAddress}/{eventName}

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
since: start time of event occurrence, timeStamp >= since will be shown
contract`Address`: contract address
start: start page, default is 1
eventName: event name

Example: https://api.unichainex.io/events/contract/TMYcx6eoRXnePKT1jVn25ZNeMNJ6828HWk/Bet?limit=1&sort=timeStamp&since=1&start=0
```
Function: get events by contract address, event name and block number
```
subpath: $baseUrl/events/contract/{contractAddress}/{eventName}/{blockNumber}

parameters   
contractAddress: contract address
blockNumber: block number, block number >= block will be shown
eventName: event name


Example: https://api.unichainex.io/events/contract/TMYcx6eoRXnePKT1jVn25ZNeMNJ6828HWk/Bet/4835773
```
Function: get events by timeStamp
```
subpath: $baseUrl/events/timestamp

parameters   
since: start time of event occurrence, timeStamp >= since will be shown
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
contract: contract address


Example: https://api.unichainex.io/events/timestamp?since=1544483426749&limit=1&start=1&sort=timeStamp
```
Function: get confirm events list
```
subpath: $baseUrl/events/confirmed

parameters   
since: start time of event occurrence, timeStamp >= since will be shown
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1


Example: https://api.unichainex.io/events/confirmed?since=1544483426749&limit=1&start=1&sort=timeStamp
```
Function: get block by block hash
```
subpath: $baseUrl/blocks/{hash}

parameters   
hash: block hash


Example: https://api.unichainex.io/blocks/000000000049c11f15d4e91e988bc950fa9f194d2cb2e04cda76675dbb349009
```
Function: get block list
```
subpath: $baseUrl/blocks

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
block: block number, block number >= block will be shown 


Example: https://api.unichainex.io/blocks?limit=1&sort=timeStamp&start=0&block=0
```
Function: get latest block number
```
subpath: $baseUrl/blocks/latestSolidifiedBlockNumber

parameters   
none

Example: https://api.unichainex.io/blocks/latestSolidifiedBlockNumber
```
Function: get contract log list
```
subpath: $baseUrl/contractlogs

parameters   
limit: each page size, default is 25
sort: sort Field, default is sort by timeStamp descending order
start: start page, default is 1
block: block number, block number >= block will be shown 

Example: https://api.unichainex.io/contractlogs
```
Function: get contract log list based on transactionId
```
subpath: $baseUrl/contractlogs/transaction/{transactionId}

parameters   
transactionId

Example: https://api.unichainex.io/contractlogs/transaction/{transactionId}
```
Function: post abi string and get contract log list based on transactionId(release on 3.6)
```
subpath: $baseUrl/contract/transaction/{transactionId}

parameters   
transactionId
body:
abi: user self upload abi

Example: https://api.unichainex.io/contract/transaction/{transactionId}
```
Function: get contract log list based on contractAddress
```
subpath: $baseUrl/contractlogs/contract/{contractAddress}

parameters   
contractAddress

Example: https://api.unichainex.io/contractlogs/contract/{contractAddress}
```
Function: post abi string and get contract log list based on contractAddress(release on 3.6)
```
subpath: $baseUrl/contract/contractAddress/{contractAddress}

parameters   
contractAddress
abi: user self upload abi

Example: https://api.unichainex.io/contract/contractAddress/{contractAddress}
```
Function: get contract log list based on uniqueId
```
subpath: $baseUrl/contractlogs/uniqueId/{uniqueId}

parameters   
uniqueId

Example: https://api.unichainex.io/contractlogs/uniqueId/{uniqueId}
```
Function: post abi string and get contract log list based on uniqueId(release on 3.6)
```
subpath: $baseUrl/contract/uniqueId/{uniqueId}

parameters   
uniqueId
abi: user self upload abi

Example: https://api.unichainex.io/contract/uniqueId/{uniqueId}
```
