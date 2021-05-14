# Weather Notification Center (aka WNC)
Periodic Weather notification to registered users

**Use case**: People are busy with personal and professional life. One can get carried away without giving appropriate attentions and time for 
physical activity.</br>
I personally feel, a person need to spend a certain amount of time each day closely with nature. But, one do not have an idea what is the best 
time of a day to plan outdoor activities such as: A walk around the park, Cycling outside in nature etc on a given day.</br>
WNC would notify you with best time of a day 24 hours ahead. One just need to register to the service and choose a list of options 
such as temperature, humidity, wind speed etc. Based on user choice, wnc would notify an user with all the possible times of a day. </br>
Ex: Temperature greater than 65 F, Humidity less than 65% and Wind speed less than 10 mil/hr.</br>


How it is build?

WNC uses OpenWeather APIs to retrive weather data of a day. WNC has a cron job that send notification to users. Cron job runs every 24 hours. 
Twilio APIs are used to notify user with SMS.</br>


**WNC API**:

* Register user - POST - /wnc/regiser
* Add preference - POST - /wnc/user/preference
* Update preference - PUT - /wnc/user/preference
* Delete preference - DELETE - /wnc/user/preference
* Adoc request to get weather data of a geo location - GET - /wnc/getweather?lat=37.45461795594225&lon=-121.91812338570007