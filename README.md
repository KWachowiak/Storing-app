=========

## Storing files application in PostgreSQL database

### The Application overview
This "RESTful" Spring Application has been made for storing downloaded files in PostgreSQl database. It provides storing files like images, documents or websites in dedicated database. The application prevents putting non existing contents, by checking connections with its origin servers using http statuses. Downloading from external links for website works asynchronously (after each request - file is downloaded independently), and for the rest synchronously (after each request file wait in the queue for being downloaded). Additionally application can store only unique items, it is impossible to use 2 exact url addresses or set item name if it is already taken. This application provides several endpoints for saving files in DB, downloaded them or getting its representation in JSON format

### The application REST Api
- GET endpoints:
    - listing endpoints:
        - for listing all resources records represented in JSON format use "/resources"
        - for getting specific record in JSON representation type "/resource/_id_"
        - for listing all resources, which its _file names_, _record names_ or _url addresses_ contains characters use "/resources?like=_phrase_"
    - downloading endpoints:
        - for downloading specific item from DB type "/resources/download/_name_"
        - for getting list of items represented in JSON use "/resources/download/many/_name1_,_name2_,..."
- POST endpoint:
    - saving endpoint:
        - for save new content add JSON object and use "/resource"
- JSON POST template:<br />
   `{
        "name" :"",
        "description" : "",
        "url" : "",
        "fileName" : ""
    }`
