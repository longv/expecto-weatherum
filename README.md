# Expecto Weatherum!
![Workflow result](https://github.com/longv/expecto-weatherum/workflows/Check/badge.svg)

## :scroll: Description
Expecto Weatherum is a real time weather application implemented in order to experiment JetpackCompose, which is a new Android UI Toolkit.

## :bulb: Motivation and Context
In this application, I wanted to explore how Compose works and how it can be integrated with existing application. Since it is only related to the UI of the app, I should be able to plug the old code out and place the new one in with ease. In the end, I was able to build a pretty solid product that show real time weather for multiple locations, while kepping the code clean and maintainable.

To get start with, let's take a look at what I have used:
* At the front, I have built the whole UI with [Compose](https://developer.android.com/jetpack/compose?gclid=Cj0KCQjwo-aCBhC-ARIsAAkNQiuh3Wfih3Ngh5F8Ahv2CKILNkfhuROSntCZgxUG8g21snytun6MLloaAhuzEALw_wcB&gclsrc=aw.ds), even a view pager functionality thanks to a open source project.
* In the middle, UI will talk to [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel?gclid=Cj0KCQjwo-aCBhC-ARIsAAkNQit5MT0TpevRASFsw81xr6NlzuyTzQQMnQYNAy2TVxGFT624g014pqEaAn5XEALw_wcB&gclsrc=aw.ds), a JetPack Architecture component, to handle UI events and return consumable UI models through channels called [LiveData](https://developer.android.com/topic/libraries/architecture/livedata).
* At the back, ViewModel will ask a Repository, which uses [Google Time Zone Api](https://developers.google.com/maps/documentation/timezone/overview#:~:text=Google%20Maps%20Services.-,Introduction,latitude%2Flongitude%20pair%20and%20date.), [Google Places Api](https://developers.google.com/maps/documentation/places/web-service/overview) and [Climacell Api](https://www.climacell.co/weather-api/?utm_adgroup=climacell_api&utm_source=google&utm_medium=cpc&utm_campaign=API_-_Brand_-_US_+_T2&utm_term=climacell%20api&utm_content=505368676922&hsa_acc=4679135646&hsa_kw=climacell%20api&hsa_net=adwords&hsa_cam=12081902124&hsa_ad=505368676922&hsa_grp=119323415911&hsa_src=g&hsa_mt=e&hsa_tgt=aud-845550136760:kwd-642598639320&hsa_ver=3&gclid=Cj0KCQjwo-aCBhC-ARIsAAkNQivEdHdbMcCb3mnVp2QdH_D2E_KKecNsBoOJXVhswiyfKHmTEbNItmgaAl3JEALw_wcB), to retrieve real time weather for a specific location. In order to make network request, I have used [Retrofit](https://square.github.io/retrofit/) together with [Coroutine](https://developer.android.com/kotlin/coroutines?gclid=Cj0KCQjwo-aCBhC-ARIsAAkNQiv4jfPFBdgE9lwUB_MzTbC_hxSjRaWqnEXHN2nZfNIPXI2aKDgjUcsaAniMEALw_wcB&gclsrc=aw.ds).

The most thing that I am proud of is how quick I can build a quite complex UI with just a few line of codes. If you look at the screenshots, you can see that the UI contains of a pager and a vertical list together an horizontally embedded list. As a result, no adapter, no view holder, no recycler view, and no fragment are required!
Secondly, the app architecture looks quite nice: A unidirectional data flow from UI to view model, which means UI -> ViewModel -> UI. Although the communication between ViewModel and Repository (data layer) is still a traditional way where ViewModel will ask for data when needed, I'm looking forward to implement the same unidirectional flow approach. Moreover, it will be good to try out [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html) proposed by Uncle Bob for the whole app.

Since the app is real time, API keys are required. If you want to test it out, create a **config.properties** in the root project and put your keys and urls as shown below:
```
climacell.api.key=//YOUR_CLIMACELL_API_KEY
climacell.base.url=https://data.climacell.co/v4/
google.map.api.key=//YOUR_GOOGLE_MAP_API_KEY
google.map.base.url=https://maps.googleapis.com/maps/api/
```

## :camera_flash: Screenshots
<img src="/results/screenshot_1.png" width="260">&emsp;<img src="/results/screenshot_2.png" width="260">

## License
```
Copyright 2020 The Android Open Source Project

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```