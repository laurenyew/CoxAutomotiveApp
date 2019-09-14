# CoxAutomotiveApp

<img src="https://github.com/laurenyew/CoxAutomotiveApp/blob/master/Screenshots/Home.png" width="200" height="400" /> <img src="https://github.com/laurenyew/CoxAutomotiveApp/blob/master/Screenshots/Dealerships.png" width="200" height="400" /> <img src="https://github.com/laurenyew/CoxAutomotiveApp/blob/master/Screenshots/Vehicles.png" width="200" height="400" /> 

## Requirements:
The app should persist dealer and vehicle data and load that cached data when possible (assume individual dealer and vehicle records provided by API are immutable)

The app will have 3 screens:

A home screen with a single button to fetch dealership and vehicle data
A list of dealerships. Each item in the list should display the name of the dealership and its id
A list of vehicles for the selected dealership. Each item in the list should display the following vehicle information:
Year
Make
Model
Vehicle id
Dealership id
 

## Fetching data:

Call get /api/datasetId to get a dataSetId 
2.       Use the datasetId to fetch the dealerships and vehicles for that data set

a.       Call /api/{datasetId}/vehicles to get a list of vehicleIds 

b.       For each id from the vehicleIds list, call /api/{datasetId}/vehicle/{vehicleId} to get the vehicle info 

c.       The vehicle info contains the dealershipId to call /api/{datasetId}/dealers/{dealerId} to get the dealership info.  Make sure to only fetch the dealership data once from all the vehicles. 

## Libraries Used:
* Retrofit
* Jetpack Navigation
* LiveData
* Lifecycles
* Room
* Kotlin Coroutines
