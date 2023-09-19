package hangman.view

import scalafxml.core.macros.sfxml
import hangman.MainApp

@sfxml
class RootLayoutController(){
  def handleClose(): Unit = {
    MainApp.stage.close()
  }
  def handleHelp(): Unit = {
    MainApp.showInstructions()
  }
}