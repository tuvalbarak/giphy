# Giphy Api Application

I used "https://api.giphy.com" to fetch data, while using two endpoints: "v1/gifs/search" and "v1/gifs/trending".

What the app can do - 
- Display a list of gifs (if the user didn't use the search option - Giphy trending gifs will be displayed).
- When a short click on a gif is being performed - the user will be navigated to a new fragment which shows the selected gif.
- When a long click on a gif is being performed - a new menu (Contextual Action Mode) will be revealed - it contains the following options: open a dialog with details of the current gif, share the gif, and save the gif(didn't implement the save option).
- Menu options - the user can start/stop the music (his choice will be saved for the next time he opens the app using shared preferences).

Architecture - 
- I wrote the app using the MVVM architecture, thus achieving a total separation of layers, which makes the app scalable, organized, and gives me the ability to easily change and test each module.
- I tried to emphasize OOP concepts and to use as much as I can in design patterns to achieve a reusable and readable code (Singleton, Observer, Factory, Template - Generics).
- The app is written based on the Single-Activity architecture. This helps me to take advantage of the use of fragments as much as possible.

Technologies - 
- The app is written in Kotlin, and it uses LiveData to enable stateless app, Repository to create an abstract layer between the Viewmodel and the DB/API, Viewmodel to perform the entire app logic, Coroutines to achieve better performance with asynchronous functions, NavGraph to easily navigate and send data (SafeArgs and Parcelize) between fragments, RxJava to efficiently take care of the user's search, CAB (Contextual Action Mode) to perform actions on a specific item, DiffUtils to efficiently compare list rows, extension functions to provide a readable and reusable code, Glide to load, display and cache gifs, implicit Intents to share gifs, MediaPlayer API to play background music, and Retrofit to fetch data using API calls.

How to Make It Better -
- In a production app, I would add Pagination to enable a faster loading and an overall better UX.
- I would create an object that will take care of the entire Shared Preferences logic.
- I would impelemnt the "Save" option, so a user will be able to save a gif to his own gallery.
- Add a feedback that a gif was clicked (I managed to make it working while reducing the alpha value of the itemView in the adapter, but couldn't manage to restrict that only one item at a time will be selected without refreshing the entire list - which I didn't want to).

* I decided to write the background music related code in GifFragment to keep the MainActivity empty (although it comes in expense that the music is bound to the fragment lifecycle). In a production app I would implement the second fragment as a dialog, and then this issue won't matter (I implemented it as fragment to show navigation between fragments with SafeArgs and Parcelize).

Below you will find some photos from the app, and a link to a YouTube video I made to demonstrate the app flows.

https://youtu.be/ltC7GH6dFP4

![image](https://drive.google.com/uc?export=view&id=1K7QTaltkX4yZFlsqbIjy02n4D3ZcuWSR)
![image](https://drive.google.com/uc?export=view&id=1o0FTqN2au01wX4N0kSs4CG9-9yvS0Z5A)
![image](https://drive.google.com/uc?export=view&id=1N8Iy3yE5uMOXlVTwFyvDASNadlAxkrz7)
![image](https://drive.google.com/uc?export=view&id=17VbOgmsotRFvOPHFiTrwiK4NPtWZrsYs)
![image](https://drive.google.com/uc?export=view&id=14E03FF3AIhH1_g2BRqj5sqvuXTL_vp0S)
![image](https://drive.google.com/uc?export=view&id=1GvBpoZE3JV0p5FWxcD8LzFHTqe85_CCo)
![image](https://drive.google.com/uc?export=view&id=1dgv5ztgBgFwFS9Wyf7eaqGwZg6-UB3XC)
![image](https://drive.google.com/uc?export=view&id=1ngvBlnjCTZmdzZb4Z-zQwDPr_WwXQdxl)
![image](https://drive.google.com/uc?export=view&id=1VEMZ095Y_kId-9QcrxsLtRzLzfBSJHKv)
