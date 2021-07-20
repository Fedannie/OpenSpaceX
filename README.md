# OpenSpaceX
One-page Web application with provided Rest API that reveals insights about SpaceX flights.

## Setup and Launch
To launch the application locally you need to open the openspacex-server folder in the terminal, and run:
`mvn spring-boot:run`
This will start the REST server. To open the UI Web page, open [http://localhost:8080/ui](http://localhost:8080/ui) in your browser. 
Make sure that you have maven and java installed on your machine.

## RESTful API
#### Base URL
`http://localhost:8080/`

### Routes

#### Number of launches

**Method**  :  `GET`
**URL**  :  `http://localhost:8080/launches_count`
**Response type** : `Integer`
**Extra URL parameter** : `year: Integer`
Gives information about the number of launches performed by SpaceX company during the specified year. If a parameter `year` is not passed - it counts the total number of launches.

------------------
#### Success rate
**Method**  :  `GET`
**URL**  :  `http://localhost:8080/success_rate`
**Response type** : `Double`
**Extra URL parameter** : `year: Integer`
Gives information about the success rate of launches performed by SpaceX company during the specified year. If a parameter `year` is not passed - it counts the success rate of all the launches. The returned value lies in `[0, 1]` and shows the ratio successful launches to the total number of launches.

----------------------------
#### Crew size
**Method**  :  `GET`
**URL**  :  `http://localhost:8080/crew`
**Response type** : `Integer`
**Extra URL parameter** : `year: Integer`
Gives information about the number of people who participated in the SpaceX flights during the specified year. If a parameter `year` is not passed - it counts the total number of SpaceX astronauts.

----------------------------
#### Cost of launches
**Method**  :  `GET`
**URL**  :  `http://localhost:8080/cost`
**Response type** : `Long`
**Extra URL parameter** : `year: Integer`
Gives information about how much did it cost to perform all the SpaceX launches during the specified year. If a parameter `year` is not passed - it counts the cost of all the SpaceX flights. The returned value is the cost in USD.

----------------------------
#### Total load
**Method**  :  `GET`
**URL**  :  `http://localhost:8080/mass`
**Response type** : `Long`
**Extra URL parameter** : `year: Integer`
Gives information about the weight of the SpaceX rockets on takeoff during the specified year. If a parameter `year` is not passed - it counts the weight of all the SpaceX launches. The returned value is the weight of the rockets, its payloads and fuel in kgs.

----------------------------
#### Rockets
**Method**  :  `GET`
**URL**  :  `http://localhost:8080/rockets`
**Response type** :  
```
Json Object {
	"id" : String
	"name" : String  
	"image" : String  
	"description" : String  
	"cost_per_launch" : Integer 
}
```

Gives information about all the SpaceX rockets.


----------------------------
#### Next launch
**Method**  :  `GET`
**URL**  :  `http://localhost:8080/next_launch`
**Response type** :  
```
Json Object {
	"id" : String
	"name" : String  
	"logo" : String  
	"description" : String  
	"date_utc" : Date 
	"rocket" : Rocket Json Object
	"crew_cnt" : Integer
}
```

Gives information about all the next SpaceX flight.
