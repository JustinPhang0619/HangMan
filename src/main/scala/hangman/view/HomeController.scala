package hangman.view

import scalafxml.core.macros.sfxml
import hangman.MainApp


@sfxml
class HomeController() {
  def getStarted(): Unit = {
  }

  def getInstructions(): Unit = {
    MainApp.showInstructions()
  }

  def getGame(): Unit = {
    MainApp.showGame()
  }

  def getManage(): Unit = {
    MainApp.showManage()
  }

  def quit(): Unit = {
    MainApp.stage.close()
  }
}