This project is a Spring Boot (https://spring.io/projects/spring-boot) application 
that wraps the Spec Math library and Web UI into a single deployable unit to execute
locally or up on Google Cloud's AppEngine.

Steps to build and deploy locally
1) Run the following command
`mvn install`
2) Run the following command to start the server
`mvn spring-boot:run`
3) The UI can be viewed at http://localhost:8080 
4) The Rest service can be tested by using curl:
`curl http://localhost:8080/v1/operations/merge -XPOST -d "{\"spec1\":\"foo\", \"spec2\":\"bar\", \"defaults\":\"foobar\"}" -H "Content-Type: application/json" -H "Accept: application/json"`


Steps to build and deploy to Google Cloud AppEngine
1) Install Google Cloud SDK from here: https://cloud.google.com/sdk/install
2) Update your google cloud sdk with the app engine components
`gcloud components install app-engine-java`
3) Build and deploy the application to your Google Cloud Project
` mvn package appengine:deploy -Dapp.deploy.projectId=YOUR_CLOUD_PROJECT_ID -Dapp.deploy.version=1 -Dapp.deploy.promote=true`
4) Once complete, run this command to view the UI:
`gcloud app browse -s specmath`