# Fitness Predictor

## How to Setup

### Endomondo

The application requires an Endomondo account. You can create an account
[here](https://www.endomondo.com/).

### Android Studio

The easiest way to setup your development environment is to install [Android
Studio](https://developer.android.com/studio/install).

You need to install an emulator and the sdk. You can then open Android Studio
and open the project.

Press on the 'Run app' button to start the application. You can choose to run it
on either an emulator or a physical device.

### Local account

The application requires a local account. You can register by clicking on the
Register button in the Login prompt.

## Screenshots

Here is a list of screenshots showing the functionality of the application.

### Dashboard

![Dashboard](screenshots/dashboard.png)

The dashboard has 3 tabs: fitness, weather and analysis. The fitness tab display
data from Endomondo.

### Endomondo Login

![Endomondo Login](screenshots/endomondo_login.png)

The user is required to log in to Endomondo to be able to go further into the
application. It is necessary because we need to fetch the fitness data to
display it.

### Local Login

![Local Login](screenshots/local_login.png)

The user is also required to log in with our Firebase database. The user is able
to register an account using the Register button.

### Local Registration

![Registration](screenshots/local_registration.png)

The user is able to register to our application.

### Log out action

![Log out](screenshots/logout_action.png)

The log out action logs out the user from both Endomondo and local login. After
pressing this button, the application will go back to login activities.

## Dependencies

- Firebase
- [GraphView](www.android-graphview.org/)
- [Fork of Endo2java](https://github.com/tomleb/endo2java/)
- Google services
