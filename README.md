# Schedule Manager
Schedule Manager Application

WGU Software 2 Performance Assessment.
This application is a scheduling application using a MySQL database. 

Default login credentials are:

- Username: test
- Password: test

### Project Features
- Login handling.
- Database connectivity
- JavaFX user interface
- MySQL database manipulation
- Customer Management
- Appointment Creation
- Reporting

### Project Requirements
- Using resource bundles to automatically account for multiple system languages. This was only required for the login controller.
- Automatically log login data to a text file.
- Managing Timestamps and converting them to various timezones. The database is based on UTC time. So all time data pulled out of the database is converted to the users time zone. When adding data to the database it is converted from the user time zone to UTC.
- Lambdas were required to be implemented to show an understanding of their function. I used an abstract expression from the Model/Message.Java for an alert in the AppointmentController, and CustomerController. As well as implementing lambdas to handle stage.OnCloseRequest and stage.setOnHiding.
- Alerts 
