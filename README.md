# MultiTenant CorDapp
This CorDapp serves as a demo of building an auction application on Corda using multiTenant approach: 2 subscriptions with testing use dynamic flow.

## CorDapp Components
### States
- `Asset`: It is an `OwnableState` that represents an asset that could be put on auction. The owner
of the asset should be able to put this asset on an auction.

### Contracts:
- `AssetContract`: It is used to govern the evolution of the asset. In this case mostly change of 
ownership. Left black for simplicity. Has two commands, `CreateAsset` and `TransferAsset`.

### Flows:
- `CreateAssetFlow`: This flow is used create an asset.

## Pre-requisites:
See https://docs.corda.net/getting-set-up.html.

## Running the nodes:
 
See https://docs.corda.net/tutorial-cordapp.html#running-the-example-cordapp.

## Running the client:
The client can be run by executing the below command from the project root:

`./gradlew runAuctionClient`

Please make sure that the nodes are already running before starting the client. 
The client can be accessed at http://localhost:8085/

## Usage

1. To get demo data do 
GET request to http://localhost:8085/api/auction/asset/list/{party}

    Where {party}:
    - PartyB
    - PartyC

2. Check logs, it should includes information about state change for B/C Party
