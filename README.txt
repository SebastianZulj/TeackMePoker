Java JDK: 17 (Local machine) 
In project structure:
	Java SDK: 17.0.2 (Intellij)
	Language: 19
	Select output folder (not found in ZIP file)
documentation: https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html
installation: https://www.oracle.com/java/technologies/downloads (Windows x64 for the university computers)

##############

You will use JavaFX

JavaFX documentation: https://openjfx.io/index.html
JavaFX version: 17.0.2
Tools: Scene Builder
Scene Builder download: https://gluonhq.com/products/scene-builder
Scene Builder Wiki: https://github.com/gluonhq/scenebuilder/wiki/Basic-JavaFX-project-with-Scene-Builder

##############

link to Getting started with JavaFX: https://openjfx.io/openjfx-docs/#install-javafx 
Please the steps following steps to get started with your preferred IDE.
	- JavaFX and IntelliJ
		- Modular from Maven.

##############

There is a setting in the file path that creates an error when you first run the application in the following class:

C:/Users###/Desktop/Projects/TeachMePoker/Program/demo/target/classes/com/example/demo/GameState.fxml:163

This is the file path
<Image preserveRatio="true" smooth="true" url="@/images/aiBarWithCards.png" />

should be written @images and not  @/images, so the correct is as follows:
<Image preserveRatio="true" smooth="true" url="@/images/aiBarWithCards.png" />

Just Ctrl + F (or cmd for Mac) and replace all instances in that class.