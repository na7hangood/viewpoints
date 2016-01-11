#Viewpoints Atom Editor

This service allows viewpoint atoms to be created and managed. A viewpoint atom is a collection of people's (or potentially organisations)
quoted view on an issue or subject. The first use of viewpoints will be showing candidates in the US elections and their views
on the issues in the election.

##Model

The viewpoint system models **commenters**, these are the people, political candidates or parties that are quoted on **subjects**.

The viewpoint atom is rendered from a number of commenters quotes on a given subject, the commenters can be different to provide a
comparison between different people opinions or can be the same commenter to show how someone's view has changed over time 
(useful for when people u-turn etc).

The model is stored in dynamo and sent to the content API as thrift. The thrift definition of the model is in the 
[content atom project](https://github.com/guardian/content-atom)

###Commenter

A commenter has: 

* name - the name of the person or organisation
* picture - a picture to display with the commenter, typically a cut out head shot
* description - A longer description (e.g. *"Former secretary of state Hillary Clinton"*)
* party - an optional party
* category - a grouping used in the viewpoint editor to filter available commenters, not used in the atom model

###Subject

The subject is the topic or issue the is being commented on. A viewpoint atom is generated for each subject. A subject has:

* name - the title
* link - an optional url that would be used when the atom is shared
* viewpoints - a list of viewpoints from commenters on this subject

###Viewpoints

Viewpoints are the meat of what is displayed in the viewpoint atom (predictably). A viewpoint has:

* commenter - who this view is attributed to
* quote - the text
* link - an optional url for reading more
* date - an optional date

##Running

Before you run for the first time you will need to run `./scripts/setup.sh` this will install and compile all the frontend
dependencies needed for the app (you may need to install npm before you can run this successfully). If any frontend
dependencies are changed you should should re run the setup script.

The Nginx setup uses the [dev-nginx](https://github.com/guardian/dev-nginx) tool. after running this the viewpoints editor
will be available on [https://viewpoints.local.dev-gutools.co.uk](https://viewpoints.local.dev-gutools.co.uk)

The viewpoints editot is a standard play app, fire up sbt and run.

By default if you change any frontend code, you will need to recompile the assets using `./scripts/setup.sh` but there
are alternatives:

###Client Side Development

We use webpack to compile the assets for this project. You have the option to run `./scripts/setup.sh` after each change
as mentioned above, or alternatively you can choose to use the alternative startup scripts provided:

`./scripts/start.sh` This starts a webpack watcher in addition to running the application - The watcher will compile
unminified code when it detects a change to the javascript. Refresh the webpage to see the new code.

##Developing

The backend code used the standard scala play layout.

The frontend components live in the public directory in root. Css is provided by [bootstrap](http://getbootstrap.com/)

