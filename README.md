# Story App

Story App is an Android application where users can share their stories with images and GPS-tagged locations. Users can easily write their stories, add photos, and let the app automatically record the location. Others can view these stories on a map, making it easy to connect and explore experiences from around the world.

## Features

- **Share Stories with Images and Locations:** Users can upload images with text descriptions and share their locations using GPS.
- **Seamless Login and Registration:** Custom Material Text Input layouts enhance security by detecting passwords shorter than 8 characters and incorrectly formatted emails.
- **Secure Data Storage:** Token data and user information are securely stored in the Data Store, ensuring a smooth and personalized experience.
- **Map Integration:** Google Maps API integration allows users to open a map within the app and see markers at the locations of shared stories.
- **Infinite Scrolling:** Paging 3 and Remote Mediator provide an infinite scrolling experience, with data stored in a Room database for offline access.

## Technical Details

### Sharing Stories with Images and Locations

Users can upload images with text descriptions seamlessly using Retrofit. Additionally, they can share their location by using GPS if they check the "Post with location" checkbox, ensuring precise location details are included.

### Seamless Login and Registration

The app uses custom Material Text Input layouts for login and registration. These inputs detect if a password is less than 8 characters or if an email is incorrectly formatted, enhancing security and usability.

### Secure Data Storage

Upon successful login, the app securely stores your token data and user information in the Data Store. This ensures a smooth and personalized experience each time you use the app.

### Map Integration

Story App integrates Google Maps API, enabling users to open a map within the app that displays markers at the locations where stories have been shared. This feature allows users to visually explore and navigate through various stories based on their geographic locations.

### Infinite Scrolling

The app uses Paging 3 and Remote Mediator to provide an infinite scrolling experience. Data is stored in a Room database, ensuring smooth and efficient access to stories even when offline.

## Screenshots

<div style="display: flex; justify-content: space-between;">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_splash.png?raw=true" width="20%">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_login.png?raw=true" width="20%">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_register.png?raw=true" width="20%">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_main.png?raw=true" width="20%">
</div>
<div style="display: flex; justify-content: space-between;">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_upload2.png?raw=true" width="20%">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_main2.png?raw=true" width="20%">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_detail.png?raw=true" width="20%">
  <img src="https://github.com/dzikryaji/story-app/blob/master/screen_map.png?raw=true" width="20%">
</div>
